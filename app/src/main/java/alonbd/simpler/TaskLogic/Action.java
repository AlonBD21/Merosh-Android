package alonbd.simpler.TaskLogic;

import android.content.Context;

import java.io.Serializable;

public interface Action extends Serializable {
public static String[] types = {"Notification", "Toast Message"};

void onExecute(Context context);
ActionType getActionType();
enum ActionType {
    Toast,
       Notification;
}
}

