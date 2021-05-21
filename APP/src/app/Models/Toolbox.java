/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Models;

import app.Vues.GameController;
import java.io.File;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author yanis
 */
public class Toolbox {
    
    public static String APP_NAME = "CERI_TSP";
    
    public static String loadedFile;
    public static Boolean demo = false;
    
    public static final String PATH_APP = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/" + APP_NAME + "/";
    public static final String PATH = PATH_APP + "Data/";
    public static final String PATH_DEMOS = PATH + "Demos/";
    public static final String PATH_MAPS = PATH + "Maps/";
//    public static final String PATH = Toolbox.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/Data/";
    public static final String FRANCE_PATH = PATH_DEMOS + "France.nodes.csv";
    public static final String ITALIE_PATH = PATH_DEMOS + "Italie.nodes.csv";
    
    public static final int DEFAULT_SIZE = 8;
    
    public static final String CLASS_GRID_ITEM = "background_tile";
    public static final String CLASS_GRID_ITEM_FILLED = "background_tile_filled";
    
    // ------------------ STATUS ------------------
    public static final String RUNNING = "RUNNING";
    public static final String RUNNING_CSS = "RUNNING_COLOR";
    
    public static final String PAUSED = "PAUSED";
    public static final String PAUSED_CSS = "PAUSED_COLOR";
    
    public static final String STOPPED = "STOPPED";
    public static final String STOPPED_CSS = "STOPPED_COLOR";
    
    public static final String FINISHED = "FINISHED";
    public static final String FINISHED_CSS = "FINISHED_COLOR";
    
    public static ArrayList<Thread> THREADS = new ArrayList();
    
    public static int GENERATED_ID = 0;
    
    public static ZonedDateTime departureDate;
    public static ZonedDateTime arrivalDate;
    
    public static boolean darkMode = false;
    
    public static HashMap<String,NodeCoordinates> points = new HashMap<String,NodeCoordinates>();
    
    public static String fromTo(int from, int to) {
        return "" + from + "-" +  to;
//        return "" + from + "-" +  to;
//        return String.valueOf(from) + "-" + String.valueOf(to);
    }    
    
//    // Save the nodes into a csv file
//    public static void save(ArrayList<NodeCoordinates> nodes) {
//        System.out.println("Start saving...");
//        CSVParser.writeFile(nodes, Toolbox.PATH_NODES_OUT, ",");
//        System.out.println("Saved!");
//    }

    public static int getDuration() {
        
        Duration duration = Duration.between(Toolbox.departureDate, Toolbox.arrivalDate);

        return (int) duration.toMillis() / 1000;
    }
    
    public static ArrayList<NodeCoordinates> getNodes()
    {
        return new ArrayList<NodeCoordinates>(points.values());
    }
      /**
     * set nodes
     * @param nodes
     */
    public static void setNodes(ArrayList<NodeCoordinates> nodes)
    {
        // clear all points first
        points.clear();

        // re-set new points
        for(NodeCoordinates node: nodes)
        {
            int x = node.getX();
            int y = node.getY();
            String nodeId = "" + x + y;
            GENERATED_ID++;
            points.put(nodeId, new NodeCoordinates(GENERATED_ID,x,y));
        }
    }

    //##
    /**
     * get map list name (csv files)
     * @return
     */
    public static ArrayList<String> getMaplist(){
        File folder = new File(Toolbox.PATH_MAPS);
        File[] listOfFiles = folder.listFiles();

        ArrayList<String> mapFileList = new ArrayList<String>();
        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                mapFileList.add( listOfFiles[i].getName() );
            } 
            // else if (listOfFiles[i].isDirectory()) {
            //     System.out.println("Directory " + listOfFiles[i].getName());
            // }
        }
        return mapFileList;
    }

    //##************************  
    public static String getMapTitleByFileName(String filename){
        return filename.split(".nodes")[0];
    }
    public static String getMapTitle(){
        return loadedFile.split(".nodes")[0];
    }

    public static void clearNodes(){
        points.clear();
        loadedFile = null;
    }

    /**
     * show a dialog message
     * @param title
     * @param msg
     * @param type
     */
    public static void showMessage(String title, String msg, Alert.AlertType type){
        
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(msg);
        // alert.setContentText("Cliquer sur les cadrillage pour poser votre premiÃ¨re ville.");
        alert.showAndWait();
    }
    /**
     * show a confirmation dialog
     * @param title
     * @param msg
     * @return
     */
    public static Boolean confirm(String title, String msg)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        return ( result.get() == ButtonType.OK );
    }    
    
    public static void setMode(AnchorPane ap){
        
        ap.getStyleClass().clear();
        
        if(darkMode)
            ap.getStyleClass().add("backgroundColorDark");
        else
            ap.getStyleClass().add("backgroundColor");
    }
}
