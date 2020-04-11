package autocomplete;

public class Term implements ITerm {

    /**
     * Data Fields
     */
    private String term;
    private long weight;


    /**
     * Constructor for a Term object
     *
     * @param query
     * @param weight
     */
    public Term(String query, long weight) {

        if (query == null || weight < 0) throw new IllegalArgumentException();
        this.term = query;
        this.weight = weight;

    }

    @Override
    public int compareTo(ITerm that) {
        return this.term.compareTo(((Term) that).getTerm());
    }

    /**
     * Setter for Term's term data field
     *
     * @param term String
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * Setter for Term's weight data field
     *
     * @param weight long
     */
    public void setWeight(long weight) {
        this.weight = weight;
    }

    /**
     * Getter for Term's term data field
     *
     * @return term String
     */
    public String getTerm() {
        return term;
    }

    /**
     * Getter for Term's weight data field
     *
     * @return weight long
     */
    public long getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return weight + "\t" + term;
    }
}
