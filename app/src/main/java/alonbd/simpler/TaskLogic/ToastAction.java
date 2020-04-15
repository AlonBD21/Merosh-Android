package alonbd.simpler.TaskLogic;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import alonbd.simpler.R;

public class ToastAction implements Action, Serializable {
    private int mDuration;
    private String mTxt;

    public ToastAction(String mTxt, int mDuration) {
        this.mDuration = mDuration;
        this.mTxt = mTxt;
    }

    @Override
    public void onExecute(Context context) {Toast.makeText(context, mTxt, mDuration).show();}

    @Override
    public View getDescriptiveView(Context context) {
        View view = View.inflate(context, R.layout.layout_view_toast, null);
        ((TextView) view.findViewById(R.id.text_tv)).setText(mTxt);

        TextView toStrike = null;
        if(mDuration == Toast.LENGTH_LONG)
            toStrike = view.findViewById(R.id.shorter_tv);
        else if(mDuration == Toast.LENGTH_SHORT)
            toStrike = view.findViewById(R.id.longer_tv);

        if(toStrike != null)
            toStrike.setPaintFlags(toStrike.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        return view;
    }
}
