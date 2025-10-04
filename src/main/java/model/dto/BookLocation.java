package model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookLocation {
    private String bookTitle;
    private String bookAuthor;
    private String shelfName;
    private String cabinetName;
    private String roomName;
}