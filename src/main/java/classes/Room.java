package classes;

import java.util.List;

public class Room {
    private Long id;
    private List<Cupboard> cupboards;

    public Room(Long id) {
        this.id = id;
    }

    public Room(Long id, List<Cupboard> cupboards) {
        this.id = id;
        this.cupboards = cupboards;
    }

    public List<Cupboard> getCupboards() {
        return cupboards;
    }

    public void setCupboards(List<Cupboard> cupboards) {
        this.cupboards = cupboards;
    }

    @Override
    public String toString() {
        return "Room with id: " + id;
    }
}
