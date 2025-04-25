package daa.huffmanca;

public class Node implements Comparable<Node> {
    private char character;
    private int frequency;
    private Node left;
    private Node right;

    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }
    public Node(int frequency, Node left, Node right) {
        this.character = '\0';
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public char getCharacter() {
        return character;
    }
    public int getFrequency() {
        return frequency;
    }
    public Node getLeft() {
        return left;
    }
    public Node getRight() {
        return right;
    }
    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public int compareTo(Node other) {
        return this.frequency - other.frequency;
    }
}