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
public interface CSVParsable {
    
    abstract String getCSVHeader(String sep);
    abstract String getCSVHeader();
    
    abstract String toCSV(String sep);
    abstract String toCSV();
    
}
