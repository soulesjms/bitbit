/*
 * Scripture Journal Created By Adam Quinton
 */
package scripturejournalapp;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Platform.exit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

/**
 * CONTRIBUTORS: Tyler Scott, Eric Eslick
 *
 * @author Adam Quinton
 */
public class ScriptureJournalApp extends Application {

    String journalTxt;
    String outputXml;
    String outputTxt;

    private Journal j;
    private TextArea entryField;
    private Label dateLbl;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ScriptureJournalApp sja = new ScriptureJournalApp();
        if (args.length < 3) {
            System.out.println("In parameters you may specify: Input text, Output xml , Output text files");
        } else {
            sja.setJournalTxt(args[0]);
            sja.setOutputXml(args[1]);
            sja.setOutputTxt(args[2]);
            try {
                FileServices mS4 = new FileServices();
                System.out.println("Importing journal file: " + sja.getJournalTxt());
                Journal j = mS4.txtToJournal(sja.getJournalTxt());
                System.out.println("Exporting xml document: " + sja.getOutputXml());
                mS4.saveDocument(mS4.buildXmlDocument(j), sja.getOutputXml());
                System.out.println("Exporting txt document: " + sja.getOutputTxt());
                mS4.saveTxt(j, sja.getOutputTxt());
            } catch (Exception ex) {
                Logger.getLogger(FileServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Launch javafx GUI!
        launch(args);
    }

    public Journal loadTxt(String inFile) {
        System.out.println("Importing journal file txt: " + inFile);
        Journal jo = new FileServices().txtToJournal(inFile);
        return jo;
    }

    public Journal loadXml(String inFile) {
        System.out.println("Importing journal file from xml: " + inFile);
        Journal jo = new FileServices().xmlToJournal(inFile);
        return jo;
    }

    public void saveTxt(Journal jo, String outFile) {
        System.out.println("Exporting journal file to txt: " + outFile);
        new FileServices().saveTxt(jo, outFile);
    }

    public void saveXml(Journal jo, String outFile) {
        System.out.println("Exporting journal file to txt: " + outFile);
        new FileServices().saveXml(jo, outFile);
    }

    @Override
    public void start(final Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        j = new Journal();

        final ListView<Entry> listView = new ListView<>();
        final ObservableList<Entry> entriesOList = FXCollections.observableArrayList();
        listView.setItems(entriesOList);
        listView.setPrefWidth(175);
        listView.setPrefHeight(255);

        final ListView<Scripture> sListView = new ListView<>();
        final ObservableList<Scripture> scripturesOList = FXCollections.observableArrayList();
        sListView.setItems(scripturesOList);
        sListView.setPrefWidth(175);
        sListView.setPrefHeight(100);
        final Label scriptureLbl = new Label();
        scriptureLbl.textProperty().setValue("Scriptures");

        final ListView<Topic> tListView = new ListView<>();
        final ObservableList<Topic> topicsOList = FXCollections.observableArrayList();
        tListView.setItems(topicsOList);
        tListView.setPrefWidth(175);
        tListView.setPrefHeight(100);
        final Label topicLbl = new Label();
        topicLbl.textProperty().setValue("Topics");
        
        final Label progBar1Lbl = new Label();
        progBar1Lbl.textProperty().setValue("Loading...");
        //TODO: SET these inside their own functions
        entryField = new TextArea("");
        entryField.setWrapText(true);
        entryField.setTooltip(new Tooltip("Context of Entry"));

        //j = loadTxt("/home/adam/Downloads/journ.txt");
        j = loadXml("/home/adam/Desktop/yay.xml");
        for (Entry eCurr : j.getEntries()) {
            entriesOList.add(eCurr);
        }

        dateLbl = new Label("Date here");
        String currDate = j.getEntries().get(0).getDate();
        dateLbl.textProperty().setValue(currDate);
        dateLbl.setTooltip(new Tooltip("Current entry"));

        Button addEntryBtn = new Button();
        addEntryBtn.setText("Add entry");
        Button openFromListBtn = new Button();
        openFromListBtn.setText("Open");
        openFromListBtn.setTooltip(new Tooltip("Open currently selected entry from list"));
        
        //default load the first entry, content:
        entryField.setText(j.getEntries().get(0).getContent());
        //scriptures list load
        for (Scripture sCurr : j.getEntries().get(0).getScriptures()) {
            scripturesOList.add(sCurr);
        }
        //Topics list load
        for (Topic tCurr : j.getEntries().get(0).getTopics()) {
            topicsOList.add(tCurr);
        }

        BorderPane root = new BorderPane();
        MenuBar mainMenu = new MenuBar();
        ToolBar toolBar = new ToolBar();
        VBox topContainer = new VBox();

        //Create SubMenu File.
        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Open File");
        openFile.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        MenuItem saveAs = new MenuItem("Save as...");
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        MenuItem exitApp = new MenuItem("Exit");
        exitApp.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        file.getItems().addAll(openFile, saveAs, exitApp);

        //Create SubMenu Edit.
        Menu edit = new Menu("Edit");
        MenuItem clearAll = new MenuItem("New entry");
        edit.getItems().add(clearAll);
        
        //Create SubMenu Topics and add them all in.
        Menu topicView = new Menu("All Topics");
        for (Entry eCurr : j.getEntries()) {
            for (Topic tCurr : eCurr.getTopics()) {
                final Entry eEach = eCurr;
                MenuItem topicName = new MenuItem(eCurr.getDate()
                        + " " + tCurr.toString());
                topicView.getItems().add(topicName);
                topicName.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        entryField.setText(eEach.getContent());
                        String currDate = eEach.getDate();
                        dateLbl.textProperty().setValue(currDate);
                        scripturesOList.clear();
                        for (Scripture sCurr : eEach.getScriptures()) {
                            scripturesOList.add(sCurr);
                        }
                        topicsOList.clear();
                        for (Topic tCurr : eEach.getTopics()) {
                            topicsOList.add(tCurr);
                        }
                    }
                });
            }
        }
                //Create SubMenu Scriptures and add them all in.
        Menu scriptureView = new Menu("All Scriptures");
        for (Entry eCurr : j.getEntries()) {
            for (Scripture sCurr : eCurr.getScriptures()) {
                MenuItem scriptureName = new MenuItem(eCurr.getDate()
                        + ":  " + sCurr.toString());
                scriptureView.getItems().add(scriptureName);
                final Entry eEach = eCurr;

                scriptureName.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        entryField.setText(eEach.getContent());
                        String currDate = eEach.getDate();
                        dateLbl.textProperty().setValue(currDate);
                        scripturesOList.clear();
                        for (Scripture sCurr : eEach.getScriptures()) {
                            scripturesOList.add(sCurr);
                        }
                        topicsOList.clear();
                        for (Topic tCurr : eEach.getTopics()) {
                            topicsOList.add(tCurr);
                        }
                    }
                });
            }
        }
        //Create SubMenu Help.
        Menu help = new Menu("Help");
        MenuItem visitWebsite = new MenuItem("Visit Website");
        help.getItems().add(visitWebsite);
        
        mainMenu.getMenus().addAll(file, edit, topicView, scriptureView, help);

                
        Button searchLDSBtn = new Button();
        searchLDSBtn.setText("Read");
        searchLDSBtn.setTooltip(new Tooltip("Look up on lds.org"));
        
        addEntryBtn.setTooltip(new Tooltip("Add text to new entry"));
        addEntryBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Adding an entry...");
                Entry eTemp = new Entry();
                eTemp.addContent(entryField.getText());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                eTemp.setDate(dateFormat.format(date));
                entriesOList.add(eTemp);
                j.add(eTemp);
                scripturesOList.clear();
                for (Scripture sCurr : eTemp.getScriptures()) {
                    scripturesOList.add(sCurr);
                }
            }
        });
        clearAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                entryField.clear();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                dateLbl.textProperty().setValue(dateFormat.format(date));
                scripturesOList.clear();
                topicsOList.clear();
            }
        });
        openFromListBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Opening");
                Entry selectedItem = listView.getSelectionModel().getSelectedItem();
                System.out.println(selectedItem.getDate());
                entryField.clear();
                entryField.setText(selectedItem.getContent());
                String currDate = selectedItem.getDate();
                dateLbl.textProperty().setValue(currDate);
                scripturesOList.clear();
                for (Scripture sCurr : selectedItem.getScriptures()) {
                    scripturesOList.add(sCurr);
                }
                topicsOList.clear();
                for (Topic tCurr : selectedItem.getTopics()) {
                    topicsOList.add(tCurr);
                }
            }
        });
        openFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Loading journal file...");
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Journals", "*.txt", "*.xml"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
                File file = chooser.showOpenDialog(primaryStage);
                String fileName = file.getPath();
                if (fileName.toLowerCase().contains(".txt")) {
                    j = loadTxt(fileName);
                } else if (fileName.toLowerCase().contains(".xml")) {
                    j = loadXml(fileName);
                } else {
                    System.err.println("Tine maproblems ne File Loading");
                }
                entriesOList.clear();
                entryField.clear();
                for (Entry eCurr : j.getEntries()) {
                    entriesOList.add(eCurr);
                }
                //display first entry
                entryField.setText(j.getEntries().get(0).getContent());
                String iDate = j.getEntries().get(0).getDate();
                dateLbl.textProperty().setValue(iDate);
                scripturesOList.clear();
                for (Scripture sCurr : j.getEntries().get(0).getScriptures()) {
                    scripturesOList.add(sCurr);
                }
                topicsOList.clear();
                for (Topic tCurr : j.getEntries().get(0).getTopics()) {
                    topicsOList.add(tCurr);
                }
            }
        });

        saveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser1 = new FileChooser();
                fileChooser1.setTitle("Save Journal");
                File file = fileChooser1.showSaveDialog(primaryStage);
                if (file != null) {
                    String fileName = file.getPath();
                    if (fileName.toLowerCase().contains(".txt")) {
                        saveTxt(j, fileName);
                    } else if (fileName.toLowerCase().contains(".xml")) {
                        saveXml(j, fileName);
                    } else {
                        System.out.println("Not good to save to that type of file");
                    }
                } else {
                    System.out.println("ERROR: save path is empty");
                }
            }
        });
        
        exitApp.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               System.exit(0);
            }
        });

        visitWebsite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VisitWebsite vW = new VisitWebsite("http://www.github.com/zvakanaka");
                Thread t = new Thread(vW);
                t.start();//does vW.run(); in a thread
            }
        });
        //Looks up scripture on lds.org
        searchLDSBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String search;
                Scripture selectedItem = sListView.getSelectionModel().getSelectedItem();
                search = selectedItem.toUrl();
                VisitWebsite vW = new VisitWebsite(search);
                Thread t = new Thread(vW);
                t.start();//does vW.run(); in a thread
            }
        });
        
        Barbar barThread1 = new Barbar(500, 10);
        ProgressBar progressBar1 = new ProgressBar();
        progressBar1.progressProperty().bind(barThread1.processProperty);
        Thread t1 = new Thread(barThread1);
        t1.start();
        if (barThread1.processProperty.equals((double)1)) {
        progBar1Lbl.textProperty().setValue("DONE");
        }
        Scene scene = new Scene(root, 700, 470);
        //Add the ToolBar and Main Menu to the VBox
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolBar);
        root.setTop(grid);

        //col, row, coltakeup, rowtakeup
        grid.add(mainMenu,        0,  0, 4, 1);
        grid.add(dateLbl,         0,  1, 2, 1);
        grid.add(entryField,      0,  3, 3, 1);
        grid.add(listView,        3,  3, 2, 1);
        grid.add(addEntryBtn,     0,  7, 1, 1);
        grid.add(openFromListBtn, 4,  7, 1, 1);
        grid.add(scriptureLbl,    0,  9, 2, 1);
        grid.add(topicLbl,        1,  9, 1, 1);
        grid.add(sListView,       0, 10, 1, 1);
        grid.add(tListView,       1, 10, 1, 1);
        grid.add(progBar1Lbl,     2,  9, 2, 1);
        grid.add(progressBar1,    2, 10, 1, 1);
        grid.add(searchLDSBtn,       0, 12, 1, 1);
        
        primaryStage.setTitle("Scripture Journal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //a sort of destructor
    @Override
    public void stop() {
        System.out.println("Write again tomorrow!");
    }

    public String getJournalTxt() {
        return journalTxt;
    }

    public String getOutputXml() {
        return outputXml;
    }

    public String getOutputTxt() {
        return outputTxt;
    }

    public void setJournalTxt(String journalTxt) {
        this.journalTxt = journalTxt;
    }

    public void setOutputXml(String outputXml) {
        this.outputXml = outputXml;
    }

    public void setOutputTxt(String outputTxt) {
        this.outputTxt = outputTxt;
    }
}
