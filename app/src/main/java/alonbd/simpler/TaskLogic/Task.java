package alonbd.simpler.TaskLogic;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {
    private Trigger trigger;
    private ArrayList<Action> actions;

    private String name;
    private Date date;

    public Task(Trigger trigger, String name, ArrayList<Action> actions) {
        date = new Date();
        this.name = name;
        this.trigger = trigger;
        this.actions = actions;
    }

    public void start(Context context){
        trigger.setConsumedTrue();
        for (Action action :
                actions) {
            action.onExecute(context);
        }
    }
    public String getName(){return  name;}
    public String triggerType(){
        return  trigger.getClass().getSimpleName();
    }
    public Trigger getTrigger() {return trigger;}



}

