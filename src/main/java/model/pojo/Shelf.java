package model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Interfaces.IBook;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shelf {
    private int id;
    private String name;
    private int cabinetId;






    @Override
    public String toString() {
        return "Shelf with id: " + id;
    }
}
