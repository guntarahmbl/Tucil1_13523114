package puzzleSolver;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filename = "input.txt";
        List<List<String>> input = IO.readInput(filename);

        if (input.isEmpty()) {
            System.out.println("Error: No valid input.");
            return;
        }

        int boardWidth = Integer.parseInt(input.get(0).get(0));
        int boardHeight = Integer.parseInt(input.get(0).get(1));

        Solver solver = new Solver(boardWidth, boardHeight, input);
        solver.solvePuzzle();
    }
}
