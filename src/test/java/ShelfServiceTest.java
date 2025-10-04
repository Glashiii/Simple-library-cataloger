import exceptions.EntityAlreadyExists;
import exceptions.EntityNotFoundException;
import exceptions.QuantityLimitException;
import model.dao.ShelfDAO;
import model.pojo.Shelf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.ShelfService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelfServiceTest {

    @Mock
    private ShelfDAO shelfDAO;

    @InjectMocks
    private ShelfService shelfService;

    @Test
    void addShelf_whenLimitReached_shouldThrowQuantityLimitException() {
        int cabinetId = 1;
        List<Shelf> fullListOfShelves = IntStream.range(0, 4)
                .mapToObj(i -> new Shelf(i, "Shelf " + i, cabinetId))
                .collect(Collectors.toList());

        when(shelfDAO.findAllShelvesByCabinetId(cabinetId)).thenReturn(fullListOfShelves);

        assertThrows(QuantityLimitException.class, () -> {
            shelfService.addShelf("new name", cabinetId);
        });

    }

    @Test
    void addShelf_whenShelfAlreadyExists_shouldThrowEntityAlreadyExists() {
        int cabinetId = 1;
        String existingName = "Exisiting shelf name";
        when(shelfDAO.findAllShelvesByCabinetId(cabinetId)).thenReturn(Collections.emptyList());
        when(shelfDAO.findShelfByName(existingName)).thenReturn(new Shelf(1, existingName, cabinetId));

        assertThrows(EntityAlreadyExists.class, () -> {
            shelfService.addShelf(existingName, cabinetId);
        });
    }

    @Test
    void addShelf_whenSuccessful_shouldCallDaoAddShelf() {
        int cabinetId = 1;
        String newName = "new name";
        when(shelfDAO.findAllShelvesByCabinetId(cabinetId)).thenReturn(Collections.emptyList());
        when(shelfDAO.findShelfByName(newName)).thenReturn(null);

        assertDoesNotThrow(() -> {
            shelfService.addShelf(newName, cabinetId);
        });

    }

    @Test
    void updateShelf_whenShelfExists_shouldCallDaoUpdate() {
        String oldName = "old name";
        String newName = "new name";
        when(shelfDAO.findShelfByName(oldName)).thenReturn(new Shelf(1, oldName, 1));

        assertDoesNotThrow(() -> {
            shelfService.updateShelf(oldName, newName);
        });
    }

    @Test
    void updateShelf_whenShelfDoesNotExist_shouldThrowEntityNotFoundException() {
        String oldName = "Not existing old name";
        String newName = "new name";
        when(shelfDAO.findShelfByName(oldName)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            shelfService.updateShelf(oldName, newName);
        });

    }

    @Test
    void deleteShelf_whenShelfExists_shouldCompleteSuccessfully() {
        String name = "name";
        when(shelfDAO.deleteShelfByName(name)).thenReturn(1);

        assertDoesNotThrow(() -> {
            shelfService.deleteShelf(name);
        });

    }

    @Test
    void deleteShelf_whenShelfDoesNotExist_shouldThrowEntityNotFoundException() {
        String name = "Not existing name";
        when(shelfDAO.deleteShelfByName(name)).thenReturn(0);

        assertThrows(EntityNotFoundException.class, () -> {
            shelfService.deleteShelf(name);
        });

    }
}