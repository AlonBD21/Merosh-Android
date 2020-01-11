package alonbd.merosh.TaskLogic;

public abstract class Trigger {
    private boolean consumed;
    public Trigger(){
        consumed = false;
    }
    public void setConsumedTrue(){
        consumed = true;
    }
}
