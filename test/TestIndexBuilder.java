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
        System.out.println(docs);
    }

}
