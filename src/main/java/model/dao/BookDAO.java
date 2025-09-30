package model.dao;

import model.Database;
import model.pojo.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void deleteBookById(int id) {
        String sql = "delete from book where id = ?";
        try(Connection connection = Database.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql))  {
            preparedStatement.setInt(1, id);
            int affected = preparedStatement.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Failed to delete book");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Book findBookById(int id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("id"),
                            rs.getInt("shelf_id"),
                            rs.getString("title"),
                            rs.getString("author")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsByTitleAndShelfId(String title, int shelfId) {
        String sql = "SELECT * FROM book WHERE title = ? AND shelf_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, shelfId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String updateBookTitle(String title, String author, String newTitle) {
        String sql = "UPDATE book SET title = ? WHERE title = ? and author = ?";
        try(Connection conn = Database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, author);
            // TODO return 1/0 depending on success
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
}
