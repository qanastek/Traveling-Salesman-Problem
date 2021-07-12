package app.Models;

import javafx.event.Event;
import javafx.event.EventHandler;

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
