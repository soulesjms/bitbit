/*
 * License goes here.
 */
package bitbit;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
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
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author adam and mark
 */
public class BitBit extends Application {

    static boolean FIRSTRUNTEST = true;
    static Bitmap im;
    static String defaultFileIn = "/home/adam/Desktop/Jesus.bmp";
    static String curFile = defaultFileIn;
    //TODO: change images to be an array
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
    MenuItem saveBtn = new MenuItem("Save");
    MenuItem exitApp = new MenuItem("Quit");
    Menu edit = new Menu("Edit");
    MenuItem swap = new MenuItem("Swap");
    MenuItem clearAll = new MenuItem("Clear");
    Menu help = new Menu("Help");
    MenuItem visitWebsite = new MenuItem("Visit Website");
    MenuItem showHelp = new MenuItem("Show Help");
//ListViews
    ListView<String> listView = new ListView<>();
    ObservableList<String> thumbsList = FXCollections.observableArrayList();
    ListView<String> colorBlocks = new ListView<>();
    ObservableList<String> colorsList = FXCollections.observableArrayList(); 
    //TODO: add view for list
    ObservableList<Integer> swapSpots = FXCollections.observableArrayList(); 
    FlowPane imgViewBlocks = new FlowPane();
    FlowPane colorFlow = new FlowPane();
//Scene
    int sceneWidth = 700;
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
        String fileName = defaultFileIn;

        fileName = defaultFileIn;
        setupListViews(fileName);
        setupImageView(fileName);
        setupColorTableView(fileName);

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        
        //Add the ToolBar and Main Menu to the VBox
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolBar);

        root.setTop(grid);
        
        //col, row, coltakeup, rowtakeup
        grid.add(mainMenu, 0, 0, 1, 1);
        grid.add(listView, 0, 1, 1, 1);
        grid.add(imgViewBlocks,  5, 1, 1, 1);
        grid.add(colorFlow,     6, 1, 1, 1);

        primaryStage.setTitle("BitBit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //a sort of destructor
    @Override
    public void stop() {
        System.out.println("Bye");
    }
    
    //TODO: find speedier alternative, this is slower than anything
    public void setupImageView(String url) {

        System.out.println("Setting imageView for: " + url);
        try {
            imgViewBlocks.getChildren().removeAll(imgViewBlocks.getChildren());
           
            Bitmap im = new Bitmap(url);
            //System.out.println("Output:\n" + im);
            
            //spacing between pixels
            int gap = 0;
            //imgViewBlocks = new FlowPane();
            imgViewBlocks.setVgap(gap);
            imgViewBlocks.setHgap(gap);
            //displayWidth
            int dWidth = im.biWidth;
            int wrapLength = 400;
            
            while (dWidth < (wrapLength-im.biWidth)) {
                dWidth += im.biWidth;
            }
            System.out.println("Display Width " + dWidth);
            imgViewBlocks.setPrefWrapLength(dWidth+gap*(im.biWidth+1));
            
            //track spot in loop
            int i = 0;
            for (int perPix : im.pix) {
                //System.out.print(perPix);
                if ((i+1)%im.biWidth == 0) {
                    //System.out.println();
                }
                try {
                    //traverse colorTable of image and dereferrence proper colors
                    Color co = Color.rgb(im.getColorTable().getColor(perPix).getRed()
                        ,                im.getColorTable().getColor(perPix).getGreen()
                        ,                im.getColorTable().getColor(perPix).getBlue());
                    final Rectangle r = new Rectangle(dWidth/im.biWidth, dWidth/im.biWidth, co);                    
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
            System.err.println("Setup Image View Error\nImage: " + url);
        }
    }

    /**
     * TODO: pass in Image when selected from listView
     * Loads Image's colorTable into ColorTableView.
     */
    public void setupColorTableView(String fileName) {
        try {
            //TODO: load colorTable from Bitmap or BmpImage class
            // Put color table rectangles in a flow container
            colorFlow.getChildren().removeAll(colorFlow.getChildren());
            System.out.println("Loading ColorTableView for: " + fileName);
            im = new Bitmap(fileName);
            colorFlow.setVgap(2);
            colorFlow.setHgap(2);
            colorFlow.setPrefWrapLength(150);
            //track spot in loop
            int i = 0;
            for (int count = 0; count < im.getColorTable().getNumColors(); count++) {
                try {
                    Color co;
                    co = Color.rgb(im.getColorTable().getColor(count).getRed(),
                            im.getColorTable().getColor(count).getGreen(),
                            im.getColorTable().getColor(count).getBlue());

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
                    colorFlow.getChildren().add(r);
                    i++;
                } catch (IllegalArgumentException e) {
                    System.err.println("Color making failed");
                }
            }
        } catch (AWTException ex) {
            Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO: change image to array or list
    public void setupListViews(final String fileName) {
        //TODO: print image instead of String in listView
//        Image image = new Image("file:" + fileName);
        thumbsList.add(fileName);
        listView.setItems(thumbsList);
        listView.setPrefWidth(150);
        listView.setPrefHeight(sceneHeight - 30);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            //TODO: change list to hold bitmaps
            @Override
            public void handle(MouseEvent event) {
                String selectedImage = listView.getSelectionModel().getSelectedItem();
                System.out.println("Loading " + selectedImage + " in ImageView");

                setupImageView(selectedImage);
                setupColorTableView(selectedImage);
            }
        });
    }
    
    public void setupMenus(final Stage primaryStage) {
        //Create SubMenu File.
        openFile.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openFolder.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+O"));
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveBtn.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        exitApp.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        file.getItems().addAll(openFile, openFolder, saveBtn, saveAs, exitApp);

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    System.out.println("Saving");
                    String fileName = "/home/adam/Desktop/exported.bmp";
                    saveBMP(fileName);
            }
        });
        swap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int i = swapSpots.size();
                if (i > 1) {
                System.out.println("Swapping " + swapSpots.get(i-2) 
                                 + " and "     + swapSpots.get(i-1));
                im.getColorTable().swapColors(swapSpots.get(i-2)
                                            , swapSpots.get(i-1));
                //TODO:refresh colorTableView to show change
                }
                else {
                    System.err.println("ERROR: Select 2 colors to swap");
                }
            }
        });

        clearAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO: clear imageView here
                System.out.println("Clear Code");
                imgViewBlocks.getChildren().removeAll(imgViewBlocks.getChildren());
                colorFlow.getChildren().removeAll(colorFlow.getChildren());
                //Want to clear list on left side as well?
                //thumbsList = FXCollections.observableArrayList();
                //listView.setItems(thumbsList);
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
        edit.getItems().addAll(swap, clearAll);
       
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
                        System.out.println("Not good to save to that type of file");
                    }
                } else {
                    System.out.println("ERROR: save path is empty");
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
                    curFile = fileName;
                    System.out.println(fileName);
                    setupListViews(fileName);
                    setupImageView(fileName);
                    setupColorTableView(fileName);   
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
                 Files.walk(Paths.get(selectedDirectory.getAbsolutePath())).forEach(filePath -> {
                     if (Files.isRegularFile(filePath)) {
                         if (filePath.toString().endsWith(".bmp")){                             
                             System.out.println(filePath.toString());
                             curFile = filePath.toString();
                             setupListViews(filePath.toString());
                             setupImageView(filePath.toString());
                             setupColorTableView(filePath.toString());
                         } else {
                             System.err.println("ERROR: Folder error");
                         }
                     }
                 });
             } catch (IOException ex) {
                 Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
     });
    }
    public void saveBMP(String fileName) {
        try {
            im.exportBitmap(fileName);
        } catch (IOException ex) {
            Logger.getLogger(BitBit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //TODO: May be deleted WHEN the current setupImageView is properly working
    public void OLDsetupImageView(String url) {
        //TODO: make images same as images variable in constructor
        //PROBLEM: for some reason, smooth set to false does NOT work for bmp
        
        //                                                  preserve ratio, smooth 
        //Image images = new Image(url, sceneWidth/2, sceneWidth/2/2, true, false);
        //Image images = new Image("file:/home/adam/Desktop/P3183616.JPG");
        Image image = new Image(url);
        System.out.println("SetupImageView: " + url);
        imgView.setImage(image);
        imgView.setFitWidth(400);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(false);
    }
}
