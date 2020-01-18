package alonbd.merosh.TaskLogic;

import android.content.Context;
import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import alonbd.merosh.BackgroundAndroid.TasksManager;

public class Task implements Serializable {
    private Trigger trigger;
    private Action[] actions;

    private String name;
    private Date date;

    public Task(Context context, Trigger trigger, String name, Action... actions) {
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




}

