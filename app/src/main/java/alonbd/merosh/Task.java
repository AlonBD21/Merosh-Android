package alonbd.merosh;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private Trigger trigger;
    private Action action;

    private String name;
    private Date date;

    public Task(Trigger trigger, Action action,String name){
        date = new Date();
        this.name = name;
        this.trigger = trigger;
        this.action = action;


        //TODO add to db
    }


}

