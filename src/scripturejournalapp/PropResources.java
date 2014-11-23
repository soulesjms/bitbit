/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturejournalapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author adam
 */
public class PropResources {
    //TODO:It may be nice to change the following 2 strings to 1 list in the future
    //TODO: OR a constructor that you pass a string of the property name that
    //you want.  If it is not there, it will throw an error.
    private String topicsFile;
    private String scripturesFile;
    private String propFileName;
    private String defaultDirectory;
    private String defaultJournalXML;
    private String defaultJournalTxt;
    
    private static PropResources pr = new PropResources();
        
    private PropResources() {
        this.propFileName = "journal.properties";
        getPropValues();
    }
    
    private PropResources(String pFileName) {
        this.propFileName = pFileName;
        getPropValues();
    }
    
    public static PropResources getPropResources() {
        return pr;
    }
    /* getPropValues
     * pretty much a giant setter called by the constructor
     * that grabs the config information from a properties file
     */
    private void getPropValues() {
        Properties prop = new Properties();
       
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        try {
            prop.load(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(PropResources.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (inputStream == null) {
            try {
                throw new FileNotFoundException("ERROR: property file '" + propFileName + "' not found in the classpath");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PropResources.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // get property values
        this.defaultDirectory = prop.getProperty("defaultDirectory");
        this.topicsFile = this.defaultDirectory + prop.getProperty("topics");
        this.scripturesFile = this.defaultDirectory + prop.getProperty("scriptures");
        this.defaultJournalXML = this.defaultDirectory + prop.getProperty("defaultJournalXML");
        this.defaultJournalTxt = this.defaultDirectory + prop.getProperty("defaultJournalTxt");
    }
    
    public String getTopicsFile() {
        return topicsFile;
    }

    public String getScripturesFile() {
        return scripturesFile;
    }
    
     public String getDirectory() {
        return defaultDirectory;
    }
    
    public static void main(String [] args) {
            PropResources pr = new PropResources();
            System.out.println("PROPERTIES: " + pr.getTopicsFile()
                                        + " \t\n" + pr.getScripturesFile()
                                        + " \t\n" + pr.getDirectory()
                                        + " \t\n" + pr.getDefaultJournalXML()
                                        + " \t\n" + pr.getDefaultJournalTxt());
    }

    String getDefaultJournalXML() {
        return this.defaultJournalXML;
    }
    
    String getDefaultJournalTxt() {
        return this.defaultJournalTxt;
    }
}