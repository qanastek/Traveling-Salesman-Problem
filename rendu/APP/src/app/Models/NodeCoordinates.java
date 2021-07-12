package app.Models;

public class NodeCoordinates implements CSVParsable {
    
    private int identifier;
    private int x;
    private int y;

    public NodeCoordinates(int identifier, int x, int y) {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
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
    
    public String toCSV(String sep) {
        return this.identifier + sep + this.x + sep + this.y;
    }
    
    public String toCSV() {
        return this.toCSV(",");
    }
        
    @Override
    public String getCSVHeader(String sep) {
        return "identifier" + sep + "x" + sep + "y";
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
