package alonbd.simpler.TaskLogic;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskBuilder implements Serializable {
    public final static String EXTRA_TAG = "taskBuilderExtra";
    String taskName;
    Trigger trigger;
    ArrayList<Action> actions;

    public TaskBuilder() {
        actions = new ArrayList<>();
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public ArrayList<alonbd.simpler.TaskLogic.Action> getActions() {
        return actions;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public Task build(){
        return new Task(trigger,taskName,actions);
    }
}
