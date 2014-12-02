/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturejournalapp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adam
 */
public class Journal {

    List<Entry> entryList;

    public Journal() {
        this.entryList = new ArrayList<>();  
    }

    public Journal(List<Entry> entryList) {
        this.entryList = entryList;
    }

    public List<Entry> getEntries() {
        return this.entryList;
    }
    
    public void add(Entry e) {
        entryList.add(e);
    }
    
    public void remove(int index) {
        entryList.remove(index);
    }
    
    public void remove(Entry e) {
        entryList.remove(e);
    }

    public void display() {
        System.out.println("Journal:\n");
        for (Entry currList : this.entryList) {
            currList.display();
        }
    }

    public List<Topic> getTopicsList() {
        List<Topic> masterTopicList = new ArrayList<>();
        for (Entry currList : this.entryList) {
            for (Topic tCurr : currList.getTopics())
            masterTopicList.add(tCurr);
        }
        return masterTopicList;
    }
    public static void main(String[] args) {
        Entry e = new Entry("Faith is a virtue. Ether chapter 12", "11-30-2014");
        e.addScripture("2 Nephi chapter 2:26");
        e.addTopic("joy");
        Journal j = new Journal();
        j.add(e);
        j.display();
    }
}
