package autocomplete;

/**
 * ==== Attributes ====
 * - words: number of words
 * - term: the ITerm object
 * - prefixes: number of prefixes
 * - references: Array of references to next/children Nodes
 * <p>
 * ==== Constructor ====
 * Node(String word, long weight)
 *
 * @author Your_Name
 */
public class Node {
    /**
     * Data fields
     */
    private int words; // 1 if it is a word, 0 if it is not
    private Term term; //the word and its weight
    private int prefixes;
    private Node[] references;


    /**
     * Constructor for a Node object
     *
     * @param query  String
     * @param weight long
     */
    public Node(String query, long weight) {
        if (query == null || weight < 0) throw new IllegalArgumentException();

        this.words = 0;
        this.term = new Term(query, weight);
        this.prefixes = 0;
        this.references = new Node[26];


    }

    /**
     * Default Constructor for a Node object
     *
     */
    public Node() {
        this.words = 0;
        this.term = null;
        this.prefixes = 0;
        this.references = new Node[26];
    }

    /**
     * Getter for Node's prefixes data field
     *
     * @return this.prefixes int
     */
    public int getPrefixes() {
        return prefixes;
    }

    /**
     * Getter for Node's term data field
     *
     * @return this.term ITerm
     */
    public Term getTerm() {
        return term;
    }

    /**
     * Getter for Node's words data field
     *
     * @return this.words int
     */
    public int getWords() {
        return words;
    }

    /**
     * Getter for Node's references data field
     *
     * @return this.references Node[]
     */
    public Node[] getReferences() {
        return references;
    }

    /**
     * Method that checks if the Node is a leaf (no children)
     *
     * @return true if Node is a leaf, else false
     */
    public boolean isLeaf() {

        boolean leaf = true;
        for (int i = 0; i < 26; i++) {
            if (references[i] != null) {
                leaf = false;
            }
        }
        return leaf;
    }

    /**
     * Setter for Node's prefixes data field
     *
     * @param prefixes int
     */
    public void setPrefixes(int prefixes) {
        this.prefixes = prefixes;
    }

    /**
     * Setter for Node's term data field
     *
     * @param term ITerm
     */
    public void setTerm(Term term) {
        this.term = term;
    }

    /**
     * Setter for Node's term words field
     *
     * @param words int
     */
    public void setWords(int words) {
        this.words = words;
    }

    /**
     * Setter for Node's term references field
     *
     * @param references Node[]
     */
    public void setReferences(Node[] references) {
        this.references = references;
    }

    /**
     * Converts the Node object into String
     *
     * @return String
     */
    public String toString() {
        return term == null ? "" : term.toString();
    }


}
