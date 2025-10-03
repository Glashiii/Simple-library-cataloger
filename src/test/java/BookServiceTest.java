import exceptions.EntityNotFoundException;
import model.dao.BookDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.BookService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookDAO bookDaoMock;

    @InjectMocks
    private BookService bookService;


    //successful
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

    //unsuccessful
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


}
