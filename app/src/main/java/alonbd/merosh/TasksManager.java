package alonbd.merosh;


import android.content.Context;
import java.util.ArrayList;

public class TasksManager {
    private final static Object SYNCHRO = new Object();
    private final static String FILE = "TasksArrayListFile";
    private ArrayList<Task> data;
    private Context context;
    private static TasksManager instance;



    public static TasksManager getInstance(Context context){
        if (instance != null) return instance;
        else{
            instance = new TasksManager(context);
            return instance;
        }
    }
    private TasksManager(Context context){
        this.context = context;
    }


}
