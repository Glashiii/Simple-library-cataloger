package service;

import exceptions.EntityAlreadyExists;
import exceptions.EntityNotFoundException;
import exceptions.QuantityLimitException;
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
    private final int MAX_CABINET = 8;
    public void addCabinet(String name, int roomId) {
        List<Cabinet> cabinets = cabinetDAO.findAllCabinets();
        if (cabinets.size() == MAX_CABINET) {
            throw new QuantityLimitException("Maximum number of rooms is " + MAX_CABINET + ".");
        }
        if (findCabinetByName(name) != null ) {
            throw new EntityAlreadyExists("Cabinet with name " + name + " already exists");
        }
        cabinetDAO.addCabinet(new Cabinet(0, name, roomId));
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
            throw new EntityNotFoundException("Cabinet with name " + oldName + " does not exist");
        }
        cabinetDAO.updateCabinetByName(oldName, newName);
    }

    public void deleteCabinet(String name) {
        int deleted =  cabinetDAO.deleteCabinetByName(name);

        if (deleted == 0) {
            throw new EntityNotFoundException("Cabinet with name " + name + " does not exist");
        }
    }
}

