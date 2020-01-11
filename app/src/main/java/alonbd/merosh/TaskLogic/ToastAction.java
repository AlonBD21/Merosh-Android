package alonbd.merosh.TaskLogic;

import android.content.Context;
import android.widget.Toast;

public class ToastAction implements Action {
    private String msg;
    private boolean quick;

    public ToastAction(String msg, boolean quick) {
        this.msg = msg;
        this.quick = quick;
    }

    @Override
    public void onExecute(Context context) {
        Toast.makeText(context,msg,quick ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
    }
}
