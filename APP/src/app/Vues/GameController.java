/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Vues;

import TSPModel_PtiDeb.*;
import app.Models.CSVParser;
import app.Models.EdgeCoordinates;
import app.Models.NodeCoordinates;
import app.Models.TaskTSP;
import app.Models.Toolbox;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

/**
 *
 * @author yanis
 */
public class GameController implements Initializable, Observer {
    
    private static final String ACTIVE = "fill-color: rgb(101, 156, 19);";
    private static final String BASE = "fill-color: rgb(144, 148, 138);";

    // Graph
    Graph graph = new SingleGraph("TSP");
    
    // TSP Model
    Thread tspThread;
    TaskTSP tsp = new TaskTSP(this);
//    TSPModel_PtiDeb tsp = new TSPModel_PtiDeb(this);
    
    ArrayList<NodeCoordinates> nodes = new ArrayList<NodeCoordinates>();
    ArrayList<EdgeCoordinates> edges = new ArrayList<EdgeCoordinates>();
    
    private int speed = 1000;
    
    @FXML
    private Text status;
    
    @FXML
    private Text distance;
    
    @FXML
    private Button startBtn;
    
    @FXML
    private Slider sliderSpeed;
    
    @FXML
    private Pane graphArea;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // When the slider value is changed
        sliderSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeSpeed(newValue.intValue());
        });
    }
    
    private void removeStatusClasses() {
        System.out.println("-------------------------- removeStatusClasses");
        status.getStyleClass().clear();
    }
    
    private void setRunning() {
        System.out.println("-------------------------- setRunning");
        status.setText(Toolbox.RUNNING);
        startBtn.setText("PAUSE");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.RUNNING_CSS);
    }
    
    private void setPaused() {
        System.out.println("-------------------------- setPaused");
        status.setText(Toolbox.PAUSED);
        startBtn.setText("START");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.PAUSED_CSS);
    }
    
    private void setStopped() {
        System.out.println("-------------------------- setRunning");
        status.setText(Toolbox.STOPPED);
        startBtn.setText("START");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.STOPPED_CSS);
    }
    
    private void setFinished() {
        System.out.println("-------------------------- setFinished");
        status.setText(Toolbox.FINISHED);
        startBtn.setText("START");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.FINISHED_CSS);
    }
    
    private void changeSpeed(int speed) {
        System.out.println("-------------------------- changeSpeed: " + speed);
        this.speed = speed * 10;
    }
    
    @FXML
    private void toggleStart() {
        
        System.out.println("-------------------------- start");
        
        // If is currently running
        if(status.getText().equals(Toolbox.RUNNING)) {
            
            if(tsp != null) tsp.getTsp().setPause(true);
            
            setPaused();
        }
        else {
            
            tspThread = null;
            
            // Set running view
            setRunning();
            
            // Start
            System.out.println("-------------------------- test new");
            csvParser();
            // Toolbox.save(nodes);
            renderGraph();
            runTSP(nodes);
        }
    }
        
    @FXML
    private void stop() {
        System.out.println("-------------------------- stop");
        setStopped();
        tspThread.stop();
    }
    
    @FXML
    private void restart() {
        System.out.println("-------------------------- restart");
        setStopped();
    }
    
    @FXML
    private void loadMap() {
        System.out.println("-------------------------- loadMap");
    }
    
    @FXML
    private void editMap() {
        System.out.println("-------------------------- editMap");
    }
    
    // Read the csv file and convert it to an workable array of nodes
    private void csvParser() {
        
        System.out.println("Read PATH_NODES_IN");
        this.nodes = CSVParser.readFile(Toolbox.PATH_NODES_IN,",");   
        
        System.out.println("******************************************************");
        System.out.println(nodes.size() );
        System.out.println("******************");

        // Generate full fully connected graph
        generateEdges();
        
        System.out.println(this.edges.size());
        System.out.println("******************************************************");
        
        System.out.println("All Loaded!");   
    }
    
    // Generate a complete graph
    private void generateEdges() {
        
        int from;
        int to;
        
        // For each node
        for (NodeCoordinates f : nodes) {
            
            from = f.getIdentifier();            
            
            // Connect it with all of this neighboors
            for (NodeCoordinates t : nodes) {
                
                to = t.getIdentifier();
                
                if(from == to) continue;
                                
                // Add tp the edges array
                this.edges.add(new EdgeCoordinates(
                    Toolbox.fromTo(from,to),
                    from,
                    to
                ));
            }
        }
    }
    
    private void runTSP(ArrayList<NodeCoordinates> nodes) {
        
        for(NodeCoordinates node : nodes) {
            
            tsp.getTsp().addPoint(
                node.getIdentifier(),
                node.getX(),
                node.getY()
            );
            
            System.out.println(node);
            System.out.println("aaaa");
        }
        
        System.out.println("-----------------------------");
        
        tspThread = new Thread(tsp);
        Toolbox.THREADS.add(tspThread);
        tspThread.start();
        
        Double totalDistance = tsp.getTsp().getDistanceTotale();
        
        System.out.println(totalDistance);        
        System.out.println("-----------");
    }
    
    // Update the graph edge
    private void updateGraph(TSPModel_PtiDeb.ActionType action, int from, int to) {
        
        System.out.println("- START -");
        
        System.out.println(from);
        System.out.println(to);
        
        System.out.println("identifier");
        String identifier = String.valueOf(Toolbox.fromTo(from,to));
//        String identifier = String.valueOf(from) + String.valueOf(to);
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
//        graph.addAttribute("ui.quality");
//        graph.addAttribute("ui.antialias");
//        graph.setAttribute("ui.stylesheet", "url('C:\\Users\\yanis\\Desktop\\Cours\\Master\\M1\\S2\\Interface Graphique\\TPs\\TP2\\APP\\src\\app\\Vues\\stylesheet.css')");

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
            
//            System.out.println(name);
//            System.out.println(from);
//            System.out.println(to);            
//            System.out.println("/*/*/");
            
            // TODO: Issue with last line if contains 213
            Edge e = graph.addEdge(
                name,
                from,
                to
            );
            
//            e.setAttribute("ui.style", BASE);
        }
        
        System.setProperty("org.graphstream.ui", "swing");        
        FxViewer mView = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        mView.enableAutoLayout();
        FxViewPanel panel = (FxViewPanel) mView.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());        
        this.graphArea.getChildren().add(panel);
    }  

    @Override
    public void update(Observable o, Object arg) {

        System.out.println("-------------------------------------------------------");

        TSPModel_PtiDeb.ActionType action = tsp.getTsp().getAction();
        System.out.println("getAction: " + action);
                
        int identifierID = tsp.getTsp().getSegmentID();
        int identifierP1 = tsp.getTsp().getSegmentP1();
        int identifierP2 = tsp.getTsp().getSegmentP2();
        
        System.out.println("---");
        
        if(action == TSPModel_PtiDeb.ActionType.Finish) {
            System.out.println("Finished");
            System.out.println("getDistanceTotale: " + tsp.getTsp().getDistanceTotale());
            System.out.println("getSegmentDistance: " + tsp.getTsp().getSegmentDistance());
            stop();
            return;
        }
        else {
               
            try {
                tsp.getTsp().setPause(true);
                TimeUnit.MILLISECONDS.sleep(speed);
                tsp.getTsp().setPause(false);
                updateGraph(action, identifierP1, identifierP2);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        
        System.out.println("-------------------------------------------------------");
    }    
}
