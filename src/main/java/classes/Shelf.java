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

    public IBook getBookByTitle(String title) throws Exception {
            for (IBook book : books) {
                if (book.getTitle().equals(title)) {
                    return book;
                }
            }
            throw new Exception("No such title");
    }

    public void deleteBookByTitle(String title) throws Exception {
        for (IBook book : books) {
            if (book.getTitle().equals(title)) {
                books.remove(book);
            }
        }
        throw new Exception("No such title");
    }

    public void addBook(IBook book) {
        books.add(book);
    }

    public void removeBook(IBook book) {
        books.remove(book);
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
