/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Controller;

import TSPModel_PtiDeb.*;
import app.Models.CSVParser;
import app.Models.CSVParserEdges;
import app.Models.EdgeCoordinates;
import app.Models.NodeCoordinates;
import app.Models.Toolbox;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 *
 * @author yanis
 */
public class FXMLDocumentController implements Initializable, Observer {
    
    // Graph
    Graph graph = new SingleGraph("TSP");
    
    // TSP Model
    TSPModel_PtiDeb tsp = new TSPModel_PtiDeb(this);
    
    ArrayList<NodeCoordinates> nodes;
    ArrayList<EdgeCoordinates> edges;
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        System.out.println("-------------------------- test new");
        csvParser();
        // save();
        runTSP(nodes);
        renderGraph();
//        updateGraph();
    }
    
    private void csvParser() {
        
        System.out.println("Read PATH_NODES_IN");
        this.nodes = CSVParser.readFile(Toolbox.PATH_NODES_IN,",");   
        
        System.out.println("Read PATH_EDGES_IN");   
        this.edges = CSVParserEdges.readFile(Toolbox.PATH_EDGES_IN,",");
        
        System.out.println("All Loaded!");   
    }
    
    private void save() {
        CSVParser.writeFile(nodes, Toolbox.PATH_NODES_OUT, ",");
        CSVParserEdges.writeFile(edges, Toolbox.PATH_EDGES_OUT, ",");
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
    
    private void updateGraph() {
        
        // https://graphstream-project.org/doc/Tutorials/Getting-Started/
        
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        
        System.out.println("-----------");
        
        Node A = graph.getNode("A");
        Edge AB = graph.getEdge("AB");
        
        System.out.println(A.getId());
        System.out.println(AB.getId());
        
        System.out.println("-----------");

        for(Node n:graph) {
            System.out.println(n.getId());
        }
    }
    
    private void renderGraph() {
        
        graph.setStrict(false);
        graph.setAutoCreate( true );
        
        // Build Nodes
        for(NodeCoordinates node : nodes) {
            
            System.out.println(node);
            System.out.println(node.getName());
            
            graph.addNode(
                node.getName()
            );
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
            
            Edge AB = graph.getEdge(name);
            Node A = graph.getNode(from);
            Node B = graph.getNode(to);
            
            System.out.println(AB);
            System.out.println(A);
            System.out.println(B);
            
            System.out.println("/*/*/");
            
            // TODO: Issue with last line if contains 213
            graph.addEdge(
                name,
                from,
                to
            );
        }
        
        System.setProperty("org.graphstream.ui", "swing"); 
        
        graph.display();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("-------------------------------------------------------");
        System.out.println("getAction: " + tsp.getAction());
        System.out.println("getDistanceTotale: " + tsp.getDistanceTotale());
        System.out.println("getSegmentDistance: " + tsp.getSegmentDistance());
        System.out.println("getSegmentID: " + tsp.getSegmentID());
        System.out.println("getSegmentP1: " + tsp.getSegmentP1());
        System.out.println("getSegmentP2: " + tsp.getSegmentP2());
        System.out.println("-------------------------------------------------------");
    }    
}
