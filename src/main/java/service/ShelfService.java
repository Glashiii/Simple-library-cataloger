package service;

import exceptions.EntityAlreadyExists;
import exceptions.EntityNotFoundException;
import exceptions.QuantityLimitException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.dao.ShelfDAO;
import model.pojo.Shelf;

import java.util.List;

@Setter
@AllArgsConstructor
public class ShelfService {
    private final ShelfDAO shelfDAO;
    private final int MAX_CABINETS = 4;

    public void addShelf(String name, int cabinetId) {
        List<Shelf> shelves = shelfDAO.findAllShelvesByCabinetId(cabinetId);
        if (shelves.size() == 4){
            throw new QuantityLimitException("Can't be more than " + MAX_CABINETS + " cabinets");
        }
        if (findShelfByName(name) != null){
            throw new EntityAlreadyExists("Shelf with name " + name + " already exists");
        }
        shelfDAO.addShelf(new Shelf(0, name, cabinetId));
    }

    public Shelf findShelfByName(String name) {
        return shelfDAO.findShelfByName(name);
    }

    public List<Shelf> getAllShelvesByCabinet(int cabinetId) {
        return shelfDAO.findAllShelvesByCabinetId(cabinetId);
    }

    public void updateShelf(String oldName, String newName) {
        Shelf shelf = shelfDAO.findShelfByName(oldName);
        if (shelf == null) {
            throw new EntityNotFoundException("Shelf with name " + oldName + " does not exist");
        }
        shelfDAO.updateShelfByName(oldName, newName);
    }

    public void deleteShelf(String name) {
        int deleted = shelfDAO.deleteShelfByName(name);
        if (deleted == 0) {
            throw new EntityNotFoundException("Shelf with name " + name + " not found");
        }
    }

}
