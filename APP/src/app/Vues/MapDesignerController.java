/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Vues;

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

/**
 * FXML Controller class
 *
 * @author yanis
 */
public class MapDesignerController implements Initializable {

    @FXML
    private AnchorPane ap;
    
    @FXML
    private Button saveBtn;
    
    @FXML
    private Button deleteBtn;
    
    @FXML
    private GridPane grid;
    
    private HashMap<String,NodeCoordinates> points = new HashMap<String,NodeCoordinates>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//
//        ap.heightProperty().addListener(new ChangeListener<Object>() {
//                @Override
//                public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
//                }			
//        });
        
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
                        
                        System.out.println(getText());
                        
                        // Ajout
                        if(!currentPane.getStyleClass().contains(Toolbox.CLASS_GRID_ITEM_FILLED)) {
                            
                            currentPane.getStyleClass().remove(Toolbox.CLASS_GRID_ITEM);
                            currentPane.getStyleClass().add(Toolbox.CLASS_GRID_ITEM_FILLED);
                            
                            Toolbox.GENERATED_ID++;
                            
                            points.put("" + x + y, new NodeCoordinates(Toolbox.GENERATED_ID,x,y));
                        }
                        else {                  
                            
                            currentPane.getStyleClass().add(Toolbox.CLASS_GRID_ITEM);
                            currentPane.getStyleClass().remove(Toolbox.CLASS_GRID_ITEM_FILLED);
                            
                            points.remove("" + x + y);
                        }
                        
                        System.out.println(points.values());
                    }
                });
                
                grid.add(currentPane, i, j);
            }
        }
    }
    
    @FXML
    private void save(){
        
        // If empty
        if(points.values().size() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aucune ville de posée!");
            alert.setHeaderText("Aucune ville n'a était posée!");
            alert.setContentText("Cliquer sur les cadrillage pour poser votre première ville.");
            alert.showAndWait();
            return;
        }
        
        // Save in the csv file
        ArrayList<NodeCoordinates> values = new ArrayList(points.values());
        Toolbox.save(values);
                
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
       
        System.out.println("back");
                
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
        points.clear();
        
        // Remove GUI Filled Elements
        for(Node n : grid.getChildren()) {
            
            Pane currentPane = (Pane) n;
            
            currentPane.getStyleClass().add(Toolbox.CLASS_GRID_ITEM);
            currentPane.getStyleClass().remove(Toolbox.CLASS_GRID_ITEM_FILLED);            
        }
    }    
}
