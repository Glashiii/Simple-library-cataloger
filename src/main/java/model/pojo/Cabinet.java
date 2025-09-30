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
public class Cabinet {
    private int id;
    private String name;
    private int roomId;


    @Override
    public String toString() {
        return "Cupboard with id: " + id;
    }
}
