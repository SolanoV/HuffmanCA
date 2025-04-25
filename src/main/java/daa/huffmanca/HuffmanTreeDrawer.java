package daa.huffmanca;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HuffmanTreeDrawer {
    private final AnchorPane pane;
    private final double baseNodeRadius = 30; // Increased base radius for better visibility
    private final double paneWidth = 1538; // AnchorPane width
    private final double paneHeight = 691; // AnchorPane height
    private Map<Node, Double> nodeXPositions; // Store x-coordinates for each node
    private double nodeRadius; // Instance variable to store nodeRadius

    public HuffmanTreeDrawer(AnchorPane pane) {
        this.pane = pane;
        this.nodeXPositions = new HashMap<>();
    }

    public void drawTree(Node root) {
        pane.getChildren().clear();
        if (root == null) {
            System.out.println("drawTree: Root is null, cannot draw tree");
            return;
        }

        int treeDepth = getTreeDepth(root);
        int leafCount = getLeafCount(root);

        // Assign nodeRadius as an instance variable
        this.nodeRadius = Math.max(20, baseNodeRadius * Math.min(1, 1500.0 / (paneWidth * Math.max(leafCount, treeDepth))));
        // Minimum spacing between leaves to prevent overlap
        double minLeafSpacing = 3 * nodeRadius;
        // Compute leaf spacing to evenly distribute leaves
        double leafSpacing = Math.max(minLeafSpacing, (paneWidth - 2 * nodeRadius) / Math.max(leafCount, 1));
        // Evenly distribute vertical spacing across levels
        double verticalSpacing = (paneHeight - 2 * nodeRadius - 50) / (treeDepth > 0 ? treeDepth + 1 : 1);

        System.out.println("drawTree: Tree depth = " + treeDepth + ", Leaf count = " + leafCount +
                ", nodeRadius = " + nodeRadius + ", verticalSpacing = " + verticalSpacing +
                ", leafSpacing = " + leafSpacing);

        // Step 1: Assign x-coordinates to all nodes
        assignXPositions(root, leafSpacing);

        // Step 2: Draw the tree using assigned positions
        drawFinalTree(root, nodeRadius + 50, verticalSpacing);
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

    private void collectLeaves(Node node, ArrayList<Node> leaves) {
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            leaves.add(node);
            return;
        }
        collectLeaves(node.getLeft(), leaves);
        collectLeaves(node.getRight(), leaves);
    }

    private void assignXPositions(Node root, double leafSpacing) {
        // Step 1: Collect all leaves in left-to-right order
        ArrayList<Node> leaves = new ArrayList<>();
        collectLeaves(root, leaves);

        // Step 2: Assign x-coordinates to leaves
        double startX = nodeRadius;
        for (int i = 0; i < leaves.size(); i++) {
            double x = startX + i * leafSpacing;
            x = Math.max(nodeRadius, Math.min(paneWidth - nodeRadius, x));
            nodeXPositions.put(leaves.get(i), x);
        }

        // Step 3: Assign x-coordinates to internal nodes by averaging children's positions
        assignInternalNodePositions(root);
    }

    private void assignInternalNodePositions(Node node) {
        if (node == null || node.isLeaf()) {
            return;
        }

        // Recursively assign positions to children
        assignInternalNodePositions(node.getLeft());
        assignInternalNodePositions(node.getRight());

        // Compute the average x-coordinate of children
        double leftX = node.getLeft() != null ? nodeXPositions.getOrDefault(node.getLeft(), paneWidth / 2) : paneWidth / 2;
        double rightX = node.getRight() != null ? nodeXPositions.getOrDefault(node.getRight(), paneWidth / 2) : paneWidth / 2;
        double avgX = (leftX + rightX) / 2;
        avgX = Math.max(nodeRadius, Math.min(paneWidth - nodeRadius, avgX));
        nodeXPositions.put(node, avgX);
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

    private void drawFinalTree(Node root, double y, double verticalSpacing) {
        if (root == null) {
            return;
        }

        double x = nodeXPositions.getOrDefault(root, paneWidth / 2);
        y = Math.max(nodeRadius, Math.min(paneHeight - nodeRadius, y));

        drawNode(root, x, y);

        double childY = y + verticalSpacing;

        if (root.getLeft() != null) {
            double leftX = nodeXPositions.getOrDefault(root.getLeft(), x);
            drawEdge(x, y, leftX, childY);
            drawFinalTree(root.getLeft(), childY, verticalSpacing);
        }
        if (root.getRight() != null) {
            double rightX = nodeXPositions.getOrDefault(root.getRight(), x);
            drawEdge(x, y, rightX, childY);
            drawFinalTree(root.getRight(), childY, verticalSpacing);
        }
    }
}