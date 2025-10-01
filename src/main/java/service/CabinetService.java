package service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.dao.CabinetDAO;
import model.dao.RoomDAO;
import model.pojo.Cabinet;
import model.pojo.Room;

import java.util.List;
@Getter
@AllArgsConstructor
public class CabinetService {
    private final CabinetDAO cabinetDAO;
    public void addCabinet(String name, int roomId) {
        List<Cabinet> cabinets = cabinetDAO.findAllCabinets();
        if (cabinets.size() == 8) {
            System.out.println("Can't add more than 8 cabinets");
            return;
        }
        if (findCabinetByName(name) != null ) {
            System.out.println("Cabinet already exists");
            return;
        }
        cabinetDAO.addCabinet(new Cabinet(0, name, roomId));
        System.out.println("Added Cabinet " + name);
    }

    public Cabinet findCabinetByName(String name) {
        return cabinetDAO.findCabinetByName(name);
    }

    public List<Cabinet> getAllCabinets() {
        return cabinetDAO.findAllCabinets();
    }

    public void updateCabinet(String oldName, String newName) {
        Cabinet cabinet = cabinetDAO.findCabinetByName(oldName);
        if (cabinet == null) {
            System.out.println("Cabinet not found");
        }
        cabinetDAO.updateCabinetByName(oldName, newName);
    }

    public void deleteCabinet(String name) {
        try {
            cabinetDAO.deleteCabinetByName(name);
        }
        catch (RuntimeException e) {
            System.out.println("Cabinet not found");
        }
    }
}

