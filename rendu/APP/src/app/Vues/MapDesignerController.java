
package app.Vues;

import app.APP;
import app.Models.CustomEventHandler;
import app.Models.NodeCoordinates;
import app.Models.Toolbox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;


public class MapDesignerController implements Initializable {

    @FXML
    private AnchorPane ap;
    
    @FXML
    private Button saveBtn;
    
    @FXML
    private Button deleteBtn;
    
    @FXML
    private GridPane grid;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Toolbox.setMode(ap);
        // Add Columns
        for (int i = 0; i < Toolbox.DEFAULT_SIZE; i++) {            
            ColumnConstraints cc = new ColumnConstraints();
            cc.setMinWidth(10);
            cc.setPrefWidth(100);            
            cc.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(cc);
        }

        // Add Rows
        for (int j = 0; j < Toolbox.DEFAULT_SIZE; j++) {          
            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(10);
            rc.setPrefHeight(30);            
            rc.setVgrow(Priority.SOMETIMES);
            grid.getRowConstraints().add(rc);
        }
        
        for (int i = 0; i < Toolbox.DEFAULT_SIZE; i++) {
            
            for (int j = 0; j < Toolbox.DEFAULT_SIZE; j++) {
                
                Pane currentPane = new Pane();
                currentPane.getStyleClass().add(Toolbox.CLASS_GRID_ITEM);
                
                currentPane.setOnMousePressed(new CustomEventHandler(i,j) {

                    @Override
                    public void handle(Event event) {
                                                
                        String nodeId = "" + x + y;

                        // add point
                        if(!currentPane.getStyleClass().contains(Toolbox.CLASS_GRID_ITEM_FILLED)) {

                            setNodePane(currentPane);
                            
                            Toolbox.GENERATED_ID++;
                            
                            Toolbox.points.put(nodeId, new NodeCoordinates(Toolbox.GENERATED_ID,x,y));
                        }
                        // remove point
                        else {                  
                            
                            clearNodePane(currentPane);

                            Toolbox.points.remove(nodeId);
                        }
                    }
                });
                
                // populate pane
                populate(currentPane,i,j);
                
                // append pane into grid
                grid.add(currentPane, i, j);

            }
        }
    }
    
    @FXML
    private void save(){
        
        // If empty
        if(Toolbox.getNodes().size() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucune ville de posée!");
            alert.setHeaderText("Aucune ville n'a était posée!");
            alert.setContentText("Cliquer sur les cadrillage pour poser votre première ville.");
            alert.showAndWait();
            return;
        }
        
        // Save in the csv file
        ArrayList<NodeCoordinates> values = new ArrayList(Toolbox.getNodes());
        
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
    private void back(){ 
        
        // Delete Items
        deleteAll();          
                
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
        
    @FXML
    private void deleteAll(){
        
        // Clear the HashMap
        Toolbox.points.clear();
        
        // Clear View
        removeCitiesScreen();        
    }    
    
    private void removeCitiesScreen() {
               
        // Remove GUI Filled Elements
        for(Node n : grid.getChildren()) {
            
            Pane currentPane = (Pane) n;
            
            currentPane.getStyleClass().add(Toolbox.CLASS_GRID_ITEM);
            currentPane.getStyleClass().remove(Toolbox.CLASS_GRID_ITEM_FILLED);            
        }
    }
    
    /**
     * populate pane
     * @param pane
     */
    private void populate(Pane pane, int x, int y)
    {
        String id = ""+ x + y;
        if(Toolbox.points.get(id) != null)
            setNodePane(pane);
        else
            clearNodePane(pane);
    }
    
    /**
     * set node pane : add point design
     * @param pane
     */
    private void setNodePane(Pane pane)
    {
        pane.getStyleClass().remove(Toolbox.CLASS_GRID_ITEM);
        pane.getStyleClass().add(Toolbox.CLASS_GRID_ITEM_FILLED);
    }

    /**
     * clear node pane : remove point design
     * @param pane
     */
    private void clearNodePane(Pane pane)
    {
        pane.getStyleClass().add(Toolbox.CLASS_GRID_ITEM);
        pane.getStyleClass().remove(Toolbox.CLASS_GRID_ITEM_FILLED);
    }
}
