package daa.huffmanca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class huffmanController extends Application {
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
}
