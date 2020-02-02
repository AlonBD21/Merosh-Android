package alonbd.simpler.TaskLogic;

import android.content.Context;

public interface Action {
public static String[] types = {"Notification", "Toast Message"};

void onExecute(Context context);

enum ActionType {
    Toast,
       Notification;
}
}

