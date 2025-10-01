package service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import model.dao.ShelfDAO;
import model.pojo.Shelf;

import java.util.List;

@Setter
@AllArgsConstructor
public class ShelfService {
    private final ShelfDAO shelfDAO;

    public void addShelf(String name, int cabinetId) {
        List<Shelf> shelves = shelfDAO.findAllShelvesByCabinetId(cabinetId);
        if (shelves.size() == 4){
            System.out.println("Can't be more than 4 cabinets");
            return;
        }
        if (findShelfByName(name) != null){
            System.out.println("Shelf already exists");
            return;
        }
        shelfDAO.addShelf(new Shelf(0, name, cabinetId));
        System.out.println("Added Shelf " + name);
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
            System.out.println("Shelf not found");
        }
        shelfDAO.updateShelfByName(oldName, newName);
    }

    public void deleteShelf(String name) {
        shelfDAO.deleteShelfByName(name);
    }

}
