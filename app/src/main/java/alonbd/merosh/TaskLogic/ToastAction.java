package alonbd.merosh.TaskLogic;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;

public class ToastAction implements Action, Serializable {
    ;
    int duration;
    String txt;

    public ToastAction(String txt, int duration) {
        this.duration = duration;
        this.txt = txt;
    }

    @Override
    public void onExecute(Context context) {Toast.makeText(context, txt, duration).show();}

}
