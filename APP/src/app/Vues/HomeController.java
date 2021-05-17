/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Vues;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author yanis
 */
public class HomeController implements Initializable {
    
    @FXML
    private AnchorPane ap;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
        
    @FXML
    private void openMapDesigner() {
                
        try {
            
            AnchorPane root = FXMLLoader.load(getClass().getResource("MapDesigner.fxml"));     
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
    private void openMapLoader() {
                
        try {
            
            AnchorPane root = FXMLLoader.load(getClass().getResource("LoadMap.fxml"));     
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
