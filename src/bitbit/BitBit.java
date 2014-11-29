/*
 * License goes here.
 */
package bitbit;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

    static String defaultFileIn = "resources/fun.bmp";
    //TODO: change images to be an array
    final Image images = new Image(defaultFileIn);
    ImageView pics = new ImageView();
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
    MenuItem clearAll = new MenuItem("Clear");
    Menu help = new Menu("Help");
    MenuItem visitWebsite = new MenuItem("Visit Website");
    MenuItem showHelp = new MenuItem("Show Help");
//ListViews
    ListView<Image> listView = new ListView<>();
    final ObservableList<Image> thumbsList = FXCollections.observableArrayList();
    final ListView<String> colorBlocks = new ListView<>();
    final ObservableList<String> colorsList = FXCollections.observableArrayList(); 
    final FlowPane flow = new FlowPane();
//Scene
    int sceneWidth = 700;
    int sceneHeight = 400;
    
    public static void main(String[] args){
        
        //TODO: replace fileIn with properties file val OR openFile box
        if (args.length != 0) {
            defaultFileIn = "file:" + args[0];
        }
        //Launch javafx GUI!
        launch(args);
    }
    
     @Override
    public void start(final Stage primaryStage) {
        GridPane grid = new GridPane();

        setupMenus(primaryStage);
        setupFileBoxes(primaryStage);
        setupListViews(images);
        setupImageView(defaultFileIn);
        setupColorTableView();

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        
        //Add the ToolBar and Main Menu to the VBox
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolBar);

        root.setTop(grid);
        
        //col, row, coltakeup, rowtakeup
        grid.add(mainMenu, 0, 0, 1, 1);
        grid.add(listView, 0, 1, 1, 1);
        grid.add(pics,     5, 1, 1, 1);
        grid.add(flow,     6, 1, 1, 1);

        primaryStage.setTitle("BitBit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //a sort of destructor
    @Override
    public void stop() {
        System.out.println("Bye");
    }
    
    public void setupImageView(String url) {
        //TODO: make images same as images variable in constructor
        //PROBLEM: for some reason, smooth set to false does NOT work for bmp
        
        //                                                  preserve ratio, smooth 
        //Image images = new Image(url, sceneWidth/2, sceneWidth/2/2, true, false);
        //Image images = new Image("file:/home/adam/Desktop/P3183616.JPG");
        Image image = new Image(url);
System.out.println("SetupImageView: " + url);
        pics.setImage(image);
        pics.setFitWidth(400);
        pics.setPreserveRatio(true);
        pics.setSmooth(false);
    }

    /**
     * TODO: pass in Image when selected from listView
     */
    public void setupColorTableView() {
        //TODO: load colorTable from Bitmap or BmpImage class
        //THE FOLLOWING CODE IS BORROWED FROM http://www.asgteach.com/blog/?p=327
        // Put all the color rectangles in a flow container

        flow.setVgap(2);
        flow.setHgap(2);
        flow.setPrefWrapLength(150);
        // Get the declared fields for the Color class
        Field[] colorFields = Color.class.getDeclaredFields();
        //track spot in loop
        int i = 0;
        for (Field fieldname : colorFields) {
            // get the field's modifiers so we can tell
            // if it's public and static
            int mods = fieldname.getModifiers();

            //TODO: replace the following loop with colorTable of selected image
            // Only use the field if it's
            // public, static, and NOT 'TRANSPARENT'
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)
                    && !(fieldname.getName().equals("TRANSPARENT"))) {
                try {
                    // create a color from the fieldname
                    Color c = Color.web(fieldname.getName());
                    // Make a rectangle with that field name's color
                    final Rectangle r = new Rectangle(15, 15, c);
                    final int iTemp = i;
                    r.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            //TODO: grab selected item only
                            String selectedItem = r.toString().substring(56, 62);
                            System.out.println("Selecting [" + iTemp + "] " + selectedItem);
                        }
                    });
                    // Configure the rectangle
                    // Add it to the flow container
                    flow.getChildren().add(r);
                    i++;
                } catch (IllegalArgumentException e) {
                    // just ignore it if for some reason we can't make
                    // a color
                }
            }
        }
    }

    //TODO: change image to array or list
    public void setupListViews(Image image) {
        //TODO: print image instead of String in listView
        thumbsList.add(image);
        listView.setItems(thumbsList);
        listView.setPrefWidth(150);
        listView.setPrefHeight(sceneHeight - 30);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                Image selectedImage = listView.getSelectionModel().getSelectedItem();
                System.out.println("Loading " + selectedImage + " in ImageView");
                pics.setImage(selectedImage);
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
                //TODO: save file here
            }
        });
        clearAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO: clear imageView here
                System.out.println("Put Clear Code Here");
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

        edit.getItems().add(clearAll);
       
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
                        System.out.println("TODO: save file to here");
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
                    //Load image
                    System.out.println(fileName);
                    setupListViews(new Image("file:" + fileName));
                    setupImageView("file:" + fileName);
                    
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
                //File defaultDirectory = new File("c:/");
                //chooser.setInitialDirectory(defaultDirectory);
                File selectedDirectory = chooser.showDialog(primaryStage);
                System.out.println(selectedDirectory);
         }
     });
    }
}
