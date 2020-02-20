package alonbd.simpler.TaskLogic;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.core.app.ActivityManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.UI.MainActivity;
import alonbd.simpler.UI.RecyclerViewAdapter;

public class Task implements Serializable {
    private Trigger mTrigger;
    private ArrayList<Action> mActions;
    private String mName;
    private boolean mOnceOnly;
    private Date mDate;
    private static RecyclerViewAdapter mRecyclerViewAdapter;

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
            TasksManager.getInstance(context).saveData();
            if(mRecyclerViewAdapter != null){
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public static void setRecyclerViewAdapter(RecyclerViewAdapter mRecyclerViewAdapter) {
        Task.mRecyclerViewAdapter = mRecyclerViewAdapter;
    }
    public static void removeRecyclerViewAdapter(){
        mRecyclerViewAdapter = null;
    }

    public String getName() {return mName;}

    public boolean isOnceOnly() {
        return mOnceOnly;
    }

    public boolean isTriggerUsed() {
        return mTrigger.isUsed();
    }

    public Class getTriggerClass() {
        return mTrigger.getClass();
    }

    public boolean triggerMatchIntent(Intent intent) {return mTrigger.matchIntent(intent);}
    public boolean triggerMatchLocation(Location location) {return mTrigger.matchLocation(location);}

    public static class DefaultDateComparator implements Comparator<Task> {
        private boolean mOrderAsc;

        @Override
        public int compare(Task o1, Task o2) {
            int t = o1.mDate.compareTo(o2.mDate);
            if(!mOrderAsc){
                t =t*-1;
            }
            return t;
        }

        public DefaultDateComparator(boolean mOrderAsc) {
            this.mOrderAsc = mOrderAsc;
        }
    }
}

