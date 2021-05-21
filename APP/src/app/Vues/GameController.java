
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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


public class GameController implements Initializable, Observer {
    
    private static final String ACTIVE = "fill-color: rgb(181, 8, 204);";
    private static final String BASE = "fill-color: rgb(144, 148, 138);";

    // Graph
    Graph graph = new MultiGraph("TSP");
    
//     TSP Model
    Thread tspThread;
    TaskTSP tsp = new TaskTSP(this);
    
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
    
    @FXML
    private Button saveBtn;
    
    @FXML
    private TextField mapTitle;
        
    private HashMap<String,Pair<Integer,Integer>> path = new HashMap();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Toolbox.setMode(root);
        
        generateEdges();
        renderGraph();
        
        // When the slider value is changed
        sliderSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeSpeed(newValue.intValue());
        });
        
        if(Toolbox.loadedFile != null)
        {
            mapTitle.setText( Toolbox.getMapTitle() );
        }
        else
        {
            mapTitle.setText("");
        }

        // a demo map : disable title dans save
        mapTitle.setDisable(Toolbox.demo);
        saveBtn.setVisible(!Toolbox.demo);
    }
    
    private void removeStatusClasses() {
        status.getStyleClass().clear();
    }
    
    private void setRunning() {
        status.setText(Toolbox.RUNNING);
        
        statusIcon.setImage(
            new Image(GameController.class.getResourceAsStream("Icons/pending.png"))
        );
        
        startBtn.setText("PAUSE");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.RUNNING_CSS);
    }
    
    private void setPaused() {
        status.setText(Toolbox.PAUSED);
        
        statusIcon.setImage(
            new Image(GameController.class.getResourceAsStream("Icons/paused.png"))
        );
        
        startBtn.setText("START");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.PAUSED_CSS);
    }
    
    private void setStopped() {
        status.setText(Toolbox.STOPPED);
        
        statusIcon.setImage(
            new Image(GameController.class.getResourceAsStream("Icons/stopped.png"))
        );
        
        startBtn.setText("START");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.STOPPED_CSS);
    }
    
    private void setFinished() {
        status.setText(Toolbox.FINISHED);
        
        statusIcon.setImage(
            new Image(GameController.class.getResourceAsStream("Icons/check.png"))
        );
        
        startBtn.setText("START");
        removeStatusClasses();
        status.getStyleClass().add(Toolbox.FINISHED_CSS);
    }
    
    private void changeSpeed(int speed) {
        this.speed = speed * 10;
    }
    
    @FXML
    private void toggleStart() {

        // If is currently running
        // Pause
        if(status.getText().equals(Toolbox.RUNNING)) {

            if(tsp != null) {
                setPaused();
                getTsp().setPause(true);
            }
        }
        // RESTART
        else if(status.getText().equals(Toolbox.STOPPED)){
            restart();
        }
        // START
        else {

            // Set running view
            setRunning();

            // Start
            if(tspThread != null && tspThread.isAlive()) {
                
                synchronized(getTsp()){
                    getTsp().notify();
                }

                getTsp().setPause(false);
            }
            else {
                runTSP(Toolbox.getNodes());
            }
        }
    }
    
    @FXML
    void save() {

        // make sure name has been specified
        if(mapTitle.getText().equals("")){
            Toolbox.showMessage("Error", "Map name is not specified.", Alert.AlertType.ERROR);
            return;
        }

        // save into csv
        String outFilename = mapTitle.getText() + ".nodes.csv";
        String outFilenamePath = Toolbox.PATH_MAPS + outFilename; 
        CSVParser.removeFile(Toolbox.loadedFile); // remove current csv
        Toolbox.loadedFile = outFilename; //reset loaded value
        CSVParser.writeFile(Toolbox.getNodes(), outFilenamePath , ","); // create new csv

        // show success message
        String successMsg = "Map : '"+ mapTitle.getText() + "' has been saved";
        Toolbox.showMessage("Success", successMsg , Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void stop() {

        setStopped();
        clearPath();
        tspThread.stop();
    }
    
    @FXML
    private void restart() {

        Toolbox.departureDate = ZonedDateTime.now();
        clearPath();
        setRunning();
        generateEdges();
        renderGraph();
        tsp = new TaskTSP(this);
        runTSP(Toolbox.getNodes());
    }
    
    @FXML
    private void loadMap() {
                    
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
    private void back() {
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
    
    @FXML
    private void editMap() {
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
    
    private void clearPath() {
                
        
        // Convert values to an ArrayList
        ArrayList<Pair<Integer,Integer>> arr = getOrderedPath();
        
        // Compile in a String
        for (Pair<Integer, Integer> vectrex : arr) {
            
            // Edge Identifier
            String identifier = String.valueOf(Toolbox.fromTo(vectrex.getKey(),vectrex.getValue()));
            
            graph.getNode(String.valueOf(vectrex.getKey())).setAttribute("ui.style", BASE);
            graph.getNode(String.valueOf(vectrex.getValue())).setAttribute("ui.style", BASE);
            graph.getEdge(identifier).setAttribute("ui.style", BASE);
        }
        
        path.clear();
    }
    
    // Generate a complete graph
    private void generateEdges() {

        int from;
        int to;
        ArrayList<NodeCoordinates> nodes = Toolbox.getNodes();

        // For each node
        for (int i=0; i<nodes.size(); i++) {

            // from = f.getIdentifier();
            from = nodes.get(i).getIdentifier();

            // Connect it with all of this neighboors
            for (int j=i; j<nodes.size(); j++) {

                to = nodes.get(j).getIdentifier();

                String id = String.valueOf(from)+"--"+String.valueOf(to);

                if(from == to)
                    continue;

                // Add tp the edges array
                this.edges.add(new EdgeCoordinates(
                    Toolbox.fromTo(from,to),
                    from,
                    to
                ));
            }
        }
    }
    
    // Load the data in the TSP model
    private void runTSP(ArrayList<NodeCoordinates> nodes) {
    
        // Add points
        for(NodeCoordinates node : nodes) {
            
            this.getTsp().addPoint(
                node.getIdentifier(),
                node.getX(),
                node.getY()
            );
        }
                
        this.startup();
    }
    
    // Start the thread
    private void startup() {
        
        Toolbox.departureDate = ZonedDateTime.now();
        
        tspThread = new Thread(tsp);
        Toolbox.THREADS.clear();
        Toolbox.THREADS.add(tspThread);
        tspThread.start();
    }
    
    private TSPModel_PtiDeb getTsp() {        
        
        if(this.tsp == null) {
            tsp = new TaskTSP(this);
        }        
        
        return this.tsp.getTsp();
    }
    
    // Update the graph edge
    private void updateGraph(TSPModel_PtiDeb.ActionType action, int identifierID, int from, int to) {
        
        String identifier = String.valueOf(Toolbox.fromTo(from,to));
        
        Edge AB = graph.getEdge(identifierID);

        Node A = graph.getNode(String.valueOf(from));    

        Node B = graph.getNode(String.valueOf(to));

        if(action == TSPModel_PtiDeb.ActionType.Remove) {

            path.put(identifier, new Pair(from,to));

            A.setAttribute("ui.style", BASE);
            B.setAttribute("ui.style", BASE);
            graph.getEdge(identifier).setAttribute("ui.style", BASE);
        }
        else {
            A.setAttribute("ui.style", ACTIVE);
            B.setAttribute("ui.style", ACTIVE);
            graph.getEdge(identifier).setAttribute("ui.style", ACTIVE);
        }
    }
    
    private void renderGraph() {
        
        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.setAttribute("ui.stylesheet", "node { size: 20px; z-index: 1; fill-color: #999; text-alignment: under; text-offset: 0px, 10px;} edge { z-index: 0; fill-color: #333; size: 3px; arrow-shape: arrow;}");

        // Build Nodes
        for(NodeCoordinates node : Toolbox.getNodes()) {
                        
            Node n = graph.addNode(
                node.getName()
            );
            
            n.setAttribute("x", node.getX());
            n.setAttribute("y", node.getY());
            n.setAttribute("ui.label", "VILLE N°" + node.getIdentifier());
            
            n.setAttribute("ui.style", BASE);
        }
        
        // Build Edges
        for(EdgeCoordinates edge : this.edges) {
                        
            String name = edge.getName();
            int from = edge.getFrom();
            int to = edge.getTo();
                        
            Edge e = graph.addEdge(
                name,
                String.valueOf(from),
                String.valueOf(to)
            );
            e.setAttribute("ui.style", BASE + "size: 2px;");
        }
        
        System.setProperty("org.graphstream.ui", "swing");        
        
        FxViewer mView = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        mView.disableAutoLayout();
        FxViewPanel panel = (FxViewPanel) mView.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());     
        panel.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);   
        
        root.setTopAnchor(panel,0.0);
        root.setBottomAnchor(panel,0.0);
        root.setLeftAnchor(panel,0.0);
        root.setRightAnchor(panel,0.0);  
        this.graphArea.getChildren().add(panel);
    }  
    
    private ArrayList<Pair<Integer,Integer>> getOrderedPath() {
        
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
        
        return arr;
    }
    
    private String getPath() {
        
        String res = "";
        
        // Convert values to an ArrayList
        ArrayList<Pair<Integer,Integer>> arr = getOrderedPath();        
        
        // Compile in a String
        for (Pair<Integer, Integer> vectrex : arr) {
            
            // Edge Identifier
            String identifier = String.valueOf(Toolbox.fromTo(vectrex.getKey(),vectrex.getValue()));
            
            graph.getNode(String.valueOf(vectrex.getKey())).setAttribute("ui.style", ACTIVE);
            graph.getNode(String.valueOf(vectrex.getValue())).setAttribute("ui.style", ACTIVE);
            
            Edge AB = graph.getEdge(identifier);
            AB.setAttribute("ui.style", "fill-color: rgb(0,100,255);");
            // Add vectrex
            res += vectrex.getKey() + " <--> " + vectrex.getValue() + "\n";
        }
                
        // Return sequence
        return res;
    }

    @Override
    public void update(Observable o, Object arg) {

        TSPModel_PtiDeb.ActionType action = this.getTsp().getAction();
                
        int identifierID = this.getTsp().getSegmentID();
        int identifierP1 = this.getTsp().getSegmentP1();
        int identifierP2 = this.getTsp().getSegmentP2();
                
        Toolbox.arrivalDate = ZonedDateTime.now();
        int duration = Toolbox.getDuration();
        time.setText(String.valueOf(duration) + " sec");
                
        if(action == TSPModel_PtiDeb.ActionType.Finish) {
            setFinished();
            getPath();
            tsp = null;
            return;
        }
        else if(action == TSPModel_PtiDeb.ActionType.NewBest) {
            distance.setText((int) this.getTsp().getDistanceTotale() + " KM");            
            path.clear();
        }
        else {
            
            try {
                TimeUnit.MILLISECONDS.sleep(speed);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            updateGraph(action, identifierID, identifierP1, identifierP2);
        }
        
    }
}
