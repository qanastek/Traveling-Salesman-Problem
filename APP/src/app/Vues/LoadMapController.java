package app.Vues;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class LoadMapController {
    
    @FXML
    AnchorPane ap;
    
    @FXML
    void loadFrance(ActionEvent event) {
        System.out.println("loadFrance");
    }

    @FXML
    void loadEiffelTower(ActionEvent event) {
        System.out.println("loadEiffelTower");
    }

    @FXML
    void loadFile(ActionEvent event) {
        System.out.println("loadFile");
    }

    @FXML
    void back(ActionEvent event) {
        
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
}
