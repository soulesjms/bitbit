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

    ColorTable unified;
    List<ColorTable> tables = new ArrayList<>();
    
    static Bitmap im;
    static String defaultFileIn = "C:\\Users\\Jonny\\Desktop\\BMPtests\\4pixle.bmp";
    static String defaultFileIn2 = "C:\\Users\\Jonny\\Desktop\\BMPtests\\4pixle2.bmp";
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
//Scene
    int sceneWidth = 800;
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
         primaryStage.setTitle("BitBit");
         primaryStage.getIcons().add(new Image("file:src\\resources\\icon.png"));
         GridPane grid = new GridPane();
         
         setupMenus(primaryStage);
         setupFileBoxes(primaryStage);
         setupButtons();
         
         
         String fileName = defaultFileIn;
         
         fileName = defaultFileIn;
         try {
             Bitmap curBitmap = new Bitmap(defaultFileIn);
             Bitmap nexBitmap = new Bitmap(defaultFileIn2);
             setupListViews(nexBitmap);
             setupImageView(curBitmap);
             setupColorTableView(curBitmap.getColorTable(), colorFlow);
             setupListViews(curBitmap);
             
             Scene scene = new Scene(root, sceneWidth, sceneHeight);
             
             //Add the ToolBar and Main Menu to the VBox
             topContainer.getChildren().addAll(mainMenu, toolBar);
             
             root.setTop(grid);
             
             ScrollPane colorSPane = new ScrollPane();
             colorSPane.setContent(colorFlow);
             
             ScrollPane unifiedSPane = new ScrollPane();
             //TODO: change im.get... to unified(and getUnified beforehand)
             unifiedSPane.setContent(setupColorTableView(curBitmap.getColorTable(), unifiedFlow));
             
//col, row, coltakeup, rowtakeup
             grid.add(mainMenu,      0, 0, 7, 1); //menu Bar
             grid.add(listView,      0, 1, 1, 2); //list of files
             grid.add(imgViewBlocks, 1, 1, 1, 2); //image display
             grid.add(swapBtn,       2, 0, 1, 1); //swap button
//        grid.add(swapLbl,       2, 1, 1, 1);
             grid.add(colorFlow,     2, 1, 2, 1); //color table display
             grid.add(generateColorTable, 3, 0, 1, 1);
//        grid.add(swapLbl,          2, 1, 1, 1);
             grid.add(colorSPane,         2, 1, 2, 1); //color table display
             grid.add(unifiedSPane,       2, 2, 2, 1); //color table display
             
             primaryStage.setTitle("PixiMagic");
             primaryStage.setScene(scene);
             primaryStage.show();
         } catch (AWTException ex) {
             Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
         }    
    }

    //a sort of destructor
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
//imgViewBlocks = new FlowPane();
            imgViewBlocks.setVgap(gap);
            imgViewBlocks.setHgap(gap);
//displayWidth
            int dWidth = bitmap.biWidth;
            int wrapLength = 400;
            while (dWidth < (wrapLength-bitmap.biWidth)) {
                dWidth += bitmap.biWidth;
            }
            System.out.println("Display Width " + dWidth);
            imgViewBlocks.setPrefWrapLength(dWidth+gap*(bitmap.biWidth+1));
//track spot in loop
            int i = 0;
            for (int perPix : bitmap.pix) {
//System.out.print(perPix);
                if ((i+1)%bitmap.biWidth == 0) {
//System.out.println();
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
//To get color from selected rectangle
                            String colorString = "ERROR CONVERTING COLOR TO STRING";
                            if (r.toString().contains("fill=0x")) {
                                colorString = r.toString().split("fill=0x")[1];
                            }
                            String selectedItem = colorString.substring(0, 6);
                            System.out.println("Selecting [" + perPix + "] "
                                    + selectedItem + " [" + iTemp + "] in pixel array");
                            swapSpots.add(perPix);
                        }
                    });
// Configure the rectangle
// Add it to the imgViewBlocks container
                    imgViewBlocks.getChildren().add(r);
                    i++;
                } catch (IllegalArgumentException e) {
                }
            }
        } catch (Exception e) {
            System.err.println("Setup Image View Error\nImage: " + bitmap.bfName);
        }
        im = bitmap;
        
    }

      /**
     * TODO: pass in Image when selected from listView
     * Loads Image's colorTable into ColorTableView.
     * @param fileName
     */
    public FlowPane setupColorTableView(ColorTable ct, FlowPane flow) {
        
            flow.getChildren().removeAll(flow.getChildren());
            int gap = 2;
            flow.setVgap(gap);
            flow.setHgap(gap);
            flow.setPrefWrapLength(400-imgViewBlocks.getPrefWrapLength()+150);
                                         
            //track spot in loop
            int i = 0;
            for (int count = 0; count < ct.getNumColors(); count++) {
                try {
                    Color co;
                    co = Color.rgb(ct.getColor(count).getRed(),
                            ct.getColor(count).getGreen(),
                            ct.getColor(count).getBlue());

                    final Rectangle r = new Rectangle(15, 15, co);
                    final int iTemp = i;
                    r.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            //TODO: grab selected item only
                            String selectedItem = r.toString().substring(56, 62);
                            System.out.println("Selecting [" + iTemp + "] " + selectedItem);
                            swapSpots.add(iTemp);
                        }
                    });
                    // add rectangle to flow container
                    flow.getChildren().add(r);
                    i++;
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
                    //TODO: change setupColorTableView to accept a color table
                    setupColorTableView(unified, unifiedFlow);
                } catch (AWTException ex) {
                    Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    //TODO: change image to array or list
    public void setupListViews(final Bitmap bitmap) {
//TODO: print image instead of String in listView
//Image image = new Image("file:" + fileName);
        thumbsList.add(bitmap);
        listView.setItems(thumbsList);
        listView.setPrefWidth(150);
        listView.setPrefHeight(sceneHeight - 30);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//TODO: change list to hold bitmaps
            @Override
            public void handle(MouseEvent event) {
                   Bitmap selectedImage = listView.getSelectionModel().getSelectedItem();
                   setupImageView(selectedImage);
                   setupColorTableView(selectedImage.getColorTable(), colorFlow);
            }
        });
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

        saveMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    String fileName = "/home/adam/Desktop/exported.bmp";
                    System.out.println("Saving to " + fileName);
                    saveBMP(fileName);
            }
        });
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
                    if (fileName.toLowerCase().contains(".bmp")) {
                        System.out.println("Save file to here");
                        saveBMP(fileName);
                    } 
                    else {
                        System.out.println("Not good to save to that type of file, adding .bmp");
                        saveBMP(fileName+".bmp");
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
                    //TODO: change setupColorTableView to accept a color table
                    setupColorTableView(unified, unifiedFlow);
                } catch (AWTException ex) {
                    Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //TODO: change to load folder of images into list
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
                        setupListViews(curBitmap);
                        setupColorTableView(curBitmap.getColorTable(), colorFlow);
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
                                    setupListViews(bitmap);
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
                        setupColorTableView(curBitmap.getColorTable(), colorFlow);
                        setupListViews(curBitmap);
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
     */
    public void swap(ColorTable ct, FlowPane flow) {
        int i = swapSpots.size();
        if (i > 1) {
            System.out.println("Swapping " + swapSpots.get(i-2)
                    + " and "     + swapSpots.get(i-1));
            
            ct.swapColors(swapSpots.get(i-2)
                    , swapSpots.get(i-1));            
            //refresh colorTableView to show change
            setupColorTableView(ct, flow);
        }
        else {
            System.err.println("ERROR: Select 2 colors to swap");
        }
    }
    
    public void saveBMP(String fileName) {
        try {
            try {
                im.replaceColorTable(unified);
            } catch (AWTException ex) {
                Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
            }
            im.exportBitmap(fileName);
        } catch (IOException ex) {
            Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
