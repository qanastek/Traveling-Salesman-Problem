/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Vues;

import app.Models.CustomEventHandler;
import app.Models.NodeCoordinates;
import app.Models.Toolbox;
import com.sun.javafx.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;

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
        
        keepAspectRatio();

//
//        ap.heightProperty().addListener(new ChangeListener<Object>() {
//                @Override
//                public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
//                }			
//        });
        
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
    
    private void keepAspectRatio() {
        
//        System.out.println("Keep Aspect Ratio Start");
//        Stage stage = (Stage) ap.getScene().getWindow();
//
//        stage.minWidthProperty().bind(stage.heightProperty());
//        stage.minHeightProperty().bind(stage.widthProperty());
//        
//        stage.maxWidthProperty().bind(stage.heightProperty());
//        stage.maxHeightProperty().bind(stage.widthProperty());
//        
////        stage.prefWidthProperty().bind(stage.heightProperty());
////        stage.prefHeightProperty().bind(stage.widthProperty());
//        
//        System.out.println("Keep Aspect Ratio End");
    }
    
    @FXML
    private void save(){
        
        // Save in the csv file
        ArrayList<NodeCoordinates> values = new ArrayList(points.values());
        Toolbox.save(values);
                
        try {
            
            AnchorPane root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));       
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
