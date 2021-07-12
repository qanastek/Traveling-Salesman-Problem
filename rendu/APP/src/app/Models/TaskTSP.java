
package app.Models;

import TSPModel_PtiDeb.TSPModel_PtiDeb;
import java.util.Observer;
import javafx.concurrent.Task;

public class TaskTSP extends Task<Void> {
    
    Observer obs;

    TSPModel_PtiDeb tsp;
    
    public TaskTSP(Observer o) {
        obs = o;
        this.tsp = new TSPModel_PtiDeb(o);
    }
    
    public TSPModel_PtiDeb getTsp() {
        return tsp;
    }
    
    public void setPause(Boolean status) {
        this.tsp.setPause(status);
    }
    
    @Override
    protected Void call() throws Exception {
        this.tsp.run();
        return null;
    }    

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }
    

    @Override
    protected void succeeded() {
        
        System.out.println("Remove observer!");
        tsp.deleteObservers();
        
        super.done();
    }
}
