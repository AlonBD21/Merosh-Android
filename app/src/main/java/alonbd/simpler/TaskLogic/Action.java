package alonbd.simpler.TaskLogic;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

public interface Action extends Serializable {
    String ACTION_EXTRA_CLASS_TYPE = "actionExtraClass";
    void onExecute(Context context);

    View getDescriptiveView(Context context);
}

