/*
 *This idex contains a list of books and list of topics that you may
 *check against.   
 */
package scripturejournalapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adam
 */
//TODO: make Index constructor private, only allowing one at a time, same for all
public class Index {
    private static Index ind = new Index();
    private List<String> books;
    private List<String> topics;
    private Map<String, List<String>> tMap;
    
    private Index() {
        this("journal.properties");
    }
    
    //Real default constructor
    private Index(String propertiesFileName) {
        loadPropertiesToLists(propertiesFileName);
        this.tMap = mapinator(this.topics);
    }

    public static Index getIndex() {
        return ind;
    }
    
    public Boolean isBook(String checkMe) {
        for (String book1 : this.books) {
            if (checkMe.equals(book1)) {
                return true;
            }
        }
        return false;
    }

    //takes in line and searches map for it
    public String searchTopic(String line) {
        String found = "";
        for (Map.Entry<String, List<String>> entry : this.tMap.entrySet()) {
            String title = entry.getKey();
            List<String> list = entry.getValue();
            for (String i : list) {
                if (line.contains(i)) {
                    found = (title + ":" + i);
                }
            }
        }
        return found;
    }
    
    public void printMap(Map<String, List<String>> mp) {
    for (Map.Entry<String, List<String>> entry : mp.entrySet()) {
            String key = entry.getKey();
            System.out.print(key + " --> ");
            for (String value : entry.getValue()) {
                System.out.print(", \'" + value + "\'");
            }
            System.out.println();
        }
}
    
    public Boolean isTopic(String checkMe) {
        Boolean found = false;
        for (Map.Entry<String, List<String>> entry : tMap.entrySet()) {
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                if (checkMe.toLowerCase().equals(value.toLowerCase())) {
                    found = true;
                }
            }
        }
        return found;
    }
    
        public String getTopicTitle(String checkMe) {
        String title = "";
        for (Map.Entry<String, List<String>> entry : tMap.entrySet()) {
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                if (checkMe.toLowerCase().equals(value.toLowerCase())) {
                    title = key;
                }
            }
        }
        return title;
    }

    public Map<String, List<String>> mapinator(List<String> topicsList) {
        //treasure map (of topics)
        Map<String, List<String>> tMap = new HashMap<>();
        for (String tCurr : topicsList) {
            List<String> subTopics = new ArrayList<>();
            String[] s;
            if (tCurr.contains(";")) {
                s = (tCurr.split(":")[1]).split(";");
            } else {
                s = (tCurr.split(":")[1]).split(",");
            }
            for (String i : s) {
                subTopics.add(i);
            }
            tMap.put(tCurr.split(":")[0], subTopics);
        }
        return tMap;
    }

    public Map<String, List<String>> getTMap() {
        return this.tMap;
    }
    //Loads text file into List, line by line
    public List<String> loadList(String fileName) {
        List<String> list = new ArrayList<>();
        try {
            BufferedReader bi = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bi.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private void loadPropertiesToLists(String propFileName) {
        PropResources pr = new PropResources(propFileName);
        this.topics = loadList(pr.getTopicsFile());
        this.books = loadList(pr.getScripturesFile());
    }
    
    public List<String> getBooks() {
        return books;
    }

    public List<String> getTopics() {
        return topics;
    }
    
    public static void main(String[] args) {
        //Testing for this class:
        Index ind = new Index();
        
        String[] bookTest = {"Helaman", "Zeezrom"};
        for (String bookTest1 : bookTest) {
            if (ind.isBook(bookTest1)) {
                System.out.println(bookTest1 + " is indeed a book!");
            } else {
                System.out.println("Sorry, " + bookTest1 + " is not a book.");
            }
        }
        
        //Pieces of topics are good now
        String[] topicTest = {"Son of God", "rejoice", "Bananas are good", "is", "joy"};
        for (String iTest : topicTest) {
            if (ind.isTopic(iTest)) {
                System.out.println(iTest + " is indeed a topic! From title: "
                                   + ind.getTopicTitle(iTest));
            } else {
                System.out.println("Sorry, " + iTest + " is not a topic.");
            }
        }
        //ind.printMap(ind.tMap);
    }
}
