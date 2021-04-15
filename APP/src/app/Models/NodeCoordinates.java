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
public class NodeCoordinates {
    
    private String identifier;
    private int x;
    private int y;

    public NodeCoordinates(String identifier, int x, int y) {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public String toCSV() {
        return this.identifier + "," + this.x + "," + this.y;
    }
        
    public static String getCSVHeader(String sep) {
        return "identifier" + sep + "x" + sep + "y";
    }
}
