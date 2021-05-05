/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Vues;

import app.Models.Toolbox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author yanis
 */
public class MapDesignerController implements Initializable {

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
        
        for (int i = 0; i < Toolbox.DEFAULT_SIZE; i++) {
            
            for (int j = 0; j < Toolbox.DEFAULT_SIZE; j++) {
                
                Pane currentPane = new Pane();
                currentPane.getStyleClass().add(Toolbox.CLASS_GRID_ITEM);
                
                grid.add(currentPane, i, j);
            }
        }
    }
    
    @FXML
    private void save(){
        // get a handle to the stage
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        // do what you have to do
        stage.close();
    }  
    
}
