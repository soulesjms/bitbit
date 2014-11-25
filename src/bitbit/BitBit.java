/*
 * License goes here.
 */
package bitbit;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author adam and mark
 */
public class BitBit extends Application {

    final String website = "http://www.github.com/zvakanaka/bitbit";
//GUI boxes, toolbars, and panes
    BorderPane root = new BorderPane();
    MenuBar mainMenu = new MenuBar();
    ToolBar toolBar = new ToolBar();
    VBox topContainer = new VBox();
//Menus
    Menu file = new Menu("File");
    MenuItem openFile = new MenuItem("Open File");
    MenuItem saveAs = new MenuItem("Save as...");
    MenuItem saveBtn = new MenuItem("Save");
    MenuItem exitApp = new MenuItem("Exit");
    Menu edit = new Menu("Edit");
    MenuItem clearAll = new MenuItem("New entry");
    Menu help = new Menu("Help");
    MenuItem visitWebsite = new MenuItem("Visit Website");
    MenuItem showHelp = new MenuItem("Show Help");
    
    public static void main(String[] args){
        //Launch javafx GUI!
        launch(args);
    }
    
     @Override
    public void start(final Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
         
        setupMenus(primaryStage);
        setupFileBoxes(primaryStage);
                
        Scene scene = new Scene(root, 300, 200);
        
        //Add the ToolBar and Main Menu to the VBox
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolBar);
        root.setTop(grid);

        //col, row, coltakeup, rowtakeup
        grid.add(mainMenu, 0, 0, 6, 1);
                
        primaryStage.setTitle("BitBit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //a sort of destructor
    @Override
    public void stop() {
        System.out.println("Bye");
    }
     public void setupMenus(final Stage primaryStage) {
                //Create SubMenu File.
        openFile.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveBtn.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        exitApp.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        file.getItems().addAll(openFile, saveBtn, saveAs, exitApp);

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //save file here
            }
        });
        clearAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
                dialogVbox.getChildren().add(new Text("TODO:print help file"));
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

        openFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Loading bmp file...");
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Bitmap", "*.bmp"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
                File file = chooser.showOpenDialog(primaryStage);
                String fileName = file.getPath();
                if (fileName.toLowerCase().contains(".bmp")) {
                    System.out.println("TODO: load file from here");
                } else {
                    System.err.println("ERROR: Open path is empty");
                }
             }
        });
    }
}
