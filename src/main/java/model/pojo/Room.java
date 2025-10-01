package model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private int id;
    private String name;


    @Override
    public String toString() {
        return "Room with id: " + id;
    }
}
