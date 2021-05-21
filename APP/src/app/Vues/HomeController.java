
package app.Vues;

import app.APP;
import app.Models.Toolbox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;

public class HomeController implements Initializable {
    
    @FXML
    private AnchorPane ap;
    
    @FXML
    private CheckBox darkMode;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Toolbox.setMode(ap);        
        darkMode.setSelected(Toolbox.darkMode);            
    }    
    
    @FXML
    private void setDarkMode()
    {
        Toolbox.darkMode = darkMode.isSelected();
        Toolbox.setMode(ap);
    }
        
    @FXML
    private void openMapDesigner() {
        
        Toolbox.clearNodes();
        Toolbox.demo = false;
        
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
