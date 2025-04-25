package daa.huffmanca;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class HuffmanTreeDrawer {
    private final AnchorPane pane;
    private final double nodeRadius = 15; // Consistent with previous update
    private final double minHorizontalSpacing = 30; // Minimum to prevent overlap
    private final double baseVerticalSpacing = 80; // Base spacing, adjusted by depth

    public HuffmanTreeDrawer(AnchorPane pane) {
        this.pane = pane;
    }

    public void drawTree(Node root) {
        pane.getChildren().clear();
        if (root == null) {
            System.out.println("drawTree: Root is null, cannot draw tree");
            return;
        }

        double paneWidth = pane.getPrefWidth();
        int treeDepth = getTreeDepth(root);
        int leafCount = getLeafCount(root);

        // Adjust vertical spacing based on tree depth
        double verticalSpacing = baseVerticalSpacing * Math.max(1, 4.0 / treeDepth); // Scale down for deeper trees
        // Calculate initial horizontal spacing based on leaf count
        double initialHSpacing = Math.min(paneWidth / (leafCount + 1), paneWidth / 4);
        initialHSpacing = Math.max(initialHSpacing, minHorizontalSpacing);

        System.out.println("drawTree: Tree depth = " + treeDepth + ", Leaf count = " + leafCount +
                ", verticalSpacing = " + verticalSpacing + ", initialHSpacing = " + initialHSpacing);

        drawFinalTree(root, paneWidth / 2, 50, initialHSpacing, verticalSpacing);
        System.out.println("drawTree: Drew final tree");
    }

    private int getTreeDepth(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getTreeDepth(node.getLeft()), getTreeDepth(node.getRight()));
    }

    private int getLeafCount(Node node) {
        if (node == null) {
            return 0;
        }
        if (node.isLeaf()) {
            return 1;
        }
        return getLeafCount(node.getLeft()) + getLeafCount(node.getRight());
    }

    private void drawNode(Node node, double x, double y) {
        Circle circle = new Circle(x, y, nodeRadius);
        circle.setFill(Color.LIGHTBLUE);
        circle.setStroke(Color.BLACK);

        String text = node.isLeaf() ? String.valueOf(node.getCharacter()) : String.valueOf(node.getFrequency());
        Text textNode = new Text(text);
        textNode.setFill(Color.BLACK);
        textNode.setTextAlignment(TextAlignment.CENTER);

        // Center the text within the circle
        double textWidth = textNode.getBoundsInLocal().getWidth();
        double textHeight = textNode.getBoundsInLocal().getHeight();
        textNode.setX(x - textWidth / 2);
        textNode.setY(y + textHeight / 4); // Slight offset for visual centering

        pane.getChildren().addAll(circle, textNode);
    }

    private void drawEdge(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY + nodeRadius, endX, endY - nodeRadius);
        line.setStroke(Color.BLACK);
        pane.getChildren().add(line);
    }

    private void drawFinalTree(Node root, double x, double y, double hSpacing, double verticalSpacing) {
        if (root == null) {
            return;
        }

        drawNode(root, x, y);

        double childY = y + verticalSpacing;
        double leftX = x - hSpacing;
        double rightX = x + hSpacing;

        if (root.getLeft() != null) {
            drawEdge(x, y, leftX, childY);
            drawFinalTree(root.getLeft(), leftX, childY, Math.max(hSpacing / 2, minHorizontalSpacing), verticalSpacing);
        }
        if (root.getRight() != null) {
            drawEdge(x, y, rightX, childY);
            drawFinalTree(root.getRight(), rightX, childY, Math.max(hSpacing / 2, minHorizontalSpacing), verticalSpacing);
        }
    }
}