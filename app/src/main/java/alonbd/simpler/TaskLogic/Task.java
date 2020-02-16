package alonbd.simpler.TaskLogic;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {

    private Trigger mTrigger;
    private ArrayList<Action> mActions;
    private String mName;
    private boolean mOnceOnly;
    private Date mDate;

    public Task(Trigger mTrigger, String mName, boolean mOnceOnly, ArrayList<Action> mActions) {
        mDate = new Date();
        this.mName = mName;
        this.mTrigger = mTrigger;
        this.mActions = mActions;
        this.mOnceOnly = mOnceOnly;
    }

    public void start(Context context) {
        if(mTrigger.isUsed() && isOnceOnly()) {
            //action is have been used already
        } else {
            for(Action action :
                    mActions) {
                action.onExecute(context);
            }
            mTrigger.setUsedTrue();
        }
    }

    public String getName() {return mName;}

    public boolean isOnceOnly() {
        return mOnceOnly;
    }

    public boolean isTriggerUsed() {
        return mTrigger.isUsed();
    }

    public String triggerType() {
        return mTrigger.getClass().getSimpleName();
    }

    public boolean triggerMatchIntent(Intent intent) {return mTrigger.matchIntent(intent);}


}

