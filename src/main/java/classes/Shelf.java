package classes;

import Interfaces.IBook;

import java.util.List;

public class Shelf {
    private final Long id;
    private List<IBook> books;


    public Shelf(Long id) {
        this.id = id;
    }

    public Shelf(Long id, List<IBook> books) {
        this.id = id;
        this.books = books;
    }


    public List<IBook> getBooks() {
        return books;
    }

    public void setBooks(List<IBook> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Shelf with id: " + id;
    }
}
