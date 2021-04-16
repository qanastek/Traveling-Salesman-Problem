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
public class EdgeCoordinates implements CSVParsable {
    
    private int identifier;
    private int from;
    private int to;

    public EdgeCoordinates(int identifier, int from, int to) {
        this.identifier = identifier;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return String.valueOf(identifier);
    }
    
    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
    
    public String toCSV(String sep) {
        return this.identifier + sep + this.from + sep + this.to;
    }
    
    public String toCSV() {
        return this.toCSV(",");
    }
    
    @Override
    public String getCSVHeader(String sep) {
        return "identifier" + sep + "from" + sep + "to";
    }
        
    @Override
    public String getCSVHeader() {
        return getCSVHeader(",");
    }

    @Override
    public String toString() {
        return this.toCSV();
    }
}
