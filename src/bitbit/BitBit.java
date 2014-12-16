/*
 * License goes here.
 */
package bitbit;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author adam quinton, mark soules, davey jones
 */
public class BitBit extends Application {

    ColorTable unified = new ColorTable();
    List<ColorTable> tables = new ArrayList<>();
//Image currently in imageView    
    static Bitmap im;
    final String website = "http://www.github.com/zvakanaka/bitbit";
    
//GUI boxes, toolbars, and panes
    BorderPane root = new BorderPane();
    MenuBar mainMenu = new MenuBar();
    ToolBar toolBar = new ToolBar();
    VBox topContainer = new VBox();
    VBox rightPane = new VBox();
//Menus
    Menu file = new Menu("File");
    MenuItem openFolder = new MenuItem("Open Folder");
    MenuItem openFile = new MenuItem("Open File");
    MenuItem saveAs = new MenuItem("Save as...");
    MenuItem saveAll = new MenuItem("Save all");
    MenuItem exitApp = new MenuItem("Quit");
    Menu edit = new Menu("Edit");
    MenuItem swapMenu = new MenuItem("Swap");
    MenuItem clearAll = new MenuItem("Clear");
    Menu help = new Menu("Help");
    MenuItem visitWebsite = new MenuItem("Visit Website");
    MenuItem showHelp = new MenuItem("Show Help");
//Buttons
    Button generateColorTable = new Button("Generate Color Table");
    Button swapBtn = new Button("Swap");
//Labels
      Label imageCtLbl = new Label("Color Table");
      Label unifiedLbl = new Label("Unified Color Table");       
//Lists for views
    ListView<Bitmap> listView = new ListView<>();
    ObservableList<Bitmap> thumbsList = FXCollections.observableArrayList();
    ListView<String> colorBlocks = new ListView<>();
    ObservableList<String> colorsList = FXCollections.observableArrayList(); 
    ObservableList<Integer> swapSpots = FXCollections.observableArrayList(); 
//Panes and Boxes
    FlowPane colorFlow = new FlowPane();
    FlowPane unifiedFlow = new FlowPane();
    FlowPane swapColorsFlow = new FlowPane();
    ScrollPane colorSPane = new ScrollPane();
    ScrollPane unifiedSPane = new ScrollPane();
    VBox imageView = new VBox();
             
//Scene
    int sceneWidth = 780;
    int sceneHeight = 450;
    
    public static void main(String[] args){
        
        //Launch javafx GUI!
        launch(args);
    }
    
     @Override
     public void start(final Stage primaryStage) {
         GridPane grid = new GridPane();
         
         setupMenus(primaryStage);
         setupFileBoxes(primaryStage);
         setupButtons();
         
         try {
             Scene scene = new Scene(root, sceneWidth, sceneHeight);
             
             //Add the ToolBar and Main Menu to the VBox
             topContainer.getChildren().addAll(mainMenu, toolBar);
             
             root.setTop(grid);
             
             imageView.setMinWidth(200);
             int paneHeight = 175;
             colorSPane.setMaxHeight(paneHeight);
             unifiedSPane.setMaxHeight(paneHeight);
             colorSPane.setMinHeight(paneHeight);
             unifiedSPane.setMinHeight(paneHeight);
             colorSPane.setContent(colorFlow);
             unifiedSPane.setContent(setupColorTableView(unified, unifiedFlow, true));
             rightPane.getChildren().addAll(imageCtLbl, colorSPane, unifiedLbl, unifiedSPane);             
             //Swap view of last 2 selected colors
             swapColorsFlow.setMaxWidth(34);

             //col, row, coltakeup, rowtakeup
             grid.add(mainMenu,           0, 0, 7, 1); //menu Bar
             grid.add(listView,           0, 1, 1, 2); //list of files
             grid.add(imageView,          1, 1, 1, 2); //image display
             grid.add(swapColorsFlow,     4, 0, 1, 1); //image display
             grid.add(swapBtn,            3, 0, 1, 1); //swap button
             grid.add(generateColorTable, 2, 0, 1, 1);
             grid.add(rightPane,          2, 1, 3, 1);
             
             primaryStage.setTitle("PixiMagic");
             primaryStage.setScene(scene);
             primaryStage.show();
             primaryStage.getIcons().add(new Image("file:src//resources//icon.png"));
         } catch (Exception ex) {
             Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
         }    
    }

     /**
      * 
      * @param bitmap 
      */
    public void setupImageView(Bitmap bitmap) {
        imageView.getChildren().removeAll(imageView.getChildren());
        ImageStrategy imgStrategy;
        if (bitmap.getBiWidth() > 200) {
            imgStrategy = new LargeImageSetup();
        }
        else {
            imgStrategy = new SmallImageSetup();
        }
        imageView.getChildren().add(imgStrategy.setupImageView(bitmap));
       
        //im is for use in export
        im = bitmap;
    }

      /**
     * Loads Image's colorTable into ColorTableView.
     * @param ct
     * @param flow
     * @param isClickable
     * @return 
     */
    public FlowPane setupColorTableView(ColorTable ct, FlowPane flow, boolean isClickable) {
            flow.getChildren().removeAll(flow.getChildren());
            int gap = 2;
            flow.setVgap(gap);
            flow.setHgap(gap);
            flow.setMinWidth(200);
            flow.setPrefWrapLength(flow.getMinWidth());

            for (int count = 0; count < ct.getNumColors(); count++) {
                try {
                    Color co;
                    co = Color.rgb(ct.getColor(count).getRed(),
                            ct.getColor(count).getGreen(),
                            ct.getColor(count).getBlue());

                    final Rectangle r = new Rectangle(15, 15, co);
                    final int iTemp = count;
                    if (isClickable) {
                        r.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            
                            @Override
                            public void handle(MouseEvent event) {
                                //grab selected item only
                                if (swapSpots.size() >= 1) {
                                   swapBtn.setDisable(false);
                                   swapMenu.setDisable(false);
                                   
                                }
                                swapSpots.add(iTemp);
                                setupSwapColorsFlow(swapSpots);
                            }
                        });
                    }
                    // add rectangle to flow container
                    flow.getChildren().add(r);
                } catch (IllegalArgumentException e) {
                    System.err.println("Color makin    /**\n" +
"g failed");
                }
            }
        return flow;
    }
    
    /**
     * Sets up all the attributes for the buttons and their onclick methods.
     */
    public void setupButtons() {
        swapBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                swap(unified, unifiedFlow);
            }
        });
        generateColorTable.setDisable(true);
        swapBtn.setDisable(true);
        generateColorTable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                List<ColorTable> tables = new ArrayList<>();
                for (Bitmap bmpUrl : thumbsList){
                    tables.add(bmpUrl.getColorTable());
                }
                try {
                    unifiedFlow.getChildren().removeAll(unifiedFlow.getChildren());
                    unified = new ColorTableUnifier().unify(tables);
                    setupColorTableView(unified, unifiedFlow, true);
                    saveAll.setDisable(false);
                    saveAs.setDisable(false);
                } catch (AWTException ex) {
                    Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    /**
     * sets up the view for the file names being used and edited.
     * @param bitmap 
     */
    public void updateFileListView(final Bitmap bitmap) {
        thumbsList.add(bitmap);
        listView.setItems(thumbsList);
        listView.setPrefWidth(150);
        listView.setPrefHeight(sceneHeight - 30);
        
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                   Bitmap selectedImage = listView.getSelectionModel().getSelectedItem();
                   setupImageView(selectedImage);
                   setupColorTableView(selectedImage.getColorTable(), colorFlow, false);
            }
        });
        listView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (thumbsList.size() > 0) {
                listView.setTooltip(new Tooltip(listView.getSelectionModel().getSelectedItem().getBfName()+ " is selected"));
                }
                else {
                    listView.setTooltip(new Tooltip("Open an image..."));
                }
            }
        });
        //set focussed cell to last item in listview
        if (thumbsList.size() > 0) {
            listView.getSelectionModel().select(thumbsList.size()-1);
        }
    }
    
    /**
     * draws a representation of the last two colors selected (clicked)
     * @param swapSpots 
     */
    public void setupSwapColorsFlow(List<Integer> swapSpots) {
        swapColorsFlow.getChildren().removeAll(swapColorsFlow.getChildren());
        int gap = 2;
        swapColorsFlow.setVgap(gap);
        swapColorsFlow.setHgap(gap);
        swapColorsFlow.setPrefWrapLength(34);
        if (swapSpots.size() > 0) {
            int count = swapSpots.size();
            if (swapSpots.size() > 1) {
                count = swapSpots.size() - 1;
            }
            for (; count <= swapSpots.size(); count++) {
                try {
                    Color co;
                    co = Color.rgb(unified.getColor(swapSpots.get(count-1)).getRed(),
                            unified.getColor(swapSpots.get(count-1)).getGreen(),
                            unified.getColor(swapSpots.get(count-1)).getBlue());
                    
                    final Rectangle r = new Rectangle(15, 15, co);
                    final int iTemp = count;
                    r.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        
                        @Override
                        public void handle(MouseEvent event) {
                            //TODO: grab selected item only
                            String selectedItem = r.toString().substring(56, 62);
                            System.out.println("Displaying [" + iTemp + "] " + selectedItem);
                        }
                    });
                    // add rectangle to swapColorsFlow container
                    swapColorsFlow.getChildren().add(r);
                } catch (IllegalArgumentException e) {
                    System.err.println("Color making failed");
                }
            }
        }
    }
    /**
     * sets up the menus and their actions
     * @param primaryStage 
     */
    public void setupMenus(final Stage primaryStage) {
        //Create SubMenu File.
        openFile.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openFolder.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+O"));
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveAll.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
        exitApp.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        file.getItems().addAll(openFile, openFolder, saveAs, saveAll, exitApp);
        saveAll.setDisable(true);
        saveAs.setDisable(true);
        swapMenu.setDisable(true);
        //do what swapBtn does
        swapMenu.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            swap(unified, unifiedFlow);
        }
        });

        clearAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //clear imageView
                imageView.getChildren().removeAll(imageView.getChildren());
                colorFlow.getChildren().removeAll(colorFlow.getChildren());
                unifiedFlow.getChildren().removeAll(unifiedFlow.getChildren());
                //clear list on left side as well
                thumbsList = FXCollections.observableArrayList();
                listView.setItems(thumbsList);
            }
        });
        
        exitApp.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               System.exit(0);
            }
        });
        //visits online repository on github
        visitWebsite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VisitWebsite vW = new VisitWebsite(website);
                Thread t = new Thread(vW);
                t.start();//does vW.run(); in a thread
            }
        });
        
        showHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Help is under construction... Refer to Website."));
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
         });
    
        //Create SubMenu Edit.
        edit.getItems().addAll(swapMenu, clearAll);
       
        //Create SubMenu Help.
        help.getItems().addAll(visitWebsite, showHelp);

        mainMenu.getMenus().addAll(file, edit, help);
    }

    /**
     * Sets up the save/open buttons and their functions.
     * @param primaryStage 
     */
    public void setupFileBoxes(final Stage primaryStage) {
        saveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser1 = new FileChooser();
                fileChooser1.setTitle("Save bmp");
                File file = fileChooser1.showSaveDialog(primaryStage);
                if (file != null) {
                    String fileName = file.getPath();
                    if (fileName.toLowerCase().endsWith(".bmp")) {
                        saveBMP(im, fileName);
                    } 
                    else {
                        saveBMP(im, fileName+".bmp");
                    }
                }
            }
        });
        
        //saves all images in listview with the generated color table
        saveAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                File selectedDirectory = chooser.showDialog(primaryStage);
                if (selectedDirectory == null) {
                    return;
                }
                int i = 1;
                for(Bitmap url : thumbsList) {
                    saveBMP(url, selectedDirectory.getPath() + "/exported" + i++ + ".bmp");
                }
            }
        });

        //load folder of bmps into list
        openFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {                
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Bitmap", "*.bmp"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                File file = chooser.showOpenDialog(primaryStage);
                if (file == null) {
                    return;
                }
                String fileName = file.getPath();
                if (fileName.toLowerCase().endsWith(".bmp")) {
                    Bitmap curBitmap;
                    try {
                        curBitmap = new Bitmap(fileName);
                        setupImageView(curBitmap);
                        updateFileListView(curBitmap);
                        setupColorTableView(curBitmap.getColorTable(), colorFlow, false);
                        generateColorTable.setDisable(false);
                        saveAs.setDisable(true);
                        saveAll.setDisable(true);
                    } catch (Exception ex) {
                        Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.err.println("ERROR: File is not a bmp");
                }
             }
        });
        
     //opens only bmp images
     openFolder.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Choose BMP Folder");
                File selectedDirectory = chooser.showDialog(primaryStage);
                if (selectedDirectory == null) {
                    return;
                }
                try {
                    thumbsList = FXCollections.observableArrayList();
                    listView.setItems(thumbsList);
                    Files.walk(Paths.get(selectedDirectory.getAbsolutePath())).forEach(filePath -> {
                        if (Files.isRegularFile(filePath)) {
                            if (filePath.toString().endsWith(".bmp")){
                                try {
                                    Bitmap bitmap = new Bitmap(filePath.toString());
                                    updateFileListView(bitmap);
                                } catch (AWTException ex) {
                                    Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    });
                    try {
                        setupImageView(thumbsList.get(thumbsList.size()-1));
                        setupColorTableView(thumbsList.get(thumbsList.size()-1).getColorTable(), colorFlow, false);
                        generateColorTable.setDisable(false);
                        saveAs.setDisable(true);
                        saveAll.setDisable(true);
                    } catch (Exception ex) {
                        Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                    }
             } catch (IOException ex) {
                 Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
     });
    }
    
    /**
     * Swap last two colors in swapped list passed in and updates unifiedColorTableView
     * @param ct
     * @param flow
     */
    public void swap(ColorTable ct, FlowPane flow) {
        int i = swapSpots.size();
        if (i > 1) {
            ct.swapColors(swapSpots.get(i-2)
                    , swapSpots.get(i-1));            
            //refresh colorTableView to show change
            setupColorTableView(ct, flow, true);
            swapSpots.clear();
            swapColorsFlow.getChildren().removeAll(swapColorsFlow.getChildren());
            swapBtn.setDisable(true);
        }
        else {
            System.err.println("ERROR: Select 2 colors to swap");
        }
    }
    
    /**
     * replaces the colorTable with unified color table and saves it.
     * @param bitmap
     * @param fileName 
     */
    public void saveBMP(Bitmap bitmap, String fileName) {
        
        try {
            Bitmap toExport = bitmap.clone();
            toExport.replaceColorTable(unified);
            toExport.exportBitmap(fileName);
        } catch (IOException ex) {
            Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}