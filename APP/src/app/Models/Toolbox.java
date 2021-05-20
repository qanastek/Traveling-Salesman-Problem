/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Models;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author yanis
 */
public class Toolbox {
    
    public static final String PATH = "C:\\Users\\yanis\\Desktop\\Cours\\Master\\M1\\S2\\Interface Graphique\\TPs\\TP2\\Test\\";
    public static final String PATH_NODES_IN = PATH + "France.nodes.csv";
    public static final String PATH_NODES_OUT = PATH + "France.nodes.out.csv";
    
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
    
    public static HashMap<String,NodeCoordinates> points = new HashMap<String,NodeCoordinates>();
    
    public static String fromTo(int from, int to) {
        return "" + from + "-" +  to;
//        return String.valueOf(from) + "-" + String.valueOf(to);
    }    
    
    // Save the nodes into a csv file
    public static void save(ArrayList<NodeCoordinates> nodes) {
        System.out.println("Start saving...");
        CSVParser.writeFile(nodes, Toolbox.PATH_NODES_OUT, ",");
        System.out.println("Saved!");
    }

    public static int getDuration() {
        
        Duration duration = Duration.between(Toolbox.departureDate, Toolbox.arrivalDate);

        return (int) duration.toMillis() / 1000;
    }
    
    public static ArrayList<NodeCoordinates> getNodes()
    {
        return new ArrayList<NodeCoordinates>(points.values());
    }
}
