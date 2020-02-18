package alonbd.simpler.TaskLogic;

import android.content.Context;

import java.io.Serializable;

public interface Action extends Serializable {
    String ACTION_EXTRA_CLASS = "actionExtraClass";
    void onExecute(Context context);
}

