import exceptions.EntityAlreadyExists;
import exceptions.EntityNotFoundException;
import exceptions.QuantityLimitException;
import model.dao.BookDAO;
import model.dto.BookLocation;
import model.pojo.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.BookService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookDAO bookDaoMock;

    @InjectMocks
    private BookService bookService;


    @Test
    void deleteBook_whenBookExists_shouldCompleteSuccessfully() {
        String title = "Some title";
        String author = "Some author";

        when(bookDaoMock.deleteBookByTitleAndAuthor(title, author))
                .thenReturn(1);

        assertDoesNotThrow(() -> {
            bookService.deleteBook(title, author);
        });

    }

    @Test
    void deleteBook_whenBookDoesNotExist_shouldThrowEntityNotFoundException() {
        String title = "Some unreal title";
        String author = "Some unreal author";

        when(bookDaoMock.deleteBookByTitleAndAuthor(title, author))
                .thenReturn(0);

        assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteBook(title, author);
        });


        verify(bookDaoMock, times(1)).deleteBookByTitleAndAuthor(title, author);
    }

    @Test
    void addBookToShelf_whenSuccessful_shouldCallAddBook() {
        int shelfId = 1;
        String title = "new title";
        String author = "new author";

        when(bookDaoMock.findAllBooksByShelfId(shelfId)).thenReturn(Collections.emptyList());
        when(bookDaoMock.findByTitleAndAuthorWithLocation(title, author)).thenReturn(null);

        assertDoesNotThrow(() -> {
            bookService.addBookToShelf(shelfId, author, title);
        });

        verify(bookDaoMock, times(1)).addBook(any(Book.class));
    }

    @Test
    void addBookToShelf_whenBookAlreadyExists_shouldThrowEntityAlreadyExists() {

        int shelfId = 1;
        String title = "existing title";
        String author = "some author";

        when(bookDaoMock.findAllBooksByShelfId(shelfId)).thenReturn(Collections.emptyList());

        when(bookDaoMock.findByTitleAndAuthorWithLocation(title, author)).thenReturn(new BookLocation());

        assertThrows(EntityAlreadyExists.class, () -> {
            bookService.addBookToShelf(shelfId, author, title);
        });

        verify(bookDaoMock, never()).addBook(any());
    }
    @Test
    void addBookToShelf_whenShelfIsFull_shouldThrowQuantityLimitException() {
        int shelfId = 1;
        List<Book> fullShelfOfBooks = IntStream.range(0, 8)
                .mapToObj(i -> new Book())
                .collect(Collectors.toList());

        when(bookDaoMock.findAllBooksByShelfId(shelfId)).thenReturn(fullShelfOfBooks);

        assertThrows(QuantityLimitException.class, () -> {
            bookService.addBookToShelf(shelfId, "author", "title");
        });

        verify(bookDaoMock, never()).findByTitleAndAuthorWithLocation(any(), any());
        verify(bookDaoMock, never()).addBook(any());
    }

}
