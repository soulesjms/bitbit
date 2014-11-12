/*
 * Scripture Journal Created By Adam Quinton
 */
package scripturejournalapp;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
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
    static String outputTxt;
    //Default input file, xml or txt
    static String inFile;

    private Journal j;
    private TextArea entryField;
    private Label dateLbl;
//Lists
    final ListView<Entry> listView = new ListView<>();
    final ObservableList<Entry> entriesOList = FXCollections.observableArrayList();
    final ListView<Scripture> sListView = new ListView<>();
    final ObservableList<Scripture> scripturesOList = FXCollections.observableArrayList();
    final ListView<Topic> tListView = new ListView<>();
    final ObservableList<Topic> topicsOList = FXCollections.observableArrayList();
//Labels
    final Label progBar1Lbl = new Label();
    final Label scriptureLbl = new Label();
    final Label topicLbl = new Label();
    final Label wordCountLbl = new Label();
//Buttons
    Button addEntryBtn = new Button();
    Button openFromListBtn = new Button();
    Button searchLDSBtn = new Button();
    Button wordCountBtn = new Button();
//Menus
    Menu file = new Menu("File");
    MenuItem openFile = new MenuItem("Open File");
    MenuItem saveAs = new MenuItem("Save as...");
    MenuItem saveBtn = new MenuItem("Save");
    MenuItem exitApp = new MenuItem("Exit");
    Menu edit = new Menu("Edit");
    MenuItem clearAll = new MenuItem("New entry");
    Menu topicView = new Menu("All Topics");
    Menu scriptureView = new Menu("All Scriptures");
    Menu help = new Menu("Help");
    MenuItem visitWebsite = new MenuItem("Visit Website");
//GUI boxes, toolbars, and panes
    BorderPane root = new BorderPane();
    MenuBar mainMenu = new MenuBar();
    ToolBar toolBar = new ToolBar();
    VBox topContainer = new VBox();
//Bars
    Barbar barThread1 = new Barbar(500, 10);
    ProgressBar progressBar1 = new ProgressBar();


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ScriptureJournalApp sja = new ScriptureJournalApp();
        if (args.length < 1) {
            System.out.println("Error: " + args.length
                    + " args, PLEASE PASS IN FILE TO LOAD, defaults will be loaded this time...");
            inFile = FileServices.workDir + "src/yay.xml";
            //inFile = FileServices.workDir + "src/journ.txt";
        } else if (args.length == 1) {
            inFile = args[0];
        }
        else {
            System.err.println("So many parameters not supported, try again with one input file");
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
        System.out.println("Exporting journal file to xml: " + outFile);
        new FileServices().saveXml(jo, outFile);
    }

    @Override
    public void start(final Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        j = new Journal();
        
        setupButtons();
        setupListViews();
        setupLabels();
        setupEntryFields();
   
        loadJournal();

        defaultSetupJournal();
        setupBars();
        setupMenus();

        fileBoxes(primaryStage);
         
        Scene scene = new Scene(root, 700, 470);
        //Add the ToolBar and Main Menu to the VBox
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolBar);
        root.setTop(grid);

        //col, row, coltakeup, rowtakeup
        grid.add(mainMenu,        0,  0, 6, 1);
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
        grid.add(wordCountBtn,    2, 12, 1, 1);
        grid.add(wordCountLbl,    0, 12, 1, 1);
        grid.add(searchLDSBtn,    0, 12, 1, 1);
                
        primaryStage.setTitle("Scripture Journal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //a sort of destructor
    @Override
    public void stop() {
        System.out.println("Write again tomorrow!");
    }
   
    public static int CountWords(String in) {
        String trim = in.trim();
        if (trim.isEmpty()) {
            return 0;
        }
        return trim.split("\\s+").length; //separate string around spaces
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
    public void setupButtons() {

        addEntryBtn.setText("Add entry");
                
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
                updateWordCount();
            }
        });

        openFromListBtn.setText("Open");
        openFromListBtn.setTooltip(new Tooltip("Open currently selected entry from list"));
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
                updateWordCount();
            }
        });

        //Looks up scripture on lds.org
        searchLDSBtn.setText("Read");
        searchLDSBtn.setTooltip(new Tooltip("Look up on lds.org"));
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
        
        //Counts words in Entry
        wordCountBtn.setText("Count Words");
        wordCountBtn.setTooltip(new Tooltip("Count words in entry"));
        wordCountBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               updateWordCount();
            }
        });
    }
    
    public void setupListViews() {
        listView.setItems(entriesOList);
        listView.setPrefWidth(175);
        listView.setPrefHeight(255);

        sListView.setItems(scripturesOList);
        sListView.setPrefWidth(175);
        sListView.setPrefHeight(100);
       
        tListView.setItems(topicsOList);
        tListView.setPrefWidth(175);
        tListView.setPrefHeight(100);

    }
    public void setupLabels() {
        scriptureLbl.textProperty().setValue("Scriptures");
        topicLbl.textProperty().setValue("Topics");
        //TODO: make this actually work
        progBar1Lbl.textProperty().setValue("Loading...");
        //wordCountLbl.textProperty().setValue("");
    }
    
    public void setupEntryFields() {
        entryField = new TextArea("");
        entryField.setWrapText(true);
        entryField.setTooltip(new Tooltip("Context of Entry"));
    }
    public void loadJournal() {
        if (inFile.contains(".txt")) {
            j = loadTxt(inFile);
        } else if (inFile.contains(".xml")) {
            j = loadXml(inFile);
        } else {
            System.err.println(inFile + " is not a valid file to load");
        }
    }
    public void setupMenus() {
                //Create SubMenu File.
        openFile.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveBtn.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        exitApp.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        file.getItems().addAll(openFile, saveBtn, saveAs, exitApp);

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String fileName = inFile;
                if (fileName.toLowerCase().contains(".txt")) {
                    saveTxt(j, fileName);
                } else if (fileName.toLowerCase().contains(".xml")) {
                    saveXml(j, fileName);
                } else {
                    System.out.println("Not good to save to that type of file");
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
                updateWordCount();
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

        //Create SubMenu Edit.
        edit.getItems().add(clearAll);
        
        //Create SubMenu Topics and add them all in.
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
                        updateWordCount();
                    }
                });
            }
        }
                //Create SubMenu Scriptures and add them all in.
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
                        updateWordCount();
                    }
                });
            }
        }
        //Create SubMenu Help.
        help.getItems().add(visitWebsite);

        mainMenu.getMenus().addAll(file, edit, topicView, scriptureView, help);
    }

    public void fileBoxes(final Stage primaryStage) {
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
    }
    
    public void defaultSetupJournal() {
        for (Entry eCurr : j.getEntries()) {
            entriesOList.add(eCurr);
        }
        dateLbl = new Label("Date here");
        String currDate = j.getEntries().get(j.getEntries().size() - 1).getDate();
        dateLbl.textProperty().setValue(currDate);
        dateLbl.setTooltip(new Tooltip("Current entry"));

        //default load the first entry, content:
        entryField.setText(j.getEntries().get(j.getEntries().size() - 1).getContent());
        //scriptures list load
        for (Scripture sCurr : j.getEntries().get(j.getEntries().size() - 1).getScriptures()) {
            scripturesOList.add(sCurr);
        }
        //Topics list load
        for (Topic tCurr : j.getEntries().get(j.getEntries().size() - 1).getTopics()) {
            topicsOList.add(tCurr);
        }
        updateWordCount();
    }

    public void setupBars() {
         progressBar1.progressProperty().bind(barThread1.processProperty);
        Thread t1 = new Thread(barThread1);
        t1.start();
        if (barThread1.processProperty.equals((double)1)) {
        progBar1Lbl.textProperty().setValue("DONE");
        }
    }

    public void updateWordCount() {
        Integer wordCount = ScriptureJournalApp.CountWords(entryField.getText());
        entryField.setTooltip(new Tooltip("Word count: " + wordCount.toString()));
        Integer selWordCount = ScriptureJournalApp.CountWords(entryField.getSelectedText());
        wordCountLbl.textProperty().setValue("              Word count: Entry: " + wordCount.toString()
                                 + "   Highlighted: " + selWordCount.toString());
    }
}
