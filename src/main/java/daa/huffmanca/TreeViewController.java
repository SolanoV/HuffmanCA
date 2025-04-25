package daa.huffmanca;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TreeViewController implements Initializable {
    @FXML
    private AnchorPane treePane;
    private Node root;

    public void setRoot(Node root) {
        this.root = root;
        System.out.println("setRoot: Root = " + (root != null ? "not null" : "null"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("TreeViewController: initialize - treePane = " + (treePane != null ? "not null" : "null"));
    }

    public void drawTree() {
        if (root != null && treePane != null) {
            HuffmanTreeDrawer drawer = new HuffmanTreeDrawer(treePane);
            drawer.drawTree(root);
            System.out.println("drawTree: Drawing tree with root = " + (root != null ? "not null" : "null"));
        } else {
            System.out.println("drawTree: Cannot draw - treePane = " + treePane + ", root = " + root);
        }
    }
}