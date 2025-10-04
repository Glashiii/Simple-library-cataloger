package model.dao;

import model.dto.BookLocation;
import model.Database;
import model.pojo.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void addBook(Book book) {
        String sql = "insert into book(title, author, shelf_id) values(?,?,?)";
        try (Connection connection = Database.connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getShelfId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String updateBookByAuthorAndTitle(String title, String author, String newTitle) {
        String sql = "UPDATE book SET title = ? WHERE title = ? and author = ?";
        try(Connection conn = Database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, author);
            preparedStatement.executeUpdate();
            return newTitle;

//            try (ResultSet rs = preparedStatement.executeQuery()) {
//                if (rs.next()) {
//                    return new Book(rs.getInt("id"), rs.getInt("shelf_id"),
//                            rs.getString("title"), rs.getString("author"));
//                }
//            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> findAllBooksByShelfId(int shelfId) {
        String sql = "SELECT * FROM book WHERE shelf_id = ?";
        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, shelfId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                List<Book> books = new ArrayList<>();
                while (rs.next()) {
                    books.add(new Book(rs.getInt("id"), rs.getInt("shelf_id"),
                            rs.getString("title"), rs.getString("author")));
                }
                return books;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public int deleteBookByTitleAndAuthor(String title, String author) {
        String sql = "DELETE FROM book WHERE title = ? AND author = ?";
        try(Connection connection = Database.connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            int affected = preparedStatement.executeUpdate();
            return affected;
        } catch (SQLException e){
            throw new RuntimeException("Failed to delete book " + title);
        }
    }

    public Book findBookByTitleAndAuthor(String title, String author) {
        String sql = "SELECT * FROM book WHERE title = ? AND author = ?";
        try(Connection conn = Database.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs.getInt("id"), rs.getInt("shelf_id"),
                            rs.getString("title"), rs.getString("author"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BookLocation findByTitleAndAuthorWithLocation(String title, String author) {
        String sql = """
        select b.title, b.author,
               s.name as shelf_name,
               c.name as cabinet_name,
               r.name as room_name
        from book b
        join shelf s   on b.shelf_id = s.id
        join cabinet c on s.cabinet_id = c.id
        join room r    on c.room_id = r.id
        where b.title like ? and b.author like ?
        ORDER BY r.name, c.name, s.name
        """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            ResultSet rs = stmt.executeQuery();

           if (rs.next()) {
                return (new BookLocation(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("shelf_name"),
                        rs.getString("cabinet_name"),
                        rs.getString("room_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<BookLocation> findAllByTitleWithLocation(String titlePart) {
        List<BookLocation> results = new ArrayList<>();
        String sql = """
        
                SELECT b.title, b.author,
               s.name AS shelf_name,
               c.name AS cabinet_name,
               r.name AS room_name
        FROM book b
                 LEFT JOIN shelf s   ON b.shelf_id = s.id
                 LEFT JOIN cabinet c ON s.cabinet_id = c.id
                 LEFT JOIN room r    ON c.room_id = r.id
        WHERE b.title LIKE ?
        ORDER BY r.name, c.name, s.name;
        """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + titlePart + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(new BookLocation(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("shelf_name"),
                        rs.getString("cabinet_name"),
                        rs.getString("room_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}
