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
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author adam and mark
 */
public class BitBit extends Application {

    ColorTable unified = new ColorTable();
    List<ColorTable> tables = new ArrayList<>();
    
    static Bitmap im;
    static String defaultFileIn = "C:/Users/David/Desktop/test.bmp";
    static String defaultFileIn2 = "C:/Users/David/Desktop/test2.bmp";
    static String curFile = defaultFileIn;
    //final Image images = new Image(defaultFileIn);
    ImageView imgView = new ImageView();
    final String website = "http://www.github.com/zvakanaka/bitbit";

//GUI boxes, toolbars, and panes
    BorderPane root = new BorderPane();
    MenuBar mainMenu = new MenuBar();
    ToolBar toolBar = new ToolBar();
    VBox topContainer = new VBox();
//Menus
    Menu file = new Menu("File");
    MenuItem openFolder = new MenuItem("Open Folder");
    MenuItem openFile = new MenuItem("Open File");
    MenuItem saveAs = new MenuItem("Save as...");
    MenuItem saveMenuBtn = new MenuItem("Save");
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
    Label swapLbl = new Label("swappoing 1 and 2");
//ListViews
    ListView<Bitmap> listView = new ListView<>();
    ObservableList<Bitmap> thumbsList = FXCollections.observableArrayList();
    ListView<String> colorBlocks = new ListView<>();
    ObservableList<String> colorsList = FXCollections.observableArrayList(); 
    ObservableList<Integer> swapSpots = FXCollections.observableArrayList(); 
    FlowPane imgViewBlocks = new FlowPane();
    FlowPane colorFlow = new FlowPane();
    FlowPane unifiedFlow = new FlowPane();
    FlowPane swapColorsFlow = new FlowPane();
//Scene
    int sceneWidth = 780;
    int sceneHeight = 450;
    
    public static void main(String[] args){
        
        //TODO: replace fileIn with properties file val OR openFile box
        if (args.length != 0) {
            defaultFileIn = args[0];
        }
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
             //TODO: remove these
             Bitmap curBitmap = new Bitmap(defaultFileIn);
             Bitmap nexBitmap = new Bitmap(defaultFileIn2);
             setupFileListView(nexBitmap);
             setupImageView(curBitmap);
             setupColorTableView(curBitmap.getColorTable(), colorFlow, false);
             
             setupFileListView(curBitmap);
             
             Scene scene = new Scene(root, sceneWidth, sceneHeight);
             
             //Add the ToolBar and Main Menu to the VBox
             topContainer.getChildren().addAll(mainMenu, toolBar);
             
             root.setTop(grid);
             
             ScrollPane colorSPane = new ScrollPane();
             colorSPane.setTooltip(new Tooltip("Color Table of: " + im.toString()));
             colorSPane.setContent(colorFlow);
             
             ScrollPane unifiedSPane = new ScrollPane();
             unifiedSPane.setTooltip(new Tooltip("Unified Color Table"));
             unifiedSPane.setContent(setupColorTableView(unified, unifiedFlow, true));
             
             //Swap view of last 2 selected colors
             swapColorsFlow.setMaxWidth(34);

             //col, row, coltakeup, rowtakeup
             grid.add(mainMenu,        0, 0, 7, 1); //menu Bar
             grid.add(listView,        0, 1, 1, 2); //list of files
             grid.add(imgViewBlocks,   1, 1, 1, 2); //image display
             grid.add(swapColorsFlow,  4, 0, 1, 1); //image display
             grid.add(swapBtn,         3, 0, 1, 1); //swap button
             //        grid.add(swapLbl,       2, 1, 1, 1);
             grid.add(colorFlow,       2, 1, 2, 1); //color table display
             grid.add(generateColorTable, 2, 0, 1, 1);
             grid.add(colorSPane,         2, 1, 2, 1); //color table display
             grid.add(unifiedSPane,       2, 2, 2, 1); //color table display
             
             primaryStage.setTitle("PixiMagic");
             primaryStage.setScene(scene);
             primaryStage.show();
             primaryStage.getIcons().add(new Image("file:src//resources//icon.png"));
         } catch (AWTException ex) {
             Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
         }    
    }

    //a sort of destructor, called when program exits
    @Override
    public void stop() {
        System.out.println("Bye");
    }
    
        //TODO: find speedier alternative, this is slower than anything
    public void setupImageView(Bitmap bitmap) {
        System.out.println("Setting imageView for: " + bitmap.bfName);
        
        try {
            imgViewBlocks.getChildren().removeAll(imgViewBlocks.getChildren());

            //spacing between pixels
            int gap = 0;
            
            imgViewBlocks.setVgap(gap);
            imgViewBlocks.setHgap(gap);
            //displayWidth
            int dWidth = bitmap.biWidth;
            int wrapLength = 400;
            //enlarge view of image if space is left over
            while (dWidth < (wrapLength-bitmap.biWidth)) {
                dWidth += bitmap.biWidth;
            }
            imgViewBlocks.setPrefWrapLength(dWidth+gap*(bitmap.biWidth+1));
            imgViewBlocks.setMinWidth(dWidth+gap*(bitmap.biWidth+1));
            //track spot in loop
            int i = 0;
            for (int perPix : bitmap.pix) {
                if ((i+1)%bitmap.biWidth == 0) {
                //reached end of line
                }
                try {
                    //traverse colorTable of image and dereferrence proper colors
                    Color co = Color.rgb(bitmap.getColorTable().getColor(perPix).getRed()
                            , bitmap.getColorTable().getColor(perPix).getGreen()
                            , bitmap.getColorTable().getColor(perPix).getBlue());
                    final Rectangle r = new Rectangle(dWidth/bitmap.biWidth, dWidth/bitmap.biWidth, co);
                    final int iTemp = i;
                    //grab selected rectangle only and print its color
                    r.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            //get color from selected rectangle
                            String colorString = "ERROR CONVERTING COLOR TO STRING";
                            if (r.toString().contains("fill=0x")) {
                                colorString = r.toString().split("fill=0x")[1];
                            }
                            String selectedItem = colorString.substring(0, 6);
                            System.out.println("Clicked [" + perPix + "] "
                                    + selectedItem + " [" + iTemp + "] in pixel array"
                                    + ", No action will be taken");
                        }
                    });
                    
                    // Configure rectangle and add to the imgViewBlocks
                    imgViewBlocks.getChildren().add(r);
                    i++;
                } catch (IllegalArgumentException e) {
                }//        swapColorsFlow.setPrefWrapLength(50);
            }
        } catch (Exception e) {
            System.err.println("Setup Image View Error\nImage: " + bitmap.bfName);
        }
        //im is for use in export
        im = bitmap;
    }

      /**
     * TODO: pass in Image when selected from listView
     * Loads Image's colorTable into ColorTableView.
     * @param fileName
     */
    public FlowPane setupColorTableView(ColorTable ct, FlowPane flow, boolean isClickable) {
        
            flow.getChildren().removeAll(flow.getChildren());
            int gap = 2;
            flow.setVgap(gap);
            flow.setHgap(gap);
            flow.setPrefWrapLength(400-imgViewBlocks.getPrefWrapLength()+150);

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
                                //TODO: grab selected item only
                                String selectedItem = r.toString().substring(56, 62);
                                System.out.println("Selecting [" + iTemp + "] " + selectedItem);
                                swapSpots.add(iTemp);
                                setupSwapColorsFlow(swapSpots);
                            }
                        });
                    }
                    // add rectangle to flow container
                    flow.getChildren().add(r);
                } catch (IllegalArgumentException e) {
                    System.err.println("Color making failed");
                }
            }
        return flow;
    }
    
    public void setupButtons() {
        swapBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                swap(unified, unifiedFlow);
            }
        });
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
                } catch (AWTException ex) {
                    Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void setupFileListView(final Bitmap bitmap) {
        //TODO: print image instead of String in listView
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
                listView.setTooltip(new Tooltip(listView.getSelectionModel().getSelectedItem().bfName+ " is selected"));
            }
        });
        //set focussed cell to last item in listview
        listView.getSelectionModel().select(thumbsList.size()-1);
    }
    
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
    
    public void setupMenus(final Stage primaryStage) {
        //Create SubMenu File.
        openFile.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openFolder.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+O"));
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveAll.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
        saveMenuBtn.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        exitApp.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        file.getItems().addAll(openFile, openFolder, saveMenuBtn, saveAs, saveAll, exitApp);

        //TODO: remove-
        saveMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    String fileName = "/home/adam/Desktop/exported.bmp";
                    System.out.println("Saving to " + fileName);
                    saveBMP(im, fileName);
            }
        });
        //TODO: get working again, just do what swapBtn does
        /*        swapMenu.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        swap();
        }
        });*/

        clearAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO: clear imageView here
                System.out.println("Clear Code");
                imgViewBlocks.getChildren().removeAll(imgViewBlocks.getChildren());
                colorFlow.getChildren().removeAll(colorFlow.getChildren());
                unifiedFlow.getChildren().removeAll(unifiedFlow.getChildren());
                //Want to clear list on left side as well?
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
                        System.out.println("Save file to here");
                        saveBMP(im, fileName);
                    } 
                    else {
                        System.out.println("Not good to save to that type of file, adding .bmp");
                        saveBMP(im, fileName+".bmp");
                    }
                } else {
                    System.out.println("ERROR: save path is empty");
                }
            }
        });
        
        saveAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<ColorTable> tables = new ArrayList<>();
                for (Bitmap bmpUrl : thumbsList){
                    tables.add(bmpUrl.getColorTable());
                }
                try {
                    unifiedFlow.getChildren().removeAll(unifiedFlow.getChildren());
                    unified = new ColorTableUnifier().unify(tables);
                    setupColorTableView(unified, unifiedFlow, true);
                    //TODO: actually write out each image
                } catch (AWTException ex) {
                    Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //load folder of bmps into list
        openFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.print("Loading bmp file... ");
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Bitmap", "*.bmp"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                File file = chooser.showOpenDialog(primaryStage);
                String fileName = file.getPath();
                if (fileName.toLowerCase().contains(".bmp")) {
                    System.out.println(fileName);
                    Bitmap curBitmap;
                    try {
                        curBitmap = new Bitmap(fileName);
                        setupImageView(curBitmap);
                        setupFileListView(curBitmap);
                        setupColorTableView(curBitmap.getColorTable(), colorFlow, false);
                    } catch (Exception ex) {
                        Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.err.println("ERROR: File is not a bmp");
                }
             }
        });
        
     openFolder.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
                System.out.print("Loading directory... ");
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Choose BMP Folder");
                File selectedDirectory = chooser.showDialog(primaryStage);
                System.out.println("filepath:");
                System.out.println(selectedDirectory);
                try {
                    System.out.println("Bmp files found: ");
                    thumbsList = FXCollections.observableArrayList();
                    listView.setItems(thumbsList);
                    Files.walk(Paths.get(selectedDirectory.getAbsolutePath())).forEach(filePath -> {
                        if (Files.isRegularFile(filePath)) {
                            if (filePath.toString().endsWith(".bmp")){
                                try {
                                    System.out.println(filePath.toString());
                                    Bitmap bitmap = new Bitmap(filePath.toString());
                                    setupFileListView(bitmap);
                                } catch (AWTException ex) {
                                    Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                             System.err.println("ERROR: Folder error");
                         }
                     }
                 });
                 //Add tables to unified color table
                    try {
                        unified = new ColorTableUnifier().unify(tables);
                    } catch (AWTException ex) {
                        Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Bitmap curBitmap;
                    try {
                        curBitmap = new Bitmap(curFile);
                        setupImageView(curBitmap);
                        setupColorTableView(curBitmap.getColorTable(), colorFlow, false);
                        setupFileListView(curBitmap);
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
     * Swap last two colors in list passed in
     * @param ct
     * @param flow
     */
    public void swap(ColorTable ct, FlowPane flow) {
        int i = swapSpots.size();
        if (i > 1) {
            System.out.println("Swapping " + swapSpots.get(i-2)
                    + " and "     + swapSpots.get(i-1));
            ct.swapColors(swapSpots.get(i-2)
                    , swapSpots.get(i-1));            
            //refresh colorTableView to show change
            setupColorTableView(ct, flow, true);
            swapSpots.clear();
            swapColorsFlow.getChildren().removeAll(swapColorsFlow.getChildren());
        }
        else {
            System.err.println("ERROR: Select 2 colors to swap");
        }
    }
    
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
