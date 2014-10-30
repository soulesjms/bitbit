/*
 * Entry class holds a single entry of a journal as well as the scriptures
 * and topics found within the content. 
 */
package scripturejournalapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static scripturejournalapp.FileServices.regexChecker;

/**
 *
 * @author adam
 */
public class Entry {
    //TODO: change content to a list so the lines will be preserved
    private String content;
    private String date;
    private String title;
    private List<Scripture> sList;
    private List<Topic> tList;
    
    public static void main(String[] args) {
        Entry e = new Entry();
        e.addScripture("2 Nephi 2:4");
    }
    public Entry() {
        this.sList = new ArrayList<>();
        this.tList = new ArrayList<>();
        this.content = "";
    }
   
    public Entry(String tent, String datee) {
        this();
        this.addContent(tent);
        this.date = datee;
    }

    public Entry(String datee) {
        this();
        this.date = datee;
    }

    
   @Override
   public String toString() {
       return this.date;
   }
    public List<Scripture> getScriptures() {
        return sList;
    }

    public List<Topic> getTopics() {
        return tList;
    }
       
     public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
    
    public void display() {
        //displays individual entry
        System.out.println("Entry: ");
        System.out.println("Date: " + this.date);
        System.out.println("Content:\n" + this.content);
        for (Scripture currScrip : this.sList) {
            System.out.println("\tScripture: " + currScrip);
        }
        for (Topic currTopic : this.tList) {
            System.out.println("\tTopic    : " + currTopic);
        }
    }
    //hard set for content -downside is it doesn't take
    //care of everything for you like addContent()
    public void setContent(String content) {
        this.content = content;
    }
    public void addContent(String freshContent) {
        this.content += freshContent;
        //new method of taking care of scriptures automagically
        for (String iList : Index.getIndex().getBooks()) {
            for (String bItem : regexChecker((iList + new Scripture().scriptureKey), freshContent)) {
                sList.add(new Scripture(bItem));
            }
        }
        //search freshContent for topics
        List<String> foundTopics = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : Index.getIndex().getTMap().entrySet()) {
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                foundTopics = regexChecker((value), freshContent);
                for (String bItem : foundTopics) {
                    tList.add(new Topic(Index.getIndex().getTopicTitle(bItem)));
                }
            }
        }
    }
    
    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addScripture(Scripture s) {
        this.sList.add(s);
    }
    public void addScripture(String s) {
        this.sList.add(new Scripture(s));
    }
    public void addTopic(Topic t) {
        this.tList.add(t);
    }
   public void addTopic(String t) {
        this.tList.add(new Topic(Index.getIndex().getTopicTitle(t)));
    }
}
