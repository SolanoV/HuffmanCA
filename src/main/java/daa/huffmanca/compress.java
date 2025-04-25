package daa.huffmanca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class compress {
    private String input;
    private String output;
    private ArrayList<characters> character;
    private Node root;

    public compress() {
        this.input = "";
        this.output = "";
        this.character = new ArrayList<>();
        this.root = null;
        System.out.println("compress: Constructor called, root = null");
    }

    public compress(String input, String output) {
        this.input = input;
        this.output = output;
        this.character = new ArrayList<>();
        this.root = null;
        System.out.println("compress: Constructor with input/output called, root = null");
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
        System.out.println("setInput: Input set to \"" + input + "\"");
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
        System.out.println("setOutput: Output set to \"" + output + "\"");
    }

    public ArrayList<characters> getCharacterList() {
        System.out.println("getCharacterList: Returning list with size = " + character.size());
        return character;
    }

    public Node getRoot() {
        System.out.println("getRoot: Returning root = " + (root != null ? "not null" : "null"));
        return root;
    }

    public void compressInput(String input) {
        System.out.println("compressInput: Input = \"" + input + "\"");
        if (input == null || input.trim().isEmpty()) {
            System.out.println("compressInput: Input is null or empty, resetting state");
            this.root = null;
            this.output = "";
            this.character = new ArrayList<>();
            return;
        }

        try {
            setInput(input);
            System.out.println("compressInput: Input set");

            Map<Character, Integer> frequencyMap = new HashMap<>();
            for (char c : input.toCharArray()) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
            System.out.println("compressInput: Frequency map = " + frequencyMap);

            if (frequencyMap.size() == 1) {
                System.out.println("compressInput: Single character input detected");
                Map.Entry<Character, Integer> entry = frequencyMap.entrySet().iterator().next();
                character = new ArrayList<>();
                characters c = new characters();
                c.setCharacter(entry.getKey());
                c.setFrequency(entry.getValue());
                c.setCode("0");
                character.add(c);
                this.root = new Node(entry.getKey(), entry.getValue());
                this.output = "0".repeat(entry.getValue());
                System.out.println("compressInput: Single char - root = not null, output = " + this.output);
                return;
            }

            character = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                characters c = new characters();
                c.setCharacter(entry.getKey());
                c.setFrequency(entry.getValue());
                c.setCode("");
                character.add(c);
            }
            System.out.println("compressInput: Character list size = " + character.size());

            System.out.println("compressInput: Building Huffman tree");
            root = buildHuffmanTree(frequencyMap);
            System.out.println("compressInput: Root node = " + (root != null ? "not null" : "null"));

            if (root == null) {
                System.out.println("compressInput: Failed to build Huffman tree, resetting output");
                this.output = "";
                return;
            }

            Map<Character, String> huffmanCodes = new HashMap<>();
            generateCodes(root, "", huffmanCodes);
            System.out.println("compressInput: Huffman codes = " + huffmanCodes);

            for (characters c : character) {
                c.setCode(huffmanCodes.get(c.getCharacter()));
            }

            StringBuilder compressed = new StringBuilder();
            for (char c : input.toCharArray()) {
                String code = huffmanCodes.get(c);
                if (code == null) {
                    System.out.println("compressInput: No code for character " + c);
                    this.output = "";
                    return;
                }
                compressed.append(code);
            }
            System.out.println("compressInput: Compressed output = " + compressed);

            setOutput(compressed.toString());
            System.out.println("compressInput: Output set");
        } catch (Exception e) {
            System.err.println("compressInput: Exception occurred: " + e.getMessage());
            e.printStackTrace();
            this.root = null;
            this.output = "";
            this.character = new ArrayList<>();
        }
    }

    private Node buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        System.out.println("buildHuffmanTree: Frequency map size = " + frequencyMap.size());
        if (frequencyMap.isEmpty()) {
            System.out.println("buildHuffmanTree: Frequency map is empty, returning null");
            return null;
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            Node node = new Node(entry.getKey(), entry.getValue());
            pq.add(node);
            System.out.println("buildHuffmanTree: Added node - Char: " + entry.getKey() + ", Freq: " + entry.getValue());
        }

        if (pq.isEmpty()) {
            System.out.println("buildHuffmanTree: Priority queue is empty, returning null");
            return null;
        }

        int step = 1;
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            if (left == null || right == null) {
                System.out.println("buildHuffmanTree: Null node encountered at step " + step + ", returning null");
                return null;
            }
            System.out.println("buildHuffmanTree: Step " + step + " - Combining nodes - Left Freq: " + left.getFrequency() +
                    ", Right Freq: " + right.getFrequency());
            Node parent = new Node(left.getFrequency() + right.getFrequency(), left, right);
            pq.add(parent);
            System.out.println("buildHuffmanTree: Step " + step + " - Added parent node with Freq: " + parent.getFrequency());
            step++;
        }

        Node result = pq.poll();
        System.out.println("buildHuffmanTree: Final root = " + (result != null ? "not null" : "null"));
        return result;
    }

    private void generateCodes(Node node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) {
            System.out.println("generateCodes: Node is null, returning");
            return;
        }

        if (node.isLeaf()) {
            String assignedCode = code.isEmpty() ? "0" : code;
            huffmanCodes.put(node.getCharacter(), assignedCode);
            System.out.println("generateCodes: Assigned code - Char: " + node.getCharacter() + ", Code: " + assignedCode);
        }

        generateCodes(node.getLeft(), code + "0", huffmanCodes);
        generateCodes(node.getRight(), code + "1", huffmanCodes);
    }

    public void updateFrequencyTable() {
        // Handled by controller
    }

    public int getOriginalSize() {
        int size = input == null ? 0 : input.length() * 8;
        System.out.println("getOriginalSize: Returning " + size + " bits");
        return size;
    }

    public int getCompressedSize() {
        int size = output == null ? 0 : output.length();
        System.out.println("getCompressedSize: Returning " + size + " bits");
        return size;
    }

    public double getCompressionPercentage() {
        int originalSize = getOriginalSize();
        int compressedSize = getCompressedSize();
        if (originalSize == 0) {
            System.out.println("getCompressionPercentage: Original size is 0, returning 0.0");
            return 0.0;
        }
        double percentage = (1.0 - (double) compressedSize / originalSize) * 100;
        System.out.println("getCompressionPercentage: Returning " + String.format("%.2f%%", percentage));
        return percentage;
    }
}