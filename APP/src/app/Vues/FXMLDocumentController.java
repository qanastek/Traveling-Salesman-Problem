/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Vues;

import TSPModel_PtiDeb.*;
import app.Models.CSVParser;
import app.Models.CSVParserEdges;
import app.Models.EdgeCoordinates;
import app.Models.NodeCoordinates;
import app.Models.Toolbox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 *
 * @author yanis
 */
public class FXMLDocumentController implements Initializable, Observer {
    
    private static final String ACTIVE = "fill-color: rgb(101, 156, 19);";
    private static final String BASE = "fill-color: rgb(144, 148, 138);";

    // Graph
    Graph graph = new SingleGraph("TSP");
    
    // TSP Model
    TSPModel_PtiDeb tsp = new TSPModel_PtiDeb(this);
    
    ArrayList<NodeCoordinates> nodes = new ArrayList<NodeCoordinates>();
    ArrayList<EdgeCoordinates> edges = new ArrayList<EdgeCoordinates>();
    
    @FXML
    private Label label;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        System.out.println("-------------------------- test new");
        csvParser();
        // save();
        renderGraph();
        runTSP(nodes);
    }
    
    @FXML
    private void handlePaneClick() {
        System.out.println("-------------------------- Pane Click");
    }
    
    private void csvParser() {
        
        System.out.println("Read PATH_NODES_IN");
        this.nodes = CSVParser.readFile(Toolbox.PATH_NODES_IN,",");   

        generateEdges();
        
        System.out.println("All Loaded!");   
    }
    
    private void generateEdges() {
        
        int from;
        int to;
        
        for (NodeCoordinates f : nodes) {
            
            from = f.getIdentifier();            
            
            for (NodeCoordinates t : nodes) {
                
                to = t.getIdentifier();
                
                this.edges.add(new EdgeCoordinates(
                    Toolbox.fromTo(from,to),
                    from,
                    to
                ));
            }
        }
    }
    
    private void save() {
        CSVParser.writeFile(nodes, Toolbox.PATH_NODES_OUT, ",");
    }
    
    private void runTSP(ArrayList<NodeCoordinates> nodes) {
        
        for(NodeCoordinates node : nodes) {
            
            tsp.addPoint(
                node.getIdentifier(),
                node.getX(),
                node.getY()
            );
            
            System.out.println(node);
            System.out.println("aaaa");
        }
        
        System.out.println("-----------------------------");
        
        tsp.run();
        
        Double totalDistance = tsp.getDistanceTotale();
        
        System.out.println(totalDistance);        
        System.out.println("-----------");
    }
    
    private void updateGraph(TSPModel_PtiDeb.ActionType action, int from, int to) {
        
        System.out.println("- START -");
        
        System.out.println(from);
        System.out.println(to);
        
        System.out.println("identifier");
        String identifier = String.valueOf(from) + String.valueOf(to);
        System.out.println(identifier);

        System.out.println("- FETCH -");
        
        Edge AB = graph.getEdge(identifier);
        System.out.println(AB);

        Node A = graph.getNode(from);     
        System.out.println(A);

        Node B = graph.getNode(to);
        System.out.println(B);
        
        System.out.println("- END FETCH -");

        if(action == TSPModel_PtiDeb.ActionType.Remove) {
//            AB.setAttribute("ui.style", BASE);
            A.setAttribute("ui.style", BASE);
            B.setAttribute("ui.style", BASE);
        }
        else {
//            AB.setAttribute("ui.style", ACTIVE);
            A.setAttribute("ui.style", ACTIVE);
            B.setAttribute("ui.style", ACTIVE);
        }
                
        System.out.println("- END UPDATE -");
    }
    
    private void renderGraph() {
        
        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.setAttribute("ui.stylesheet", "url('C:\\Users\\yanis\\Desktop\\Cours\\Master\\M1\\S2\\Interface Graphique\\TPs\\TP2\\APP\\src\\app\\Vues\\stylesheet.css')");

        // Build Nodes
        for(NodeCoordinates node : nodes) {
            
            System.out.println(node);
            System.out.println(node.getName());
            
            Node n = graph.addNode(
                node.getName()
            );
            
            n.setAttribute("ui.style", BASE);
//            n.setAttribute("fill-mode", "image-scaled");
//            n.setAttribute("fill-image", "url('https://img.icons8.com/bubbles/2x/city.png');");
        }
        
        System.out.println("*****************************");
//        System.out.println(edges);
        
        // Build Edges
        for(EdgeCoordinates edge : edges) {
            
            System.out.println(edge);
            
            String name = edge.getName();
            int from = edge.getFrom();
            int to = edge.getTo();
            
            System.out.println(name);
            System.out.println(from);
            System.out.println(to);
            
            System.out.println("/*/*/");
            
            // TODO: Issue with last line if contains 213
            Edge e = graph.addEdge(
                name,
                from,
                to
            );
            
//            e.setAttribute("ui.style", BASE);
        }
        
        System.setProperty("org.graphstream.ui", "swing"); 
        
        graph.display();
    }  

    @Override
    public void update(Observable o, Object arg) {

        System.out.println("-------------------------------------------------------");

        TSPModel_PtiDeb.ActionType action = tsp.getAction();
        System.out.println("getAction: " + action);
                
        int identifierID = tsp.getSegmentID();
        int identifierP1 = tsp.getSegmentP1();
        int identifierP2 = tsp.getSegmentP2();
        
        System.out.println("---");
        
        if(action == TSPModel_PtiDeb.ActionType.Finish) {
            System.out.println("Finished");
            System.out.println("getDistanceTotale: " + tsp.getDistanceTotale());
            System.out.println("getSegmentDistance: " + tsp.getSegmentDistance());  
            return;
        }
        else {
               
            try {
                tsp.setPause(true);
                TimeUnit.MILLISECONDS.sleep(1000);
                tsp.setPause(false);
                updateGraph(action, identifierP1, identifierP2);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        
        System.out.println("-------------------------------------------------------");
    }    
}
