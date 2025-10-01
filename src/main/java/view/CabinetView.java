package view;

import model.pojo.Cabinet;

import java.util.List;

public class CabinetView {

    private static final int CABINET_WIDTH = 15;
    private static final int CABINET_HEIGHT = 5;
    private static final int GAP_BETWEEN_ROWS = 10;


    public static void render(List<Cabinet> cabinets) {
        int roomWidth = (CABINET_WIDTH + 2) * 4 + 1;
        int roomHeight = (CABINET_HEIGHT * 2) + GAP_BETWEEN_ROWS + 2;

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
//        canvas[0][0] = canvas[0][roomWidth - 1] = '+';
//        canvas[roomHeight - 1][0] = canvas[roomHeight - 1][roomWidth - 1] = '+';

        try{
            for (int i = 0; i < cabinets.size() && i < 8; i++) {
                Cabinet c = cabinets.get(i);

                int row = i / 4; // 0 -> upper, 1 -> lower
                int col = i % 4;

                int startY = 1 + (row == 1 ? CABINET_HEIGHT + GAP_BETWEEN_ROWS : 0);
                int startX = 1 + col * (CABINET_WIDTH + 2);

                drawCabinet(canvas, startX, startY, CABINET_WIDTH, CABINET_HEIGHT, c.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        for (int i = 0; i < canvas.length; i++) {
            System.out.println(new String(canvas[i]));
        }
    }

    private static void drawCabinet(char[][] canvas, int x, int y, int w, int h, String name) {

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

