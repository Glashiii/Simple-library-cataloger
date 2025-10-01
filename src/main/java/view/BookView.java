package view;

import model.pojo.Book;
import java.util.List;

public class BookView {

    private static final int BOOK_WIDTH = 16;
    private static final int BOOK_HEIGHT = 9;
    private static final int MAX_BOOKS = 8;

    public static void render(List<Book> books) {
        int roomHeight = BOOK_HEIGHT + 2, roomWidth, count;

        if (books == null){
            count = 0;
            roomWidth = BOOK_WIDTH+2;
        }
        else{
            count = Math.min(books.size(), MAX_BOOKS);
            roomWidth = (BOOK_WIDTH * count) + 2;
        }

        char[][] canvas = new char[roomHeight][roomWidth];

        for (int i = 0; i < roomHeight; i++) {
            for (int j = 0; j < roomWidth; j++) {
                canvas[i][j] = ' ';
            }
        }

        for (int x = 0; x < roomWidth; x++) {
            canvas[0][x] = '-';
            canvas[roomHeight - 1][x] = '-';
        }
        for (int y = 0; y < roomHeight; y++) {
            canvas[y][0] = '|';
            canvas[y][roomWidth - 1] = '|';
        }
        canvas[0][0] = canvas[0][roomWidth - 1] = '+';
        canvas[roomHeight - 1][0] = canvas[roomHeight - 1][roomWidth - 1] = '+';

        for (int i = 0; i < count; i++) {
            Book b = books.get(i);
            int startX = 1 + i * BOOK_WIDTH;
            int startY = 1;
            drawBookBlock(canvas, startX, startY, BOOK_WIDTH, BOOK_HEIGHT, b.getTitle());
        }

        for (char[] row : canvas) {
            System.out.println(new String(row));
        }
    }

    private static void drawBookBlock(char[][] canvas, int x, int y, int w, int h, String title) {
        for (int i = 0; i < w; i++) {
            canvas[y][x + i] = '-';
            canvas[y + h - 1][x + i] = '-';
        }
        for (int j = 0; j < h; j++) {
            canvas[y + j][x] = '|';
            canvas[y + j][x + w - 1] = '|';
        }
        canvas[y][x] = canvas[y][x + w - 1] = '+';
        canvas[y + h - 1][x] = canvas[y + h - 1][x + w - 1] = '+';

        String shortTitle = title.length() > w - 2 ? title.substring(0, w - 5) + "..." : title;
        int textRow = y + h / 2;
        int padding = (w - 2 - shortTitle.length()) / 2;
        int startTextX = x + 1 + Math.max(0, padding);

        for (int i = 0; i < shortTitle.length(); i++) {
            if (startTextX + i < x + w - 1) {
                canvas[textRow][startTextX + i] = shortTitle.charAt(i);
            }
        }
    }
}