import exceptions.EntityAlreadyExists;
import exceptions.EntityNotFoundException;
import exceptions.QuantityLimitException;
import model.dao.CabinetDAO;
import model.pojo.Cabinet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.CabinetService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CabinetServiceTest {

    @Mock
    CabinetDAO cabinetDaoMock;

    @InjectMocks
    CabinetService cabinetService;

    @Test
    void deleteCabinet_whenCabinetExists_shouldCompleteSuccessfully() {
        String name = "Cabinet name";

        when(cabinetDaoMock.deleteCabinetByName(name)).thenReturn(1);

        assertDoesNotThrow(() -> cabinetService.deleteCabinet(name));
    }

    @Test
    void deleteCabinet_whenCabinetNotExists_shouldThrowEntityNotFoundException() {
        String name = "Cabinet name";

        when(cabinetDaoMock.deleteCabinetByName(name)).thenReturn(0);

        assertThrows(EntityNotFoundException.class, () -> cabinetService.deleteCabinet(name));
    }

    @Test
    void addCabinet_whenLimitReached_shouldThrowQuantityLimitException() {
        List<Cabinet> fullListOfCabinets = IntStream.range(0, 8)
                .mapToObj(i -> new Cabinet(i, "Cabinet " + i, 0))
                .collect(Collectors.toList());


        when(cabinetDaoMock.findAllCabinets()).thenReturn(fullListOfCabinets);

        assertThrows(QuantityLimitException.class, () -> {
            cabinetService.addCabinet("new cabinet", 0);
        });
    }

    @Test
    void addCabinet_whenCabinetAlreadyExists_shouldThrowEntityAlreadyExists() {
        String existingName = "Existing cabinet name";
        when(cabinetDaoMock.findAllCabinets()).thenReturn(Collections.emptyList());
        when(cabinetDaoMock.findCabinetByName(existingName)).thenReturn(new Cabinet(1, existingName, 0));

        assertThrows(EntityAlreadyExists.class, () -> {
            cabinetService.addCabinet(existingName, 0);
        });
    }

    @Test
    void addCabinet_whenSuccessful_shouldCallDaoAddCabinet() {
        String newName = "New cabinet name";

        when(cabinetDaoMock.findAllCabinets()).thenReturn(Collections.emptyList());
        when(cabinetDaoMock.findCabinetByName(newName)).thenReturn(null);


        assertDoesNotThrow(() -> {
            cabinetService.addCabinet(newName, 0);
        });

    }

    @Test
    void updateCabinet_whenCabinetExists_shouldCallDaoUpdate() {
        String oldName = "old name";
        String newName = "new name";
        when(cabinetDaoMock.findCabinetByName(oldName)).thenReturn(new Cabinet(1, oldName, 0));

        assertDoesNotThrow(() -> {
            cabinetService.updateCabinet(oldName, newName);
        });
    }

    @Test
    void updateCabinet_whenCabinetDoesNotExist_shouldThrowEntityNotFoundException() {
        String oldName = "not existing cabinet name";
        String newName = "new name";
        when(cabinetDaoMock.findCabinetByName(oldName)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            cabinetService.updateCabinet(oldName, newName);
        });

    }

}
