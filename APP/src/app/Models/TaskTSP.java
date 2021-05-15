/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Models;

import TSPModel_PtiDeb.TSPModel_PtiDeb;
import java.util.Observer;
import javafx.concurrent.Task;

/**
 *
 * @author yanis
 */
public class TaskTSP extends Task<Void> {

    TSPModel_PtiDeb tsp;
    
    public TaskTSP(Observer o) {
        this.tsp = new TSPModel_PtiDeb(o);
    }
    
    public TSPModel_PtiDeb getTsp() {
        return tsp;
    }
    
    @Override
    protected Void call() throws Exception {
        this.tsp.run();
        return null;
    }    
}
