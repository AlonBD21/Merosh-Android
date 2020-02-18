package alonbd.simpler.TaskLogic;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;

public class ToastAction implements Action, Serializable {
    private int mDuration;
    private String mTxt;

    public ToastAction(String mTxt, int mDuration) {
        this.mDuration = mDuration;
        this.mTxt = mTxt;
    }

    @Override
    public void onExecute(Context context) {Toast.makeText(context, mTxt, mDuration).show();}

}
