package alonbd.merosh.TaskLogic;

import java.io.Serializable;

public abstract class Trigger implements Serializable {
    private boolean consumed;
    public Trigger(){
        consumed = false;
    }
    public void setConsumedTrue(){
        consumed = true;
    }
}
