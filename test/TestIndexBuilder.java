package test;

import static org.junit.Assert.*;

import indexing.IndexBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TestIndexBuilder {

    List<String> feeds;
    IndexBuilder ib;

    @Before
    public void setUp(){
        feeds = new ArrayList<>();
        feeds.add("http://localhost:8090/sample_rss_feed.xml");
        ib = new IndexBuilder();
    }

    @Test
    public void testParseFeed(){
        Map<String, List<String>> docs = ib.parseFeed(feeds);
        assertEquals(5, docs.size());
        assertEquals(10, docs.get("http://localhost:8090/page1.html").size());
        assertEquals(55, docs.get("http://localhost:8090/page2.html").size());
        assertEquals(33, docs.get("http://localhost:8090/page3.html").size());
        assertEquals(22, docs.get("http://localhost:8090/page4.html").size());
        assertEquals(18, docs.get("http://localhost:8090/page5.html").size());

    }

}
