/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Models;

import java.util.ArrayList;

/**
 *
 * @author yanis
 */
public class Toolbox {
    
    public static final String PATH = "C:\\Users\\yanis\\Desktop\\Cours\\Master\\M1\\S2\\Interface Graphique\\TPs\\TP2\\Test\\";
    public static final String PATH_NODES_IN = PATH + "France.nodes.csv";
    public static final String PATH_NODES_OUT = PATH + "France.nodes.out.csv";
    
    public static final int DEFAULT_SIZE = 10;
    
    public static final String CLASS_GRID_ITEM = "background_tile";
    public static final String CLASS_GRID_ITEM_FILLED = "background_tile_filled";
    
    public static int GENERATED_ID = 0;
    
    public static int fromTo(int from, int to) {
        return Integer.parseInt("1" + String.valueOf(from) + String.valueOf(to));
    }    
    
    // Save the nodes into a csv file
    public static void save(ArrayList<NodeCoordinates> nodes) {
        System.out.println("Start saving...");
        CSVParser.writeFile(nodes, Toolbox.PATH_NODES_OUT, ",");
        System.out.println("Saved!");
    }
}
