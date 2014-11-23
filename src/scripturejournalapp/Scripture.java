/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturejournalapp;

/**
 *
 * @author adam
 */
public class Scripture {

    //TODO: Prob wannna do some other way of handling

    private String book;
    private String chapter;
    private String startVerse;
    private String endVerse;
    final   String scriptureKey = ("(\\s+(chapter)?)?\\s+\\d+:?\\d*");
    final   String ldsPrefix = "https://www.lds.org/scriptures/search?lang=eng&type=verse&query=";

    public Scripture() {

    }

    public Scripture(String s) {
        setScripture(s);
    }

    @Override
    public String toString() {
        String scrip;
        scrip = this.book;
        if (!this.chapter.isEmpty()) {
            scrip += " " + this.chapter;
            if (!this.startVerse.isEmpty()) {
                scrip += ":" + this.startVerse;
                if (!this.endVerse.isEmpty()) {
                    scrip += "-" + this.endVerse;
                }
            }
        }
        return scrip;
    }

    public String toUrl() {
        String scrip = ldsPrefix;
        String bookChecked;
        if (this.book.contains(" ")) {
            bookChecked = this.book.replace(" ", "+");
        }
        else {
            bookChecked = this.book;
        }
        scrip += bookChecked;
        if (!this.chapter.isEmpty()) {
            scrip += "+" + this.chapter;
            if (!this.startVerse.isEmpty()) {
                scrip += ":" + this.startVerse;
                if (!this.endVerse.isEmpty()) {
                    scrip += "-" + this.endVerse;
                }
            }
        }
        return scrip;
    }
    
    //I love Splitting strings
    public Boolean setScripture(String s) {
        String bookName = "";
        String sVerseName = "";
        String eVerseName = "";
        String chapterName = "";
        String rest = "";
        //ommit the word chapter if it exists
        String two = "";
        Boolean looped = false;
        String one[] = s.split("\\s([c]|[C])hapter");
        for (String one1 : one) {
            two += one1;
            looped = true;
        }
        if (looped == true) {
            s = two;
        }

        if (s.split(" ").length == 3) {
            bookName = s.split(" ")[0] + " " + s.split(" ")[1];
            rest = s.split(" ")[2];
            chapterName = rest.split(":")[0];
            if (s.split(":").length == 2) {
                sVerseName = rest.split(":")[1];
            }
        } else if (s.split(" ").length == 2) {
            bookName = s.split(" ")[0];
            rest = s.split(" ")[1];
            chapterName = rest.split(":")[0];
            if (s.split(":").length == 2) {
                sVerseName = rest.split(":")[1];
            }
            if (sVerseName.split("-").length == 2) {
                eVerseName = sVerseName.split("-")[1];
                sVerseName = sVerseName.split("-")[0];
            }
        } else {
            System.err.println("ERROR: bad scripture string passed in");
            return false;
        }
        this.book = bookName;
        this.startVerse = sVerseName;
        this.endVerse = eVerseName;
        this.chapter = chapterName;
        return true;
    }

    public void setBook(String b) {
        //TODO more authorizing here
        book = b;
    }

    public String getBook() {
        //TODO more authorizing here
        return book;
    }

    public void setChapter(String c) {
        //TODO more authorizing here
        chapter = c;
    }

    public String getChapter() {
        //TODO more authorizing here
        return chapter;
    }

    public void setVerse(String v) {
        //TODO more authorizing here
        startVerse = v;
    }

    public String getVerse() {
        //TODO more authorizing here
        return startVerse;
    }

    public void setVerse(String sV, String eV) {
        //TODO more authorizing here
        startVerse = sV;
        endVerse = eV;
    }

    public void setStartVerse(String v) {
        //TODO more authorizing here
        startVerse = v;
    }

    public String getStartVerse() {
        //TODO more authorizing here
        return startVerse;
    }

    public void setEndVerse(String v) {
        //TODO more authorizing here
        endVerse = v;
    }

    public String getEndVerse() {
        //TODO more authorizing here
        return endVerse;
    }

    public static void main(String[] args) {
        Scripture s = new Scripture("Helaman chapter 3:4");
        System.out.println(s.getBook() + " " + s.getChapter() + ":" + s.getVerse());
        s = new Scripture("Alma 3:4");
        System.out.println(s.getBook() + " " + s.getChapter() + ":" + s.getVerse());
        s = new Scripture("2 Nephi 4:3");
        System.out.println(s.getBook() + " " + s.getChapter() + ":" + s.getVerse());
        s = new Scripture("2 Nephi chapter 4");
        System.out.println(s.getBook() + " " + s.getChapter() + ":" + s.getVerse());
        s = new Scripture("Alma chapter 4");
        System.out.println(s.getBook() + " " + s.getChapter() + ":" + s.getVerse());
        s = new Scripture("D&C 30:30-99");
        System.out.println(s.getBook() + " " + s.getChapter() + ":" + s.getVerse() + "-" + s.getEndVerse());
        System.out.println(s);
    }
}
