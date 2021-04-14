/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import TSPModel_PtiDeb.*;
import java.net.URL;
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
    
    Graph graph = new SingleGraph("Tutorial 1");
    
    TSPModel_PtiDeb tsp = new TSPModel_PtiDeb(this);
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        runTSP();
        updateGraph();
    }
    
    private void runTSP() {
        
        tsp.addPoint(1, 1, 1);
        tsp.addPoint(2, 2, 2);
        tsp.addPoint(3, 3, 3);
        tsp.addPoint(4, 4, 4);
        
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        graph.setStrict(false);
        graph.setAutoCreate( true );
        
        graph.addNode("A" );
        graph.addNode("B" );
        graph.addNode("C" );
        
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        
        System.setProperty("org.graphstream.ui", "swing"); 
        
        graph.display();
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
