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
        
        // Save in the csv file
        ArrayList<NodeCoordinates> values = new ArrayList(points.values());
        Toolbox.save(values);
                
        try {
            
            AnchorPane root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));       
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
