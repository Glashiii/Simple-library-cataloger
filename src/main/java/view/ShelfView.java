package view;

import model.pojo.Shelf;

import java.util.List;

public class ShelfView {

    private static final int SHELF_WIDTH = 30;
    private static final int SHELF_HEIGHT = 7;

    public static void render(List<Shelf> shelves) {
        int roomWidth = SHELF_WIDTH + 2, roomHeight, count;

        if (shelves != null) {
            count = Math.min(shelves.size(), 4);
            roomHeight = count * SHELF_HEIGHT + 2;
        }
        else {
            roomHeight = SHELF_HEIGHT + 2;
            count = 0;
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
            Shelf shelf = shelves.get(i);
            int startY = 1 + i * SHELF_HEIGHT;
            drawShelfBlock(canvas, 1, startY, SHELF_WIDTH, SHELF_HEIGHT, shelf.getName());
        }

        for (char[] row : canvas) {
            System.out.println(new String(row));
        }
    }

    private static void drawShelfBlock(char[][] canvas, int x, int y, int w, int h, String name) {
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

        String shortName = name.length() > w - 2 ? name.substring(0, w - 5) + "..." : name;
        int textRow = y + h / 2;
        int padding = (w - 2 - shortName.length()) / 2;
        int startTextX = x + 1 + Math.max(0, padding);

        for (int i = 0; i < shortName.length(); i++) {
            if (startTextX + i < x + w - 1) {
                canvas[textRow][startTextX + i] = shortName.charAt(i);
            }
        }
    }
}