package test;

import static org.junit.Assert.*;
import indexing.IIndexBuilder;
import indexing.IndexBuilder;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

@SuppressWarnings("All")
public class TestIndexBuilder {

    List<String> feeds;
    IndexBuilder ib;
    Map<String, List<String>> docs;


    /**
     * setUp() method for the test cases
     */
    @Before
    public void setUp(){
        this.feeds = new ArrayList<>();
        this.feeds.add("http://localhost:8090/sample_rss_feed.xml");
        this.ib = new IndexBuilder();
    }

    /**
     * Test parseFeed() method in IndexBuilder
     */
    @Test
    public void testParseFeed(){
        Map<String, List<String>> docs = ib.parseFeed(feeds);
        assertEquals(5, docs.size());
        assertEquals(10, docs.get("http://localhost:8090/page1.html").size());
        assertEquals(55, docs.get("http://localhost:8090/page2.html").size());
        assertEquals(33, docs.get("http://localhost:8090/page3.html").size());
        assertEquals(22, docs.get("http://localhost:8090/page4.html").size());
        assertEquals(18, docs.get("http://localhost:8090/page5.html").size());
        this.docs = docs;
    }

    /**
     * Test buildeIndex() method in IndexBuilder
     */
    @Test
    public void testBuildIndex(){
        Map<String, List<String>> docs = ib.parseFeed(feeds);
        Map<String, Map<String, Double>> forwardIndex = ib.buildIndex(docs);
        String host = "http://localhost:8090/";

        assertEquals(0.1021, forwardIndex.get(host+"page1.html").get("data"), 0.001);
        assertEquals(0.183, forwardIndex.get(host+"page1.html").get("structures"), 0.001);
        assertEquals(0.091, forwardIndex.get(host+"page1.html").get("linkedlist"), 0.001);
        assertEquals(0.091, forwardIndex.get(host+"page1.html").get("stacks"), 0.001);
        assertEquals(0.0916, forwardIndex.get(host+"page1.html").get("lists"), 0.001);

        assertEquals(0.046, forwardIndex.get(host+"page2.html").get("data"), 0.001);
        assertEquals(0.0666, forwardIndex.get(host+"page2.html").get("structures"), 0.001);
        assertEquals(0.0585, forwardIndex.get(host+"page2.html").get("search"), 0.001);
        assertEquals(0.0499, forwardIndex.get(host+"page2.html").get("binary"), 0.001);
        assertEquals(0.0166, forwardIndex.get(host+"page2.html").get("queues"), 0.001);

        assertEquals(0.04877, forwardIndex.get(host+"page3.html").get("implement"), 0.001);
        assertEquals(0.0487, forwardIndex.get(host+"page3.html").get("java"), 0.001);
        assertEquals(0.0832, forwardIndex.get(host+"page3.html").get("trees"), 0.001);
        assertEquals(0.0487, forwardIndex.get(host+"page3.html").get("treeset"), 0.001);
        assertEquals(0.0277, forwardIndex.get(host+"page3.html").get("binary"), 0.001);

        assertEquals(0.0731, forwardIndex.get(host+"page4.html").get("mallarme"), 0.001);
        assertEquals(0.0731, forwardIndex.get(host+"page4.html").get("poem"), 0.001);
        assertEquals(0.1463, forwardIndex.get(host+"page4.html").get("do"), 0.001);
        assertEquals(0.0731, forwardIndex.get(host+"page4.html").get("think"), 0.001);
        assertEquals(0.0731, forwardIndex.get(host+"page4.html").get("others"), 0.001);

        assertEquals(0.08947, forwardIndex.get(host+"page5.html").get("categorization"), 0.001);
        assertEquals(0.08947, forwardIndex.get(host+"page5.html").get("random"), 0.001);
        assertEquals(0.08947, forwardIndex.get(host+"page5.html").get("topics"), 0.001);
        assertEquals(0.0509, forwardIndex.get(host+"page5.html").get("files"), 0.001);
        assertEquals(0.0894, forwardIndex.get(host+"page5.html").get("completely"), 0.001);
    }

    /**
     * Test buildInvertedIndex() method in IndexBuilder
     */
    @Test
    public void testBuildInvertedIndex(){
        Map<String, List<String>> docs = ib.parseFeed(feeds);
        Map<String, Map<String, Double>> forwardIndex = ib.buildIndex(docs);
        Map<?, ?> invertedIndex = ib.buildInvertedIndex(forwardIndex);

        assertEquals(0.1021,  ((List<Map.Entry<String, Double>>)invertedIndex.get("data")).get(0).getValue() , 0.001);
        assertEquals(0.0464,  ((List<Map.Entry<String, Double>>)invertedIndex.get("data")).get(1).getValue()  , 0.001);
        assertEquals(0.0154,  ((List<Map.Entry<String, Double>>)invertedIndex.get("data")).get(2).getValue()  , 0.001);
    }

    /**
     * Test buildHomePage() in IndexBuilder
     */
    @Test
    public void testBuildHomePage(){
        Map<String, List<String>> docs = ib.parseFeed(feeds);
        Map<String, Map<String, Double>> forwardIndex = ib.buildIndex(docs);
        Map<?, ?> invertedIndex = ib.buildInvertedIndex(forwardIndex);
        Collection<Map.Entry<String, List<String>>> homePage = ib.buildHomePage(invertedIndex);


        int prevSize = -1;
        String prevWord = null;
        for( Map.Entry<String, List<String>> e: homePage){

            System.out.println(e.getKey()+" "+e.getValue().size());

            // check word not a STOP work
            assertTrue(!IIndexBuilder.STOPWORDS.contains(e.getKey()));

            if (prevSize == -1) prevSize = e.getValue().size();
            else{
                // check sorted by number of articles
                assertTrue(prevSize >= e.getValue().size());

                if(prevSize == e.getValue().size()){
                    if (prevWord == null) prevWord = e.getKey();

                    //check sorted by reverse lexicographic order
                    assertTrue(prevWord.compareTo(e.getKey()) >= 0);
                    prevWord = e.getKey();
                }

                prevSize = e.getValue().size();
                prevWord = e.getKey();
            }
        }
    }

    /**
     * Test searchArciles() in IndexBuilder
     */
    @Test
    public void testSearchArticles(){
        Map<String, List<String>> docs = ib.parseFeed(feeds);
        Map<String, Map<String, Double>> forwardIndex = ib.buildIndex(docs);
        Map<?, ?> invertedIndex = ib.buildInvertedIndex(forwardIndex);
        List<String> articles = ib.searchArticles("data", invertedIndex);

        assertEquals(3, ib.searchArticles("data", invertedIndex).size() );
        assertEquals(2, ib.searchArticles("trees", invertedIndex).size() );
        assertEquals(2, ib.searchArticles("order", invertedIndex).size() );
        assertEquals(1, ib.searchArticles("working", invertedIndex).size() );
        assertEquals(1, ib.searchArticles("use", invertedIndex).size() );

    }

    /**
     * Test createAutocompleteFile() in IndexBuilder
     */
    @Test
    public void testCreateAutoCompleteFile(){
        Map<String, List<String>> docs = ib.parseFeed(feeds);
        Map<String, Map<String, Double>> forwardIndex = ib.buildIndex(docs);
        Map<?, ?> invertedIndex = ib.buildInvertedIndex(forwardIndex);
        Collection<Map.Entry<String, List<String>>> homePage = ib.buildHomePage(invertedIndex);
        Collection<?> wordsWritten = ib.createAutocompleteFile(homePage);

        assertEquals(wordsWritten.size(), homePage.size());
    }


}
