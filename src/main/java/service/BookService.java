package service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import model.DTO.BookLocation;
import model.dao.BookDAO;
import model.dao.ShelfDAO;
import model.pojo.Book;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookService {
    private final BookDAO bookDAO;
    private final ShelfDAO shelfDAO;

    public void addBookToShelf(int shelfId, String author, String title) {
        List<Book> books = bookDAO.findAllBooksByShelfId(shelfId);
        if (books.size() == 8) {
            System.out.println("Can't be more than 8 books");
            return;
        }
        if (findBookLocation(title, author) != null) {
            System.out.println("Book already exists");
            return;
        }
        Book book = new Book(0, shelfId, title, author);
        bookDAO.addBook(book);
        System.out.println("Added Book " + title);
    }

    public void deleteBook(String title, String author) {
        if(author == null || author.equals("")) {
            System.out.println("Please enter author");
            return;
        }
        bookDAO.deleteBookByTitleAndAuthor(title, author);
    }

    public void updateBookTitle(String oldTitle, String author, String newTitle) {
        Book book = bookDAO.findBookByTitleAndAuthor(oldTitle, author);
        if (book == null) {
            System.out.println("Book is not found");
            return;
        }
        bookDAO.updateBookByAuthorAndTitle(oldTitle, author, newTitle);
    }

    public BookLocation findBookLocation(String title, String author) {
        return bookDAO.findByTitleAndAuthorWithLocation(title, author);
    }


    public List<BookLocation> findBooksByTitle(String title) {
        List<BookLocation> locations = bookDAO.findAllByTitleWithLocation(title);
        if (locations.isEmpty()) {
            System.out.println("No locations found");
        }
        return bookDAO.findAllByTitleWithLocation(title);
    }

    public List<Book> getAllBooksByShelf(int shelfId) {
        return bookDAO.findAllBooksByShelfId(shelfId);
    }


}
