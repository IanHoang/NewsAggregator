package autocomplete;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Long.parseLong;

public class Autocomplete implements IAutocomplete {

    /**
     * Data fields
     */
    private Node root;
    private int k;


    @Override
    public void addWord(String word, long weight) {
        if (word.matches("^.*[^a-zA-Z0-9 ].*$")) return;
        if (root == null) root = new Node();
        root.setPrefixes(root.getPrefixes() + 1);

        addWord(word, word, weight, root);
    }

    /**
     * Recursively adds the letters of the word
     *
     * @param word    String the original word
     * @param letters String letters left to process
     * @param weight  long the weight of the original word
     * @param curr    Node
     * @return
     */
    public Node addWord(String word, String letters, long weight, Node curr) {

        char c = Character.toLowerCase(letters.charAt(0));
        if (c < 'a' || c > 'z') return null;
        int index = c - 'a';
        Node next = curr.getReferences()[index];

        //base case: only 1 letter left
        if (letters.length() == 1) {
            if (next == null) {
                curr.getReferences()[index] = new Node(word, weight);
                next = curr.getReferences()[index];
                next.setPrefixes(1);
                next.setWords(1);
            } else {
                next.setPrefixes(next.getPrefixes() + 1);
                next.setWords(1);
                next.setTerm(new Term(word, weight));
            }
            return next;

            // recursively add letters of the word
        } else {
            if (next == null) {
                curr.getReferences()[index] = new Node();
                next = curr.getReferences()[index];
                next.setPrefixes(1); // added
            } else {
                next.setPrefixes(next.getPrefixes() + 1);
            }
            return addWord(word, letters.substring(1), weight, next);
        }
    }

    @Override
    public Node buildTrie(String filename, int k) {

        this.root = new Node();
        this.k = k;

        try {
            FileReader file = new FileReader(filename);
            BufferedReader reader = new BufferedReader(file);

            int size = Integer.parseInt(reader.readLine());

            String[] read;
            long weight;
            String word;
            for (int i = 0; i < size; i++) {
                read = reader.readLine().trim().split(" ");
                if (read.length <2) continue;
                weight = parseLong(read[0]);
                word = read[1].trim();
                addWord(word, weight);
            }
        } catch (IOException e) {
            System.out.println("IO Exception!");
            return null;
        }

        return this.root;
    }

    @Override
    public int numberSuggestions() {
        return k;
    }

    @Override
    public Node getSubTrie(String prefix) {
        Node curr = root;
        char c;
        int index;

        while (prefix.length() != 0) {
            c = Character.toLowerCase(prefix.charAt(0));
            if (c < 'a' || c > 'z') return null;
            index = c - 'a';
            curr = curr.getReferences()[index];
            if (curr == null) return null;
            prefix = prefix.substring(1);
        }

        return curr;
    }

    @Override
    public int countPrefixes(String prefix) {
        Node node = getSubTrie(prefix);
        return node == null ? 0 : node.getPrefixes();
    }

    @Override
    public List<ITerm> getSuggestions(String prefix) {

        List<ITerm> list = new ArrayList<>();
        Node node = getSubTrie(prefix);
        if (node == null) return list;
        getSuggestions(node, list);

        return list;
    }

    /**
     * Recursively find ITerm objects with query starting from Nodenode
     *
     * @param node Node
     * @param list List<ITerm> where the list of ITerm objects is stored
     */
    public void getSuggestions(Node node, List<ITerm> list) {

        //base case: node is a leaf
        if (node.isLeaf()) {
            list.add(node.getTerm());
        } else {
            if (node.getWords() == 1) list.add(node.getTerm());
            Node next;
            for (int i = 0; i < 26; i++) {
                next = node.getReferences()[i];
                if (next == null) continue;
                else getSuggestions(next, list);
            }
        }
    }

    /**
     * Getter for Autocomplete's root field
     *
     * @return root Node
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Setter for Autocomplete's root data field
     *
     * @param root Node
     */
    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * Setter for Autocomplete's k data field
     *
     * @param k int
     */
    public void setK(int k) {
        this.k = k;
    }
}
