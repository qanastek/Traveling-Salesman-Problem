package app.Models;

public interface CSVParsable {
    
    abstract String getCSVHeader(String sep);
    abstract String getCSVHeader();
    
    abstract String toCSV(String sep);
    abstract String toCSV();
    
}
