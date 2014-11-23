/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturejournalapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//List
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
//Document
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author adam
 */
public class FileServices implements Runnable {
    
    static final String workDir = PropResources.getPropResources().getDirectory();
    @Override
    public void run() {

    }

    public static void main(String[] args) {
        String txtFileIn = workDir + "src/journ.txt";
        String xmlFileIn = workDir + "src/yay.xml";
        String outTxtFile = workDir + "src/out.txt";
        String outXmlFile = workDir + "src/out.xml";
        FileServices mS4 = new FileServices();
        Journal j = new Journal();
        j = mS4.txtToJournal(txtFileIn);
        try {
            mS4.saveDocument(mS4.buildXmlDocument(j), xmlFileIn);
        } catch (Exception ex) {
            Logger.getLogger(FileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            mS4.displayXml(mS4.buildXmlDocument(j));
        } catch (Exception ex) {
            Logger.getLogger(FileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        mS4.saveTxt(j, outTxtFile);
        mS4.saveXml(j, outXmlFile);
        mS4.xmlToJournal(xmlFileIn);
    }

    public void saveXml(Journal jo, String outFile) {
        try {
            try {
                new FileServices().saveDocument(new FileServices().buildXmlDocument(jo), outFile);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(FileServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (TransformerException ex) {
            Logger.getLogger(FileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //hydrates Journal with text file journal
    public Journal txtToJournal(String file) {
        Journal j = new Journal();
        String dateKey = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern datePat = Pattern.compile(dateKey);
        String scriptureKey = ("(\\s+(chapter)?)?\\s+\\d+:?\\d*");

        String allKeys = (datePat + "|" + "-----");
        Pattern allPat = Pattern.compile(allKeys);
        Entry e = new Entry();
        try {
            BufferedReader bi = new BufferedReader(new FileReader(file));
            Integer contentTime = 0;//If it is 2 or greater, it's time for content
            String line;
            while ((line = bi.readLine()) != null) {
                if ("-----".equals(line)) {//if new entry found
                    //adding entry to journal
                    contentTime = 0;
                    if (j.entryList.size() > 0) {
                        e = new Entry();
                    }
                    j.add(e);
                }
                Matcher anyMatcher = allPat.matcher(line);
                while (anyMatcher.find()) {
                    //needles in the haystack:
                    if (line.matches(dateKey)) {
                        //Setting date
                        e.setDate(anyMatcher.group());
                    }
                }
                if (contentTime > 1) {
                    //adding line to content
                    e.addContent(line + "\n");
                }
                contentTime++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return j;
    }

    public Journal xmlToJournal(String inFile) {
        Journal j = new Journal();
        Entry e = new Entry();
        try {
            File fXmlFile = new File(inFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("entry");
            //loop through entries
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //new entry
                    String date = eElement.getAttribute("date");
                    e = new Entry();
                    e.setDate(date);
                    //add content
                    Element cElement = (Element) eElement.getElementsByTagName("content").item(0);
                    String content = cElement.getTextContent();
                    e.addContent(content);
                }
                j.add(e);
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("ERROR of parser or SAX or IOException");
        }
        return j;
    }

    public Document buildXmlDocument(Journal j) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();
        //get entries from j
        List<Entry> entries = j.getEntries();
        Element element = doc.createElement("journal");
        doc.appendChild(element);
        //loop for entry list reading
        for (Entry eCurr : entries) {
            Element entryElement = doc.createElement("entry");
            element.appendChild(entryElement);
            entryElement.setAttribute("date", eCurr.getDate());
            //Loop through scriptures list of entry
            for (Scripture sCurr : eCurr.getScriptures()) {
                Element scriptureElement = doc.createElement("scripture");
                entryElement.appendChild(scriptureElement);
                scriptureElement.setAttribute("book", sCurr.getBook());
                scriptureElement.setAttribute("chapter", sCurr.getChapter());
                scriptureElement.setAttribute("startverse", sCurr.getVerse());
            }
            //TODO: put topics here:
            for (Topic tCurr : eCurr.getTopics()) {
                Element topicElement = doc.createElement("topic");
                entryElement.appendChild(topicElement);
                topicElement.setTextContent(tCurr.getTopic());
            }
            Element contentElement = doc.createElement("content");
            entryElement.appendChild(contentElement);
            contentElement.setTextContent(eCurr.getContent());
        }
        return doc;
    }

    //Print document to screen
    public static final void displayXml(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println(out.toString());
    }

//Save document to xml file
    public static void saveDocument(Document myDocument, String outFile) throws TransformerException {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();

            Result output = new StreamResult(new File(outFile));
            Source input = new DOMSource(myDocument);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(input, output);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(FileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveTxt(Journal j, String outFileName) {
        try {
            List<Entry> entries = j.getEntries();
            String content;
            String date;
            File file = new File(outFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            //Loop for entry list reading
            for (Entry eCurr : entries) {
                date = eCurr.getDate();
                content = eCurr.getContent();
                bw.write("-----\n" + date + "\n" + content + "\n");
                //Loop through scriptures list of entry
                for (Scripture sCurr : eCurr.getScriptures()) {
                    //content = "This is the content to write into file";
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Kyle Eslick helped me out a lot with this method.
    public static List<String> regexChecker(String key, String checkMe) {
        List<String> foundList = new ArrayList<>();
        Pattern pat = Pattern.compile(key);
        Matcher matcher = pat.matcher(checkMe);
        while (matcher.find()) {
            foundList.add(matcher.group());
        }
        return foundList;
    }
}