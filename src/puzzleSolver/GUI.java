package puzzleSolver;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Scanner;

public class GUI extends Application {
    private ImageView solutionView = new ImageView();
    private Solver solver;

    private Label executionTimeLabel = new Label("Waktu pencarian: - ms");
    private Label casesConsideredLabel = new Label("Banyak kasus yang ditinjau: -");
    private Label warningLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Styling for the warning label
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        warningLabel.setText(""); // Initially empty

        // Styling for buttons
        String buttonStyle = "-fx-font-size: 14px; -fx-background-color: #000072; -fx-text-fill: white; " +
                "-fx-padding: 8px 15px; -fx-background-radius: 5px; -fx-border-radius: 5px;";

        // Declaration of buttons
        Button loadFileButton = new Button("Masukkan File");
        Button solveButton = new Button("Solve");
        Button saveTxtButton = new Button("Simpan txt");
        Button saveImageButton = new Button("Simpan gambar");

        // Styling for buttons
        loadFileButton.setStyle(buttonStyle);
        solveButton.setStyle(buttonStyle);
        saveTxtButton.setStyle(buttonStyle);
        saveImageButton.setStyle(buttonStyle);

        // Button Actions
        loadFileButton.setOnAction(e -> {
            loadPuzzleFile(primaryStage);
            warningLabel.setText(""); // Clear warning when file is loaded
        });
        solveButton.setOnAction(e -> solvePuzzle());
        saveTxtButton.setOnAction(e -> saveSolutionTxt());
        saveImageButton.setOnAction(e -> saveSolutionImage());

        // Save Buttons (Below Solution Image)
        HBox saveBox = new HBox(10, saveTxtButton, saveImageButton);
        saveBox.setAlignment(Pos.CENTER);

        solutionView.setFitWidth(400);
        solutionView.setFitHeight(400);
        solutionView.setPreserveRatio(true);
        solutionView.setSmooth(true);
        solutionView.setCache(true);

        // Image Container
        StackPane imageContainer = new StackPane(solutionView);
        imageContainer.setMinSize(400, 400);
        imageContainer.setMaxSize(400, 400);
        imageContainer.setPrefSize(400, 400);

        // Left panel
        VBox solutionBox = new VBox(20, imageContainer, saveBox);
        solutionBox.setAlignment(Pos.CENTER_LEFT);
        solutionBox.setPadding(new Insets(10, 0, 10, 0));
        solutionBox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        solutionBox.setPrefSize(400, 500);
        solutionBox.setMinSize(400, 400);
        solutionBox.setMaxSize(400, 600);

        // Input and Solve Buttons (Right Side)
        HBox inputSolveBox = new HBox(10, loadFileButton, solveButton);
        inputSolveBox.setAlignment(Pos.CENTER);

        HBox infoBox = new HBox(10, executionTimeLabel, casesConsideredLabel);
        infoBox.setAlignment(Pos.CENTER);

        // Right Panel (Buttons + Info)
        Label title = new Label("IQ Puzzler Pro Solver");
        title.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-text-fill: #000072; -fx-padding: 10px;");
        title.setAlignment(Pos.CENTER);
        VBox rightButtonsl = new VBox(5, inputSolveBox, warningLabel,infoBox);
        VBox rightPanel = new VBox(15, title, rightButtonsl);
        rightPanel.setAlignment(Pos.CENTER);

        // Main Layout
        HBox mainLayout = new HBox(125, solutionBox, rightPanel);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("IQ Puzzler Pro Solver");
        primaryStage.show();
    }

    private void loadPuzzleFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt")); // Hanya file .txt

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            parsePuzzleFile(file);
        }
    }


    private void parsePuzzleFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            int boardWidth = scanner.nextInt();
            int boardHeight = scanner.nextInt();
            List<List<String>> rawPieces = IO.readInput(file.getName());
            solver = new Solver(boardWidth, boardHeight, rawPieces);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void solvePuzzle() {
        if (solver == null) {
            warningLabel.setText("Error: Input file lebih dulu!");
            return;
        }

        if (solver.solve(0)) {
            solver.printBoard();
            char[][] board = solver.getBoard();
            BufferedImage image = IO.makeImage(board);
            solutionView.setImage(SwingFXUtils.toFXImage(image, null));

            // Update labels
            executionTimeLabel.setText("Waktu Pencarian: " + solver.getExecutionTimeMs() + " ms");
            casesConsideredLabel.setText("Banyak kasus yang ditinjau: " + solver.getCasesConsidered());
            warningLabel.setText("");
        }
    }



    private void saveSolutionTxt() {
        if (solver == null) {
            warningLabel.setText("Error: Solve the puzzle first before saving!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Solution as Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            IO.saveSolutionAsFile(solver.getBoard(), solver.getExecutionTimeMs(), solver.getCasesConsidered());
            System.out.println("Solution saved as: " + file.getAbsolutePath());
            warningLabel.setText("");
        }
    }


    private void saveSolutionImage() {
        if (solver == null) {
            warningLabel.setText("Error: Solve the puzzle first before saving!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Solution Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            BufferedImage image = IO.makeImage(solver.getBoard());
            IO.saveSolutionAsImage(image, file);
            System.out.println("Solution saved as: " + file.getAbsolutePath());
            warningLabel.setText("");
        }
    }

}
