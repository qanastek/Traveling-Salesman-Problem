/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import app.Models.Toolbox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author yanis
 */
public class APP extends Application {
    
    Scene scene;
    Stage stage;
    
    boolean lockWidth = false;
    boolean lockHeight = false;

    @Override
    public void stop() throws Exception {
        
        for(Thread t : Toolbox.THREADS) {
            t.stop();
        }
        
        super.stop();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        System.setProperty("org.graphstream.ui", "javafx");
        
        Parent root = FXMLLoader.load(getClass().getResource("Vues/Home.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("Vues/Game.fxml"));
        
        scene = new Scene(root);
        this.stage = stage;
        
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(800);
        stage.show();
        
        keepAspectRatio();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void keepAspectRatio() {
        
        try {

            System.out.println("Keep Aspect Ratio Start");
                   
            stage.widthProperty().addListener((obs, oldVal, newVal) -> {

                lockHeight = true;

                if(lockWidth == true) {
                    lockWidth = false;
                    return;
                }
                stage.setHeight(stage.widthProperty().doubleValue());

                lockWidth = false;
           });

           stage.heightProperty().addListener((obs, oldVal, newVal) -> {

                    lockWidth = true;

                    if(lockHeight == true) {
                        lockHeight = false;
                        return;
                    }

                    stage.setWidth(stage.heightProperty().doubleValue());

                    lockHeight = false;
           });

            System.out.println("Keep Aspect Ratio End");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
