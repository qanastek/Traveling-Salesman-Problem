/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Vues;

import TSPModel_PtiDeb.*;
import app.APP;
import app.Models.CSVParser;
import app.Models.EdgeCoordinates;
import app.Models.NodeCoordinates;
import app.Models.TaskTSP;
import app.Models.Toolbox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Pair;

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
    
    private static final String ACTIVE = "fill-color: rgb(181, 8, 204);";
    private static final String BASE = "fill-color: rgb(144, 148, 138);";

    // Graph
    Graph graph = new SingleGraph("TSP");
    
//     TSP Model
    Thread tspThread;
    TaskTSP tsp = new TaskTSP(this);
//    TSPModel_PtiDeb tsp = new TSPModel_PtiDeb(this);
    
    ArrayList<EdgeCoordinates> edges = new ArrayList<EdgeCoordinates>();
    
    private int speed = 1000;
    
    @FXML
    private AnchorPane root;
    
    @FXML
    private Text status;
    
    @FXML
    private ImageView statusIcon;
    
    @FXML
    private Text time;
    
    @FXML
    private Text distance;
    
    @FXML
    private Button startBtn;
    
    @FXML
    private Slider sliderSpeed;
    
    @FXML
    private Pane graphArea;
        
    private HashMap<String,Pair<Integer,Integer>> path = new HashMap();
//    private ArrayList<Pair<Integer,Integer>> history = new ArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        generateEdges();
        renderGraph();
        
        // When the slider value is changed
        sliderSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeSpeed(newValue.intValue());
        });
        
        APP.keepAspectRatio1TO1();
//        APP.keepAspectRatio16TO9();
    }
    
    private void removeStatusClasses() {
        System.out.println("-------------------------- removeStatusClasses");
        status.getStyleClass().clear();
    }
    
    private void setRunning() {
        System.out.println("-------------------------- setRunning");
        status.setText(Toolbox.RUNNING);
        
        statusIcon.setImage(
            new Image(GameController.class.getResourceAsStream("Icons/pending.png"))
        );
        
        startBtn.setText("PAUSE");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.RUNNING_CSS);
    }
    
    private void setPaused() {
        System.out.println("-------------------------- setPaused");
        status.setText(Toolbox.PAUSED);
        
        statusIcon.setImage(
            new Image(GameController.class.getResourceAsStream("Icons/paused.png"))
        );
        
        startBtn.setText("START");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.PAUSED_CSS);
    }
    
    private void setStopped() {
        System.out.println("-------------------------- setRunning");
        status.setText(Toolbox.STOPPED);
        
        statusIcon.setImage(
            new Image(GameController.class.getResourceAsStream("Icons/stopped.png"))
        );
        
        startBtn.setText("START");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.STOPPED_CSS);
    }
    
    private void setFinished() {
        System.out.println("-------------------------- setFinished");
        status.setText(Toolbox.FINISHED);
        
        statusIcon.setImage(
            new Image(GameController.class.getResourceAsStream("Icons/check.png"))
        );
        
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
            
            if(tsp != null) this.getTsp().setPause(true);
            
            setPaused();
        }
        else {
            
//            tspThread = null;
            
            // Set running view
            setRunning();
            
            // Start
            System.out.println("-------------------------- test new");
//            csvParser();
            // Toolbox.save(nodes);
            runTSP(Toolbox.getNodes());
        }
    }
        
    @FXML
    private void save() {
        System.out.println("-------------------------- save");
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
        Toolbox.departureDate = ZonedDateTime.now();
        setStopped();
    }
    
    @FXML
    private void loadMap() {
        System.out.println("-------------------------- loadMap");
                
        try {
            
            AnchorPane ap = FXMLLoader.load(getClass().getResource("LoadMap.fxml"));     
            root.setTopAnchor(ap,0.0);
            root.setBottomAnchor(ap,0.0);
            root.setLeftAnchor(ap,0.0);
            root.setRightAnchor(ap,0.0);
            root.getChildren().setAll(ap);
            
        } catch (IOException ex) {
            Logger.getLogger(MapDesignerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void editMap() {
        System.out.println("-------------------------- editMap");
        try {
            AnchorPane ap = FXMLLoader.load(getClass().getResource("MapDesigner.fxml"));
            root.setTopAnchor(ap,0.0);
            root.setBottomAnchor(ap,0.0);
            root.setLeftAnchor(ap,0.0);
            root.setRightAnchor(ap,0.0);
            root.getChildren().setAll(ap);
        } 
        catch (IOException ex) {
            Logger.getLogger(MapDesignerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    // Read the csv file and convert it to an workable array of nodes
//    private void csvParser() {
//        
//        System.out.println("Read PATH_NODES_IN");
//        this.nodes = CSVParser.readFile(Toolbox.PATH_NODES_IN,",");   
//        
//        System.out.println("******************************************************");
//        System.out.println(nodes.size() );
//        System.out.println("******************");
//
//        // Generate full fully connected graph
//        generateEdges();
//        
//        System.out.println(this.edges.size());
//        System.out.println("******************************************************");
//        
//        System.out.println("All Loaded!");   
//    }
    
    // Generate a complete graph
    private void generateEdges() {
        
        int from;
        int to;
        
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~NODES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(Toolbox.getNodes());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        // For each node
        for (NodeCoordinates f : Toolbox.getNodes()) {
            
            from = f.getIdentifier();            
            
            // Connect it with all of this neighboors
            for (NodeCoordinates t : Toolbox.getNodes()) {
                
                to = t.getIdentifier();
                
                if(from == to) continue;
                
                System.out.println("from: " + from);
                System.out.println("to: " + to);
                System.out.println("Toolbox: " + Toolbox.fromTo(from,to));
                                
                // Add tp the edges array
                this.edges.add(new EdgeCoordinates(
                    Toolbox.fromTo(from,to),
                    from,
                    to
                ));
            }
        }
        
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~EDGES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(this.edges);
        System.out.println(this.edges.size());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    private void runTSP(ArrayList<NodeCoordinates> nodes) {
        
        for(NodeCoordinates node : nodes) {
            
            this.getTsp().addPoint(
                node.getIdentifier(),
                node.getX(),
                node.getY()
            );
            
            System.out.println(node);
            System.out.println("aaaa");
        }
        
        System.out.println("-----------------------------");
        
        this.startup();
        
        Double totalDistance = this.getTsp().getDistanceTotale();
        
        System.out.println(totalDistance);        
        System.out.println("-----------");
    }
    
    private void startup() {
        
        Toolbox.departureDate = ZonedDateTime.now();
        
        tspThread = new Thread(tsp);
        Toolbox.THREADS.add(tspThread);
        tspThread.start();
        
//        tsp.run();
    }
    
    private TSPModel_PtiDeb getTsp() {
        
        return this.tsp.getTsp();
//        return this.tsp;
    }
    
    // Update the graph edge
    private void updateGraph(TSPModel_PtiDeb.ActionType action, int identifierID, int from, int to) {
        
        System.out.println("- START -");
        
        System.out.println(from);
        System.out.println(to);
        
        System.out.println("identifier");
//        String identifier = String.valueOf(identifierID);
        String identifier = String.valueOf(Toolbox.fromTo(from,to));
        System.out.println(identifier);

        System.out.println("- FETCH -");
        
        Edge AB = graph.getEdge(identifierID);
        System.out.println(AB);

        Node A = graph.getNode(String.valueOf(from));     
        System.out.println(A);

        Node B = graph.getNode(String.valueOf(to));
        System.out.println(B);
        
        System.out.println("- END FETCH -");

        if(action == TSPModel_PtiDeb.ActionType.Remove) {
//            AB.setAttribute("ui.style", BASE);

            // Smallest identifier at the left
//            Pair pair = from < to ? new Pair(from,to) : new Pair(to,from);

            // Remove from the Path HashMap
//            path.remove(identifier);
//            history.add(new Pair(from,to));
            path.put(identifier, new Pair(from,to));

            A.setAttribute("ui.style", BASE);
            B.setAttribute("ui.style", BASE);
            graph.getEdge(identifier).setAttribute("ui.style", BASE);
        }
        else {
//            AB.setAttribute("ui.style", ACTIVE);

            // Add in the Path HashMap
//            history.add(new Pair(from,to));
//            path.put(identifier, new Pair(from,to));
            
            A.setAttribute("ui.style", ACTIVE);
            B.setAttribute("ui.style", ACTIVE);
            graph.getEdge(identifier).setAttribute("ui.style", ACTIVE);
        }
                
        System.out.println("- END UPDATE -");
    }
    
    private void renderGraph() {
        
        graph.setStrict(false);
        graph.setAutoCreate(true);
        // fill-image: url('https://img.icons8.com/ios/452/city.png'); 
        graph.setAttribute("ui.stylesheet", "node { size: 20px; z-index: 1; fill-color: #999; text-alignment: under; text-offset: 0px, 10px;} edge { z-index: 0; fill-color: #333; size: 3px; arrow-shape: arrow;}");

        // Build Nodes
        for(NodeCoordinates node : Toolbox.getNodes()) {
            
            System.out.println(node);
            System.out.println(node.getName());
            
            Node n = graph.addNode(
                node.getName()
            );
            
            n.setAttribute("x", node.getX());
            n.setAttribute("y", node.getY());
            
//            n.setAttribute("xy", node.getX(), node.getY());
            n.setAttribute("ui.label", "VILLE N°" + node.getIdentifier());
            
            n.setAttribute("ui.style", BASE);
//            n.setAttribute("fill-mode", "image-scaled");
//            n.setAttribute("fill-image", "url('https://img.icons8.com/bubbles/2x/city.png');");
        }
        
        // Build Edges
        for(EdgeCoordinates edge : this.edges) {
            
            System.out.println(edge);
            
            String name = edge.getName();
            int from = edge.getFrom();
            int to = edge.getTo();
            
            System.out.println(name);
            System.out.println(from);
            System.out.println(to);            
            System.out.println("/*/*/");
            
//            Edge e = graph.addEdge(
//                name,
//                String.valueOf(from),
//                String.valueOf(to)
//            );
            
            Edge e = graph.addEdge(
                name,
                String.valueOf(from),
                String.valueOf(to),
                true
            );
            
            e.setAttribute("ui.style", BASE);
        }
        
        System.setProperty("org.graphstream.ui", "swing");        
        
//        graph.addAttribute("ui.stylesheet", "node { size: 20px; z-index: 1; fill-color: #999; text-alignment: under; text-offset: 0px, 10px;} edge { z-index: 0; fill-color: #333; size: 3px; arrow-shape: arrow;}");
        
        FxViewer mView = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//        mView.enableAutoLayout();
        mView.disableAutoLayout();
        FxViewPanel panel = (FxViewPanel) mView.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());     
        panel.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);   
//        panel.setPrefSize(Double.MAX_VALUE,Double.MAX_VALUE);
        root.setTopAnchor(panel,0.0);
        root.setBottomAnchor(panel,0.0);
        root.setLeftAnchor(panel,0.0);
        root.setRightAnchor(panel,0.0);  
        this.graphArea.getChildren().add(panel);
    }  
    
    private String getPath() {
        
        String res = "";
        
        // Convert values to an ArrayList
        ArrayList<Pair<Integer,Integer>> arr = new ArrayList(path.values());
        
        // Order by ASC Nodes identifier
        Collections.sort(arr, new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                
                String id1 = "" + p1.getKey() + p1.getValue();
                String id2 = "" + p2.getKey() + p2.getValue();
                
                return id1.compareTo(id2);
            }
        });
        
        System.out.println("*********************************************************");
        System.out.println(arr);
        
        // Compile in a String
        for (Pair<Integer, Integer> vectrex : arr) {
            
            // Edge Identifier
            String identifier = String.valueOf(Toolbox.fromTo(vectrex.getKey(),vectrex.getValue()));
            
            graph.getNode(String.valueOf(vectrex.getKey())).setAttribute("ui.style", ACTIVE);
            graph.getNode(String.valueOf(vectrex.getValue())).setAttribute("ui.style", ACTIVE);
            
            Edge AB = graph.getEdge(identifier);
//            System.out.println(AB);
            AB.setAttribute("ui.style", "fill-color: rgb(0,100,255);");
//            AB.setAttribute("ui.style", "fill-color: rgb(0,100,255); arrow-shape: arrow;");
//            AB.setAttribute("ui.stylesheet", "{arrow-shape: arrow; arrow-size: 10px, 10px;}");
            
            // Add vectrex
            res += vectrex.getKey() + " <--> " + vectrex.getValue() + "\n";
        }
        
        // Debug
        System.out.println(res);
        
        // Return sequence
        return res;
    }

    @Override
    public void update(Observable o, Object arg) {

        System.out.println("-------------------------------------------------------");

        TSPModel_PtiDeb.ActionType action = this.getTsp().getAction();
        System.out.println("getAction: " + action);
                
        int identifierID = this.getTsp().getSegmentID();
        int identifierP1 = this.getTsp().getSegmentP1();
        int identifierP2 = this.getTsp().getSegmentP2();
        
        System.out.println(identifierID);
        System.out.println(identifierP1);
        System.out.println(identifierP2);
        
        Toolbox.arrivalDate = ZonedDateTime.now();
        int duration = Toolbox.getDuration();
        time.setText(String.valueOf(duration) + " sec");
        
        System.out.println("---");
        
        if(action == TSPModel_PtiDeb.ActionType.Finish) {
            System.out.println("Finished");
            System.out.println("getDistanceTotale: " + this.getTsp().getDistanceTotale());
            System.out.println("getSegmentDistance: " + this.getTsp().getSegmentDistance());
            System.out.println(path);
            setFinished();
            getPath();
//            stop();
            return;
        }
        else if(action == TSPModel_PtiDeb.ActionType.NewBest) {
            System.out.println("getDistanceTotale: " + (int) this.getTsp().getDistanceTotale());
            distance.setText((int) this.getTsp().getDistanceTotale() + " KM");            
            path.clear();
        }
        else {
            
            try {
                this.getTsp().setPause(true);
                TimeUnit.MILLISECONDS.sleep(speed);
                this.getTsp().setPause(false);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            updateGraph(action, identifierID, identifierP1, identifierP2);
        }
        
        System.out.println("-------------------------------------------------------");
    }
}
