/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Models;

/**
 *
 * @author yanis
 */
public class Toolbox {
    public static final String PATH = "C:\\Users\\yanis\\Desktop\\Cours\\Master\\M1\\S2\\Interface Graphique\\TPs\\TP2\\Test\\";
    public static final String PATH_NODES_IN = PATH + "France.nodes.csv";
    public static final String PATH_NODES_OUT = PATH + "France.nodes.out.csv";
    
    public static int fromTo(int from, int to) {
        return Integer.parseInt("1" + String.valueOf(from) + String.valueOf(to));
    }
}