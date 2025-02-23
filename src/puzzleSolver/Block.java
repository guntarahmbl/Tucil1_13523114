package puzzleSolver;

import java.util.*;

class Block {
    private final int[][] shape;
    private final List<int[][]> variants;

    public Block(List<String> rawPiece) {
        this.shape = parseShape(rawPiece);
        this.variants = generateVariances();
    }

    public List<int[][]> getVariances() {
        return variants;
    }

    private int[][] parseShape(List<String> rawPiece) {
        int rows = rawPiece.size();
        int cols = rawPiece.stream().mapToInt(String::length).max().orElse(0);

        int[][] piece = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            String row = rawPiece.get(i);
            for (int j = 0; j < cols; j++) {
                if (j < row.length()) {
                    piece[i][j] = (row.charAt(j) != ' ') ? 1 : 0;
                } else {
                    piece[i][j] = 0;
                }
            }
        }
        return piece;
    }


    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++)
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        return copy;
    }

    private List<int[][]> generateVariances() {
        List<int[][]> variants = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        // Rotate 0°, 90°, 180°, 270°
        int[][] current = deepCopy(shape);
        for (int i = 0; i < 4; i++) {
            addVariant(current, variants, seen);
            current = rotate(current);
        }

        // Flip origin and rotated
        int[][] flipped = flipHorizontal(shape);
        for (int i = 0; i < 4; i++) {
            addVariant(flipped, variants, seen);
            flipped = rotate(flipped);
        }

        return variants;
    }


    private void addVariant(int[][] piece, List<int[][]> variants, Set<String> seen) {
        String rep = Arrays.deepToString(piece);
        if (!seen.contains(rep)) {
            seen.add(rep);
            variants.add(piece);
        }
    }

    private int[][] rotate(int[][] piece) {
        int rows = piece.length, cols = piece[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                rotated[j][rows - 1 - i] = piece[i][j];
        return rotated;
    }

    private int[][] flipHorizontal(int[][] piece) {
        int rows = piece.length, cols = piece[0].length;
        int[][] flipped = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                flipped[i][cols - 1 - j] = piece[i][j];
        return flipped;
    }
}
