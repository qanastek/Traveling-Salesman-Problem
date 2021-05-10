/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author yanis
 */
public class CSVParser {
    
    public static ArrayList<NodeCoordinates> readFile(String path, String sep) {
        
        File file = new File(path);

        ArrayList<NodeCoordinates> result = new ArrayList();

        try {
            
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            // Jump the header
            br.readLine();
            
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                
                String[] splitted = line.split(sep);
                
                result.add(
                    new NodeCoordinates(
                        Integer.parseInt(splitted[0]),
                        Integer.parseInt(splitted[1]),
                        Integer.parseInt(splitted[2])
                    )
                );
            }

            br.close();
            fr.close();
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }
    
    public static void writeFile(ArrayList<NodeCoordinates> items, String path, String sep) {
        
        try {
            
            // Check if created
            if (new File(path).createNewFile()) {
              System.out.println("File created at " + path + " !");
            } else {
              System.out.println("File already exists.");
            }
          
            // The output stream
            FileWriter output = new FileWriter(path);
            
            // Write the header
            output.write(items.get(0).getCSVHeader(sep) + "\n");
            
            // Write each Nodes
            for (NodeCoordinates item : items) {
                
                String csv = item.toCSV() + "\n";
                
                output.write(csv);
            }
            
            // Close the stream
            output.close();
                    
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
        
    }
}
