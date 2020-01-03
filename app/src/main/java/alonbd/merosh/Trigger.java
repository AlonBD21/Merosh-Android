package alonbd.merosh;

public abstract class Trigger {
    private boolean consumed;
    public Trigger(){
        consumed = false;
    }
    public void SetConsumedTrue(){
        consumed = true;
    }
}
