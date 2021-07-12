
package app;

import app.Models.Toolbox;
import java.io.File;
import java.util.Observable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;


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
        
        scene = new Scene(root);
        APP.stage = stage;
        APP.stage.getIcons().add(new Image(APP.class.getResourceAsStream("Vues/Icons/graph.png")));
        APP.stage.setTitle("Voyageur de commerce");

        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(800);
        stage.show();
        
        createAppFolder();
        
        keepAspectRatio1TO1();
    }
    
    private void createAppFolder() {        
        createFolder(Toolbox.PATH_APP);
        createFolder(Toolbox.PATH);
        createFolder(Toolbox.PATH_DEMOS);
        createFolder(Toolbox.PATH_MAPS);
    }
    
    private void createFolder(String path) {
        
        File file = new File(path);
        
        if (!file.exists()) {
            
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void keepAspectRatio1TO1() {
        
        try {
                   
            Screen smallestScreen = Screen.getPrimary();
            Double smallestHeight = smallestScreen == null ? 768 : smallestScreen.getBounds().getHeight() * 0.85;
            Double minHeight = smallestScreen == null ? 768 : smallestScreen.getBounds().getHeight() * 0.65;
            
            APP.clearAspectRatio();
            APP.stage.setWidth(smallestHeight);
            APP.stage.setHeight(smallestHeight);
            
            APP.stage.setMinWidth(minHeight);
            APP.stage.setMinHeight(minHeight);
            
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
                   
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }  
    
    public static void keepAspectRatio16TO9() {
        
        try {
            
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
