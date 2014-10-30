/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import scripturejournalapp.Entry;
import scripturejournalapp.Index;
import scripturejournalapp.Journal;
import scripturejournalapp.PropResources;
import scripturejournalapp.Scripture;
import scripturejournalapp.Topic;

/**
 *
 * @author adam
 */
public class ScriptureTester {

    //are scriptures fail safe?
    public ScriptureTester() {
        Scripture s = new Scripture("Helaman chapter 3:4");
        Assert.assertEquals(s.getBook(), "Helaman");
        Assert.assertEquals(s.getChapter(), "3");
        Assert.assertEquals(s.getVerse(), "4");
        s = new Scripture("Alma 12:9");
        Assert.assertEquals(s.getBook(), "Alma");
        Assert.assertEquals(s.getChapter(), "12");
        Assert.assertEquals(s.getVerse(), "9");
        s = new Scripture("2 Nephi 4");
        Assert.assertEquals(s.getBook(), "2 Nephi");
        Assert.assertEquals(s.getChapter(), "4");
        Assert.assertEquals(s.getVerse(), "");
        s = new Scripture("2 Nephi chapter 4");
        Assert.assertEquals(s.getBook(), "2 Nephi");
        Assert.assertEquals(s.getChapter(), "4");
        Assert.assertEquals(s.getVerse(), "");
        s = new Scripture("Alma chapter 5:53");
        Assert.assertEquals(s.getBook(), "Alma");
        Assert.assertEquals(s.getChapter(), "5");
        Assert.assertEquals(s.getVerse(), "53");
    }

    //test if content is set right
    public void entryContentTest() {
        Entry e = new Entry();
        e.setContent("This is a journal entry.");
        String content = e.getContent();
        Assert.assertEquals(content, "This is a journal entry.");
    }

    @Test
    public void entryDateTest() {
        Entry e = new Entry();
        e.setDate("11-30-2014");
        String date = e.getDate();
        Assert.assertEquals(date, "11-30-2014");
    }

    @Test
    public void entryTitleTest() {
        Entry e = new Entry();
        e.setTitle("This is a title in an entry.");
        String title = e.getTitle();
        Assert.assertEquals(title, "This is a title in an entry.");
    }

    @Test
    public void entryConstructorTest() {
        Entry e = new Entry("My scripture journal.", "11-30-2014");
        Assert.assertEquals(e.getContent(), "My scripture journal.");
        Assert.assertEquals(e.getDate(), "11-30-2014");
    }

    @Test
    public void topicConstructorTest() {
        Topic t = new Topic("joy");
        Assert.assertEquals(t.getSubTopic(), "joy");
    }

    @Test
    public void isTopicTest() {
        Topic t = new Topic("joy");
        Boolean r;
        r = true;
        Boolean test;
        test = t.isTopic(t.getTopic());
        Assert.assertEquals(test, r);
    }

    @Test
    public void isNotTopicTest() {
        Topic t = new Topic("rampage");
        Boolean r;
        r = false;
        Boolean test;
        test = t.isTopic("rampage");
        Assert.assertEquals(test, r);
    }

    @Test
    //check if scriptures are being properly put into journal
    public void journalScriptureTest() {
        Journal j = new Journal();
        Entry e = new Entry();
        e.addScripture("2 Nephi chapter 2:26");
        e.addScripture("2 Nephi Chapter 2:26");
        e.addScripture("2 Nephi 2:26");
        j.add(e);
        List<Entry> eList = j.getEntries();
        for (Entry eCurr : eList) {
            List<Scripture> sList = eCurr.getScriptures();
            for (Scripture sCurr : sList) {
                Assert.assertEquals(sCurr.getBook(), "2 Nephi");
                Assert.assertEquals(sCurr.getChapter(), "2");
                Assert.assertEquals(sCurr.getVerse(), "26");
            }
        }
    }

    @Test
    //Test if multiple entries can be created in journal
    public void journalEntryTest() {
        Journal j = new Journal();
        Entry e = new Entry("2014-10-30");
        e.addContent("The fall of man.");
        j.add(e);
        Entry e2 = new Entry("2014-11-30");
        e2.addContent("The birth of the Savior.");
        j.add(e2);
        Assert.assertEquals(j.getEntries().get(0).getContent(), "The fall of man.");
        Assert.assertEquals(j.getEntries().get(1).getContent(), "The birth of the Savior.");
    }

    @Test
    public void propertiesTest() {
        PropResources pr = new PropResources();
        Assert.assertEquals(pr.getScripturesFile(), "/home/adam/Downloads/books.txt");
        Assert.assertEquals(pr.getTopicsFile(), "/home/adam/Downloads/topics.txt");
    }

    @Test
    public void indexIsBookTest() {
        String[] test = {"Helaman", "Matthew", "Banana King", "is", "Isaiah"};
        Boolean[] boolIndx = {true, true, false, false, true};
        Assert.assertEquals(Index.getIndex().isBook(test[0]), boolIndx[0]);
        Assert.assertEquals(Index.getIndex().isBook(test[1]), boolIndx[1]);
        Assert.assertEquals(Index.getIndex().isBook(test[2]), boolIndx[2]);
        Assert.assertEquals(Index.getIndex().isBook(test[3]), boolIndx[3]);
        Assert.assertEquals(Index.getIndex().isBook(test[4]), boolIndx[4]);

    }

//Pieces of topics are the problem right now
    @Test
    public void indexIsTopicTest() {
        String[] topicTest = {"Son of God", "Rejoice", "Bananas are good", "is", "joy"};
        Boolean[] boolIndx = {true, true, false, false, true};
        Assert.assertEquals(Index.getIndex().isTopic(topicTest[0]), boolIndx[0]);
        Assert.assertEquals(Index.getIndex().isTopic(topicTest[1]), boolIndx[1]);
        Assert.assertEquals(Index.getIndex().isTopic(topicTest[2]), boolIndx[2]);
        Assert.assertEquals(Index.getIndex().isTopic(topicTest[3]), boolIndx[3]);
        Assert.assertEquals(Index.getIndex().isTopic(topicTest[4]), boolIndx[4]);

    }
}
