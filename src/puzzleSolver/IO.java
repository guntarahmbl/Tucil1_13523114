package puzzleSolver;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class IO {
    public static List<List<String>> readInput(String filename) {
        List<List<String>> pieces = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String[] boardInfo = reader.readLine().split(" ");
            if (boardInfo.length < 2) {
                throw new IllegalArgumentException("Invalid input format: Missing board dimensions");
            }

            int boardWidth = Integer.parseInt(boardInfo[1]);
            int boardHeight = Integer.parseInt(boardInfo[0]);

            pieces.add(Arrays.asList(String.valueOf(boardWidth), String.valueOf(boardHeight)));

            String line;
            List<String> currentPiece = new ArrayList<>();
            Character lastBlock = null;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                // Find first non-space character
                char firstChar = ' ';
                for (char c : line.toCharArray()) {
                    if (c != ' ') {
                        firstChar = c;
                        break;
                    }
                }

                // new character = new block
                if (lastBlock != null && firstChar != lastBlock && !currentPiece.isEmpty()) {
                    pieces.add(new ArrayList<>(currentPiece));
                    currentPiece.clear();
                }

                currentPiece.add(line);
                lastBlock = firstChar;
            }

            // last piece
            if (!currentPiece.isEmpty()) {
                pieces.add(new ArrayList<>(currentPiece));
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pieces;
    }

    public static void saveSolutionAsFile(char[][] board, double executionTime, int casesConsidered, File file) {
        try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
            for (char[] row : board) {
                for (char cell : row) {
                    writer.write(cell + " ");
                }
                writer.write("\n");
            }

            // Execution time and cases considered
            writer.write("\nExecution time: " + executionTime + " ms\n");
            writer.write("Total cases considered: " + casesConsidered + "\n");

            System.out.println("Solution saved to " + file.getName());
        } catch (IOException e) {
            System.out.println("Error saving solution: " + e.getMessage());
        }
    }

    public static BufferedImage makeImage(char[][] board) {
        int cellSize = 100;
        int rows = board.length;
        int cols = board[0].length;
        int width = cols * cellSize;
        int height = rows * cellSize;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        HashMap<Character, Color> colorMap = new HashMap<>();
        Random rand = new Random();

        for (char[] row : board) {
            for (char cell : row) {
                if (cell != '.' && !colorMap.containsKey(cell)) {
                    colorMap.put(cell, new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                }
            }
        }

        Font font = new Font("Inter", Font.PLAIN, cellSize / 4);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);

        // Draw the board and letters
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char cell = board[r][c];

                if (cell == '.') {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(colorMap.get(cell));
                }
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);

                g.setColor(Color.BLACK);
                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);

                if (cell != '.') {
                    g.setColor(Color.WHITE);
                    String text = String.valueOf(cell);

                    // Text position
                    int textX = c * cellSize + (cellSize - metrics.stringWidth(text)) / 2;
                    int textY = r * cellSize + ((cellSize - metrics.getHeight()) / 2) + metrics.getAscent();

                    g.drawString(text, textX, textY);
                }
            }
        }

        g.dispose();
        return image;
    }

    public static void saveSolutionAsImage(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }

}
