package alonbd.merosh;

public abstract class Action {
    boolean done;
    public Action(){
        done = true;
    }
    public void act(){
        done = true;
    }
}
