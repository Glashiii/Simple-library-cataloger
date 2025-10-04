package service;


import exceptions.EntityAlreadyExists;
import exceptions.EntityNotFoundException;
import exceptions.QuantityLimitException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import model.dto.BookLocation;
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
    private final int MAX_BOOKS = 8;

    public void addBookToShelf(int shelfId, String author, String title) {
        List<Book> books = bookDAO.findAllBooksByShelfId(shelfId);
        if (books.size() == MAX_BOOKS) {
            throw new QuantityLimitException("Maximum number of books on 1 shelf is " + MAX_BOOKS);
        }
        try{
            findBookLocation(title, author, shelfId);
            throw new EntityAlreadyExists("Book with title " + title + " and author " + author +
                    " already exists on this shelf");
        } catch (EntityNotFoundException ignored){
        }

        Book book = new Book(0, shelfId, title, author);
        bookDAO.addBook(book);
    }

    public void deleteBook(String title, String author) {
        int deleted = bookDAO.deleteBookByTitleAndAuthor(title, author);
        if (deleted == 0) {
            throw new EntityNotFoundException("Book with title " + title + " and author " + author + " not found");
        }
    }

    public void updateBookTitle(String oldTitle, String author, String newTitle) {
        Book book = bookDAO.findBookByTitleAndAuthor(oldTitle, author);
        if (book == null) {
            throw new EntityNotFoundException("Book with title " + oldTitle + " and author " + author + " not found");
        }
        bookDAO.updateBookByAuthorAndTitle(oldTitle, author, newTitle);
    }

    public BookLocation findBookLocation(String title, String author, int shelfId) {
        BookLocation book = bookDAO.findByTitleAndAuthorWithLocation(title, author, shelfId);
        if (book == null) {
            throw new EntityNotFoundException("Book with title " + title + " and author " + author + " not found");
        }
        return book;
    }


    public List<BookLocation> findBooksByTitle(String title) {
        List<BookLocation> locations = bookDAO.findAllByTitleWithLocation(title);
        if (locations.isEmpty()) {
           throw new EntityNotFoundException("Books with title " + title + " not found");
        }
        return bookDAO.findAllByTitleWithLocation(title);
    }

    public List<Book> getAllBooksByShelf(int shelfId) {
        return bookDAO.findAllBooksByShelfId(shelfId);
    }


}
