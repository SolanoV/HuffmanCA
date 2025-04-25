package daa.huffmanca;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class huffmanController extends Application implements Initializable {
    @FXML Button compressButton;
    @FXML Button decompressButton;
    @FXML Button openFileButton;
    @FXML Button saveButton;
    @FXML Button treeViewButton;
    @FXML Label statusLabel;
    @FXML Label originalSizeLabel;
    @FXML Label compressedSizeLabel;
    @FXML Label compressPercentLabel;
    @FXML TextArea inputTextArea;
    @FXML TextArea outputTextArea;
    @FXML TableView<characters> frequencyTable;
    private compress compress = new compress();

    public static void run(){ launch(); }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("huffman.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Huffman Coding Compression Tool");
        stage.getIcons().add(new Image("file:src/main/resources/Images/huffmanlogo.png"));
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTooltip(compressButton, "Compress Button");
        setupTooltip(decompressButton, "Decompress Button");
        setupTooltip(openFileButton, "Open File Button");
        setupTooltip(saveButton, "Save Button");
        setupTooltip(treeViewButton, "Tree View Button");

        outputTextArea.setWrapText(true);
        TableColumn<characters, Character> charColumn = new TableColumn<>("Characters");
        charColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCharacter()));
        charColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Character item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.valueOf(item));
            }
        });

        TableColumn<characters, Integer> freqColumn = new TableColumn<>("Frequency");
        freqColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFrequency()));
        freqColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.valueOf(item));
            }
        });

        TableColumn<characters, String> codeColumn = new TableColumn<>("Code");
        codeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCode()));
        codeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });

        charColumn.setPrefWidth(85);
        freqColumn.setPrefWidth(95);
        codeColumn.setPrefWidth(99);
        frequencyTable.getColumns().clear();
        frequencyTable.getColumns().addAll(charColumn, freqColumn, codeColumn);

    }
    private void setupTooltip(Button button, String text){
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.seconds(1));
        Tooltip.install(button, tooltip);
    }

    @FXML
    public void compressButton(ActionEvent event) {
        String input = inputTextArea.getText();
        if (input == null || input.trim().isEmpty()) {
            statusLabel.setText("No Input Found");
            clearStatusLabelAfterDelay();
            return;
        }
        compress.compressInput(input);
        outputTextArea.setText(compress.getOutput());
        frequencyTable.getItems().clear();
        frequencyTable.setItems(FXCollections.observableArrayList(compress.getCharacterList()));
        System.out.println("Character List Size: " + compress.getCharacterList().size());
        for (characters c : compress.getCharacterList()) {
            System.out.println("Char: " + c.getCharacter() + ", Freq: " + c.getFrequency() + ", Code: " + c.getCode());
        }
        originalSizeLabel.setText(Integer.toString(compress.getOriginalSize()) + " bits");
        compressedSizeLabel.setText(Integer.toString(compress.getCompressedSize()) + " bits");
        compressPercentLabel.setText(String.format("%.2f%%", compress.getCompressionPercentage()));


        frequencyTable.refresh();
        statusLabel.setText("Compression Complete");
        clearStatusLabelAfterDelay();
    }
    private void clearStatusLabelAfterDelay() {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> statusLabel.setText(""));
        delay.play();
    }

    @FXML
    public void decompressButton(ActionEvent event) {
        if(outputTextArea.getText() == null || outputTextArea.getText().trim().isEmpty()) {
            statusLabel.setText("Nothing to Decompress");
            clearStatusLabelAfterDelay();
        }
        else {
            compress.setOutput(compress.getInput());
            outputTextArea.setText(compress.getOutput());
            inputTextArea.setText(compress.getOutput());
            frequencyTable.getItems().clear();
            originalSizeLabel.setText("");
            compressedSizeLabel.setText("");
            compressPercentLabel.setText("");
            statusLabel.setText("Decompression Complete");
            clearStatusLabelAfterDelay();
        }
    }
    @FXML
    public void openFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        Stage stage = (Stage) openFileButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                inputTextArea.setText(content.toString());
                statusLabel.setText("File Loaded");
                clearStatusLabelAfterDelay();
            } catch (IOException e) {
                statusLabel.setText("Error Reading File: " + e.getMessage());
                clearStatusLabelAfterDelay();
                e.printStackTrace();
            }
        } else {
            statusLabel.setText("No File Selected");
            clearStatusLabelAfterDelay();
        }
    }
    @FXML
    public void saveButton(ActionEvent event) {
        String output = outputTextArea.getText();
        if (output == null || output.trim().isEmpty()) {
            statusLabel.setText("Nothing to Save");
            clearStatusLabelAfterDelay();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialFileName("compressed.txt");
        Stage stage = (Stage) saveButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(output);
                statusLabel.setText("File Saved");
                clearStatusLabelAfterDelay();
            } catch (IOException e) {
                statusLabel.setText("Error Saving File: " + e.getMessage());
                clearStatusLabelAfterDelay();
                e.printStackTrace();
            }
        } else {
            statusLabel.setText("No File Selected");
            clearStatusLabelAfterDelay();
        }
    }
    @FXML
    public void treeViewButton(ActionEvent event) {
        System.out.println("treeViewButton: compress instance = " + compress);
        System.out.println("treeViewButton: Root = " + (compress.getRoot() != null ? "not null" : "null"));
        if (compress.getRoot() == null) {
            statusLabel.setText("No Huffman Tree Available");
            clearStatusLabelAfterDelay();
            return;
        }

        try {
            Stage treeStage = new Stage();
            treeStage.setTitle("Huffman Tree Visualization");
            treeStage.getIcons().add(new Image("file:src/main/resources/Images/huffmanlogo.png"));
            treeStage.setResizable(false);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("treeView.fxml"));
            Parent root = loader.load();

            TreeViewController controller = loader.getController();
            controller.setRoot(compress.getRoot());
            controller.drawTree();

            Scene scene = new Scene(root);
            treeStage.setScene(scene);
            treeStage.show();
        } catch (Exception e) {
            statusLabel.setText("Error Opening Tree View: " + e.getMessage());
            clearStatusLabelAfterDelay();
            System.err.println("treeViewButton: Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
