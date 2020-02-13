package alonbd.simpler.TaskLogic;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {

    private Trigger mTrigger;
    private ArrayList<Action> mActions;
    private String mName;
    private Date mDate;

    public Task(Trigger mTrigger, String mName, ArrayList<Action> mActions) {
        mDate = new Date();
        this.mName = mName;
        this.mTrigger = mTrigger;
        this.mActions = mActions;
    }

    public void start(Context context){
        for (Action action :
                mActions) {
            action.onExecute(context);
        }
    }
    public String getName(){return mName;}
    public String triggerType(){
        return  mTrigger.getClass().getSimpleName();
    }
    public Trigger getTrigger() {return mTrigger;}



}

