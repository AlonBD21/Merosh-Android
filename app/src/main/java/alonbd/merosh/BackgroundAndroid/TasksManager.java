package alonbd.merosh.BackgroundAndroid;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import alonbd.merosh.TaskLogic.BTTrigger;
import alonbd.merosh.TaskLogic.Task;
import alonbd.merosh.TaskLogic.ToastAction;

//TODO, fix reace conditions with 'synchronized'
public class TasksManager {
    private final static Object SYNCHRO = new Object();
    private final static String FILE = "TasksArrayListFile";
    private ArrayList<Task> data;
    private Context context;
    private static TasksManager instance;

    //Private
    private TasksManager(Context context) {
        this.context = context;
        loadData();
        if (data == null){
            data = new ArrayList<>();
            saveData();
        }
    }

    private void saveData() {
        try {
            FileOutputStream fos = context.openFileOutput(FILE, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Task> loadData(){
        try {
            FileInputStream fis = context.openFileInput(FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (ArrayList<Task>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
    //Public
    public static TasksManager getInstance(Context context) {
        if (instance != null) return instance;
        else {
            instance = new TasksManager(context);
            return instance;
        }
    }
    public void addTask(Task task){
        loadData();
        data.add(task);
        saveData();
    }
    public void removeTaskAt(int index){
        loadData();
        data.remove(index);
        saveData();
    }
}
