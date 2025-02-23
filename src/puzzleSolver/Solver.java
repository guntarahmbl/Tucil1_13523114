package puzzleSolver;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.Scanner;

class Solver {
    private final int boardWidth;
    private final int boardHeight;
    private final List<List<String>> rawPieces;
    private final char[][] board;
    private final List<Block> blocks = new ArrayList<>();
    private int casesConsidered = 0;
    private double executionTimeMs;



    // ANSI color codes for different letters
    private static final String[] COLORS = {
            "\u001B[31m", // Red
            "\u001B[32m", // Green
            "\u001B[33m", // Yellow
            "\u001B[34m", // Blue
            "\u001B[35m", // Magenta
            "\u001B[36m", // Cyan
            "\u001B[37m", // White
            "\u001B[91m", // Bright Red
            "\u001B[92m", // Bright Green
            "\u001B[93m", // Bright Yellow
            "\u001B[94m", // Bright Blue
            "\u001B[95m", // Bright Magenta
            "\u001B[96m", // Bright Cyan
            "\u001B[97m"  // Bright White
    };
    private static final String RESET = "\u001B[0m";

    public Solver(int boardWidth, int boardHeight, List<List<String>> rawPieces) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.board = new char[boardHeight][boardWidth];
        this.rawPieces = rawPieces;
        initializeBoard();
        initializeBlocks(rawPieces);
    }

    public char[][] getBoard() {
        return board;
    }
    public double getExecutionTimeMs(){
        return executionTimeMs;
    }
    public int getCasesConsidered(){
        return casesConsidered;
    }
    private void initializeBoard() {
        for (int i = 0; i < boardHeight; i++) {
            Arrays.fill(board[i], '.'); // Use '.' for empty spaces
        }
    }

    private void initializeBlocks(List<List<String>> rawPieces) {
        rawPieces.remove(0); // First element contains board dimensions
        for (List<String> rawPiece : rawPieces) {
            blocks.add(new Block(rawPiece));
        }
    }

    private boolean canPlacePiece(int[][] piece, int row, int col) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] == 1) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow >= boardHeight || newCol >= boardWidth || board[newRow][newCol] != '.') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placePiece(int[][] piece, int row, int col, int pieceIndex) {
        char pieceLetter = rawPieces.get(pieceIndex).get(0).charAt(0); //get unique letter form input txt

        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] == 1) {
                    board[row + i][col + j] = pieceLetter;
                }
            }
        }
    }

    private void removePiece(int[][] piece, int row, int col) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] == 1) {
                    board[row + i][col + j] = '.'; // Reset cell
                }
            }
        }
    }

    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '.') return false;
            }
        }
        return true;
    }

    public boolean solve(int pieceIndex) {
        casesConsidered++; // Count recursive calls

        // If all blocks are placed, board is full
        if (pieceIndex >= blocks.size()) {
            return isBoardFull();
        }
        Block block = blocks.get(pieceIndex);
        boolean placedAtLeastOnce = false;

        for (int[][] pieceVariant : block.getVariances()) {
            for (int row = 0; row < boardHeight; row++) {
                for (int col = 0; col < boardWidth; col++) {
                    if (canPlacePiece(pieceVariant, row, col)) {
                        placedAtLeastOnce = true;
                        placePiece(pieceVariant, row, col, pieceIndex);

                        if (solve(pieceIndex + 1)) {
                            return true;
                        }

                        removePiece(pieceVariant, row, col);
                    }
                }
            }
        }

        // If no placement, no solution
        if (!placedAtLeastOnce) {
            return false;
        }

        return false;
    }


    public void solvePuzzle() {
        long startTime = System.nanoTime();

        if (solve(0)) {
            long endTime = System.nanoTime();
            executionTimeMs = (endTime - startTime) / 1_000_000.0;
            System.out.println("Solution found:");
            printBoard();
            System.out.println("Execution time: " + executionTimeMs + " ms");
            System.out.println("Total cases considered: " + casesConsidered);

            Scanner scanner = new Scanner(System.in);

            // Ask to save the solution as text
            System.out.print("Do you want to save the solution to a text file? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                IO.saveSolutionAsFile(this.board, executionTimeMs, casesConsidered);
                System.out.println("Solution saved to solution.txt.");
            } else {
                System.out.println("Solution not saved.");
            }

            // Ask to save the solution as image
            System.out.print("Do you want to save the solution as an image? (yes/no): ");
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                BufferedImage image = IO.makeImage(this.board);
                IO.saveSolutionAsImage(image, new File("solution.png"));
                System.out.println("Solution saved as solution.png.");
            } else {
                System.out.println("Solution image not saved.");
            }
        } else {
            System.out.println("No solution found.");
        }
    }



    public void printBoard() {
        Map<Character, String> colorMap = new HashMap<>();
        int colorIndex = 0;

        // Assign colors to each letter
        for (char[] row : board) {
            for (char cell : row) {
                if (cell != '.' && !colorMap.containsKey(cell)) {
                    colorMap.put(cell, COLORS[colorIndex % COLORS.length]);
                    colorIndex++;
                }
            }
        }

        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '.') {
                    System.out.print(cell + " ");
                } else {
                    System.out.print(colorMap.get(cell) + cell + RESET + " ");
                }
            }
            System.out.println();
        }
    }
}
