package classes;

import java.util.List;

public class Cupboard {
    private final Long id;
    private List<Shelf> shelves;

    public Cupboard(Long id) {
        this.id = id;
    }

    public Cupboard(Long id, List<Shelf> shelves) {
        this.id = id;
        this.shelves = shelves;
    }

    public List<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }

    @Override
    public String toString() {
        return "Cupboard with id: " + id;
    }
}
