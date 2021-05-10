/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Models;

import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author yanis
 */
public abstract class CustomEventHandler implements EventHandler<Event> {
    
    public int x;
    public int y;

    public CustomEventHandler(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return "(" + x + "," + y + ")";
    }    
}
