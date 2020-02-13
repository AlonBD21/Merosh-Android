package alonbd.simpler.BackgroundAndroid;


import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import alonbd.simpler.TaskLogic.Task;

//TODO, fix race conditions with 'synchronized'
public class TasksManager {
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
        if (mData == null){
            mData = new ArrayList<>();
            saveData();
        }
    }
    private void saveData() {
        try {
            FileOutputStream fos = mContext.openFileOutput(FILE, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mData);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Task> loadData(){
        try {
            FileInputStream fis = mContext.openFileInput(FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mData = (ArrayList<Task>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mData;
    }
    public static TasksManager getInstance(Context context) {
        if (sInstance != null) return sInstance;
        else {
            sInstance = new TasksManager(context);
            return sInstance;
        }
    }
    public void addTask(Task task){
        loadData();
        mData.add(task);
        saveData();
    }
    public void removeTaskAt(int index){
        loadData();
        mData.remove(index);
        saveData();
    }
}
