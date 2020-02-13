package alonbd.simpler.TaskLogic;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskBuilder implements Serializable {
    public final static String EXTRA_TAG = "taskBuilderExtra";

    private String mTaskName;
    private Trigger mTrigger;
    private ArrayList<Action> mActions;

    public TaskBuilder() {
        mActions = new ArrayList<>();
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        this.mTaskName = taskName;
    }

    public Trigger getTrigger() {
        return mTrigger;
    }

    public void setTrigger(Trigger trigger) {
        this.mTrigger = trigger;
    }

    public ArrayList<alonbd.simpler.TaskLogic.Action> getmActions() {
        return mActions;
    }

    public void addAction(Action action) {
        mActions.add(action);
    }

    public Task build(){
        return new Task(mTrigger, mTaskName, mActions);
    }
}
