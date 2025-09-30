package model.pojo;

import lombok.*;
import model.Interfaces.IBook;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book implements IBook{
    private int id;
    private int shelfId;
    private String title;
    private String author;

}