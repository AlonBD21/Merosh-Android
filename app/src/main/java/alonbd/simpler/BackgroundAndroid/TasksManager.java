package alonbd.simpler.BackgroundAndroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import alonbd.simpler.TaskLogic.LocationTrigger;
import alonbd.simpler.TaskLogic.Task;
public class TasksManager {
    private final static String TAG = "ThugTasksManager";
    //Constants
    private final static Object SYNCHRO = new Object();
    private final static String FILE = "TasksArrayListFile";
    //vars
    private ArrayList<Task> mData;
    private Context mContext;
    private static TasksManager sInstance;

    //Methods
    private TasksManager(Context context) {
        this.mContext = context;
        loadData();
        if(mData == null) {
            mData = new ArrayList<>();
        }
    }

    public synchronized void saveData() {
        Log.d(TAG, "saveData: ");
        try {
            FileOutputStream fos = mContext.openFileOutput(FILE, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mData);
            oos.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void loadData() {
        Log.d(TAG, "loadData: ");
        try {
            FileInputStream fis = mContext.openFileInput(FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mData = (ArrayList<Task>) ois.readObject();
            Collections.sort(mData, new Task.DefaultDateComparator(true));
            ois.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getData() {
        return mData;
    }

    public void setData(ArrayList<Task> mData) {
        if(mData != null) {
            this.mData = mData;
            saveData();
        }else{
            Log.d(TAG, "setData: cant set data to a null pointer.");
        }
    }

    public static TasksManager getInstance(Context context) {
        if(sInstance != null) return sInstance;
        else {
            sInstance = new TasksManager(context);
            return sInstance;
        }
    }

    public static boolean startAllWithIntent(Context context, Intent intent) {
        boolean ret = false;
        ArrayList<Task> data = getInstance(context).mData;
        for(Task t : data) {
            if(t.triggerMatchIntent(intent)) {
                t.start(context);
                ret = true;
            }
        }
        return ret;
    }

    public static boolean startAllWithLocation(Context context, Location location) {
        boolean ret = false;
        ArrayList<Task> data = getInstance(context).mData;
        for(Task t : data) {
            if(t.triggerMatchLocation(location)) {
                t.start(context);
                ret = true;
            }
        }
        return ret;
    }

    public String getLocationTasksString() {
        StringBuilder builder = new StringBuilder();
        ArrayList<Task> tmp = new ArrayList<>();
        for(Task task : mData) {
            if(task.getTriggerClass() == LocationTrigger.class) {
                if(!(task.isOnceOnly() && task.isTriggerUsed()))
                    tmp.add(task);
            }
        }
        if(tmp.size() == 0) return null;
        for(int i = 0; i < tmp.size(); i++) {
            if(i == 0) {
                builder.append(tmp.get(i).getName());
            } else if(i == tmp.size() - 1) {
                builder.append(" & " + tmp.get(i).getName());
            } else {
                builder.append(" " + tmp.get(i).getName());
            }
        }
        return builder.toString();
    }

    public static class NotificationIdGenerator {
        private static String SP_NAME = "NotificationIdGenerator";
        private static String COUNTER_KEY = "CounterKey";
        private static SharedPreferences sp;

        public static int getNewId(Context context) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            int counter = sp.getInt(COUNTER_KEY, 777);
            counter++;
            sp.edit().putInt(COUNTER_KEY, counter).commit();
            Log.d(TAG, "getNewId: New notification id generated: " + counter);
            return counter;
        }
    }
}
