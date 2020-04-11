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
                    wordsList = new ArrayList<String>();
                    for(String word: words){
                        wordsList.add(word.toLowerCase().replaceAll("[^a-z]", "").trim());
                    }
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

        int n = docs.size(); //number of documents
        Map<String, Integer> countDocWithWord = new HashMap<>(); //number of documents with term

        //create mapping of doc: (word:count)
        Map<String, Integer> countWordsPerDoc; //number of times a term appears in a doc
        Map<String, Map<String, Integer>> temp = new LinkedHashMap<>(); // doc : countWordsPerDoc
        for (String doc: docs.keySet()){
            List<String> words = docs.get(doc);
            countWordsPerDoc = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            for(String word: words){
                if (countWordsPerDoc.containsKey(word)){
                    countWordsPerDoc.put(word, countWordsPerDoc.get(word)+1);
                } else {
                    countWordsPerDoc.put(word, 1);
                    countDocWithWord.put(word, countDocWithWord.getOrDefault(word, 0) +1);
                }
            }
            temp.put(doc, countWordsPerDoc);
        }

        //create TF-IDF
        Map<String, Map<String, Double>> forwardIndex = new LinkedHashMap<>();
        Map<String, Double> TFIDF;
        for (String doc: temp.keySet()){
            TFIDF = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            double TF;
            double IDF;
            for (String word: docs.get(doc)){
               if (!TFIDF.containsKey(word)){
                   TF = (double)temp.get(doc).get(word) / (double) docs.get(doc).size();
                   IDF = Math.log( (double) n / (double) countDocWithWord.get(word)) ;
                   TFIDF.put(word, TF*IDF);
               }
            }
            forwardIndex.put(doc, TFIDF);
        }

        return forwardIndex;
    }

    @Override
    public Map<?, ?> buildInvertedIndex(Map<String, Map<String, Double>> index) {

        //  word  :  list of (doc, TFIDF)
        Map<String, List<Map.Entry<String, Double>>> invertedIndex = new HashMap<>();

        List<Map.Entry<String, Double>> temp;
        for (String doc: index.keySet()){
            for(String word: index.get(doc).keySet()){
                if (!invertedIndex.containsKey(word)){
                    temp = new ArrayList<>();
                    temp.add(new AbstractMap.SimpleEntry<String, Double>(doc, index.get(doc).get(word)));
                    invertedIndex.put(word, temp);
                } else {
                    temp = invertedIndex.get(word);
                    temp.add(new AbstractMap.SimpleEntry<String, Double>(doc, index.get(doc).get(word)));
                }
            }
        }

        // sort by reverse tag term TFIDF value
        for(String word: invertedIndex.keySet()){
            temp = invertedIndex.get(word);
            Collections.sort(temp, new Comparator<Map.Entry<String, Double>>() {
                @Override
                public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
        }

        return invertedIndex;
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
























