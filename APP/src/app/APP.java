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
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author yanis
 */
public class APP extends Application {
    
    public static Scene scene;
    public static Stage stage;
    
    public static boolean lockWidth = false;
    public static boolean lockHeight = false;

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
        APP.stage = stage;
//        APP.stage.getIcons().add(new Image("Vues/Icons/graph.png"));
        APP.stage.getIcons().add(new Image(APP.class.getResourceAsStream("Vues/Icons/graph.png")));
        APP.stage.setTitle("Voyageur de commerce");

        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(800);
        stage.show();
        
        keepAspectRatio1TO1();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void keepAspectRatio1TO1() {
        
        try {

            System.out.println("Keep Aspect Ratio 1/1 Start");
                   
            APP.clearAspectRatio();
            APP.stage.setWidth(768);
            APP.stage.setHeight(768);
            
            APP.stage.widthProperty().addListener((obs, oldVal, newVal) -> {

                lockHeight = true;

                if(lockWidth == true) {
                    lockWidth = false;
                    return;
                }
                APP.stage.setHeight(APP.stage.widthProperty().doubleValue());

                lockWidth = false;
           });

           APP.stage.heightProperty().addListener((obs, oldVal, newVal) -> {

                    lockWidth = true;

                    if(lockHeight == true) {
                        lockHeight = false;
                        return;
                    }

                    APP.stage.setWidth(APP.stage.heightProperty().doubleValue());

                    lockHeight = false;
           });
                   
            System.out.println("Keep Aspect Ratio End");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }  
    
    public static void keepAspectRatio16TO9() {
        
        try {

            System.out.println("Keep Aspect Ratio 16/9 Start");
            
            APP.clearAspectRatio();
                   
            APP.stage.setWidth(768);
            APP.stage.setHeight(432);
            
            APP.stage.widthProperty().addListener((obs, oldVal, newVal) -> {

                lockHeight = true;

                if(lockWidth == true) {
                    lockWidth = false;
                    return;
                }
                APP.stage.setHeight(APP.stage.widthProperty().doubleValue() / 1.777);

                lockWidth = false;
           });

           APP.stage.heightProperty().addListener((obs, oldVal, newVal) -> {

                    lockWidth = true;

                    if(lockHeight == true) {
                        lockHeight = false;
                        return;
                    }

                    APP.stage.setWidth(APP.stage.heightProperty().doubleValue() * 1.777);

                    lockHeight = false;
           });

            System.out.println("Keep Aspect Ratio End");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }    
    
    
    public static void clearAspectRatio() {
        
        try {
                   
            APP.stage.widthProperty().addListener((obs, oldVal, newVal) -> {
           });

           APP.stage.heightProperty().addListener((obs, oldVal, newVal) -> {
           });
           
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }  
}
