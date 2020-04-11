package indexing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class IndexBuilder implements IIndexBuilder{

    @Override
    public Map<String, List<String>> parseFeed(List<String> feeds) {

        Document doc;
        Elements elements;
        List<String> htmlLinks = new ArrayList<>();
        Map<String, List<String>> docs = new LinkedHashMap<>();

        // parsing RSS fedds
        for (String feed: feeds){
            try {
                doc = Jsoup.connect(feed).get();
                elements = doc.getElementsByTag("link");
                for (Element link: elements){
                    htmlLinks.add(link.text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // parsing HTML documents
        String[] words;
        List<String> wordsList;
        for (String html: htmlLinks){
            try {
                doc = Jsoup.connect(html).get();
                elements = doc.getElementsByTag("body");
                for (Element link: elements){
                    words = link.text().split(" ");
                    wordsList = new ArrayList<String>(Arrays.asList(words));
                    docs.put(html, wordsList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return docs;
    }

    @Override
    public Map<String, Map<String, Double>> buildIndex(Map<String, List<String>> docs) {
        return null;
    }

    @Override
    public Map<?, ?> buildInvertedIndex(Map<String, Map<String, Double>> index) {
        return null;
    }

    @Override
    public Collection<Map.Entry<String, List<String>>> buildHomePage(Map<?, ?> invertedIndex) {
        return null;
    }

    @Override
    public Collection<?> createAutocompleteFile(Collection<Map.Entry<String, List<String>>> homepage) {
        return null;
    }

    @Override
    public List<String> searchArticles(String queryTerm, Map<?, ?> invertedIndex) {
        return null;
    }
}
