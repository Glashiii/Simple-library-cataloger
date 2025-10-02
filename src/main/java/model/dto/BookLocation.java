package model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BookLocation {
    private String bookTitle;
    private String bookAuthor;
    private String shelfName;
    private String cabinetName;
    private String roomName;
}