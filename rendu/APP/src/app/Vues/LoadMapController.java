package app.Vues;

import app.APP;
import app.Models.Toolbox;
import app.Models.CSVParser;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;


import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import javafx.event.Event;
import javafx.event.EventHandler;

public class LoadMapController implements Initializable {
    
    @FXML
    AnchorPane ap;

    @FXML
    GridPane mapList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Toolbox.setMode(ap);
        // pupulate map list
        populateMapList();        
    }

    /**
     * populate map list
     */
    private void populateMapList()
    {
        // clean map list first
        mapList.getChildren().clear();

        ArrayList<String> mapNames = Toolbox.getMaplist();
        if(mapNames.size() == 0)
        {
            Text t = new Text("You do not have any MAP yet!");
            t.getStyleClass().add("empty-map-text");
            mapList.add(t, 0, 0);
        }

        int nCol = 3;
        int nRow = (mapNames.size() % 3 == 0) ? ( mapNames.size() / 3) :  ( mapNames.size() / 3) + 1;
        
        int k = 0;
        outerloop:
        for(int i=0; i<nCol; i++)
        {
            for(int j=0; j<nRow; j++)
            {
                if(k >= mapNames.size())
                    break outerloop;

                String mapName = mapNames.get(k);
                k++;

                Button btn = new Button( Toolbox.getMapTitleByFileName(mapName) );
                btn.setPrefWidth(450);
                btn.setPrefHeight(100);
                btn.getStyleClass().add("buttonColor");
                btn.getStyleClass().add("load-a-map-btn");

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override 
                    public void handle(ActionEvent e) {
                        loadMap(mapName);
                    }
                });

                VBox btnWrapper = new VBox();
                btnWrapper.getStyleClass().add("load-a-map-btn-wrapper");
                btnWrapper.getChildren().addAll(btn);

                mapList.add(btnWrapper, i, j);

                btn.setContextMenu(newContextMenu(mapName));  
            }
        }
    }

    private ContextMenu newContextMenu(String mapName)
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItemDelete = new MenuItem("DELETE");
        menuItemDelete.getStyleClass().add("DELETE_MODAL");
        menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                if( Toolbox.confirm("DELETE", "You are about to delete : " + Toolbox.getMapTitleByFileName(mapName) ))
                {
                    CSVParser.removeFile(mapName);
                    populateMapList(); 
                }
            }
        });
        contextMenu.getItems().add(menuItemDelete);
        return contextMenu;
    }

    /**
     * load selected map
     * @param filename
     */
    private void loadMap(String filename){
        Toolbox.setNodes( CSVParser.readFile( Toolbox.PATH_MAPS + filename, ",") );
        // save loaded filename
        Toolbox.loadedFile = filename;
        Toolbox.demo = false;
        showGame();
    }

    @FXML
    void loadFrance(ActionEvent event) {
        
        Toolbox.setNodes( CSVParser.readFile(Toolbox.FRANCE_PATH, ",") );
        Toolbox.loadedFile = "France";
        Toolbox.demo = true;
        showGame();       
    }
    
    @FXML
    void loadItalie(ActionEvent event) {    
        Toolbox.setNodes( CSVParser.readFile(Toolbox.ITALIE_PATH, ",") );
        Toolbox.loadedFile = "Italie";
        Toolbox.demo = true;
        showGame();
    }

    /**
     * show game interface
     */
    private void showGame()
    {
        try {
            
            AnchorPane root = FXMLLoader.load(getClass().getResource("Game.fxml"));   
            ap.setTopAnchor(root,0.0);
            ap.setBottomAnchor(root,0.0);
            ap.setLeftAnchor(root,0.0);
            ap.setRightAnchor(root,0.0);
            ap.getChildren().setAll(root);
            
        } catch (IOException ex) {
            Logger.getLogger(MapDesignerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void openExplorerMapsFolder(ActionEvent event) {

        File file = new File(Toolbox.PATH_MAPS);
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(LoadMapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void refresh(ActionEvent event) {

        populateMapList();
    }
        

    @FXML
    void back(ActionEvent event) {
                        
        try {
            
            AnchorPane root = FXMLLoader.load(getClass().getResource("Home.fxml"));     
            ap.setTopAnchor(root,0.0);
            ap.setBottomAnchor(root,0.0);
            ap.setLeftAnchor(root,0.0);
            ap.setRightAnchor(root,0.0);
            ap.getChildren().setAll(root);
            
        } catch (IOException ex) {
            Logger.getLogger(MapDesignerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}