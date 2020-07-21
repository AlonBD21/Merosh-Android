package alonbd.simpler.TaskLogic;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import alonbd.simpler.R;

public abstract class Trigger implements Serializable, Comparable<Trigger> {

    private boolean mUsed;
    private boolean mSingleUse;

    public Trigger(boolean mSingleUse) {
        mUsed = false;
        this.mSingleUse = mSingleUse;
    }

    public abstract boolean matchCondition(Object object);

    protected abstract View getTypeDescriptiveView(Context context);

    public boolean isReady() {
        return !(mUsed && mSingleUse);
    }

    public void setUsed() {
        mUsed = true;
    }

    public void setReady() {mUsed = false;}

    public boolean isSingleUse() {
        return mSingleUse;
    }

    public View getDescriptiveView(Context context) {
        View view = View.inflate(context, R.layout.layout_view_trigger, null);
        ((TextView) view.findViewById(R.id.activation_tv)).setText(mSingleUse ? context.getString(R.string.prop_once_only) : context.getString(R.string.prop_reusable));

        String status;
        int color;

        if(!mSingleUse)
            if(isReady()) {
                color = context.getResources().getColor(android.R.color.holo_blue_light);
                status = context.getString(R.string.prop_ready);
            } else {
                color = context.getResources().getColor(android.R.color.holo_orange_light);
                status = context.getString(R.string.prop_cooldown);
            }
        else {
            if(mUsed) {
                color = context.getResources().getColor(android.R.color.holo_red_light);
                status = context.getString(R.string.prop_done);
            } else {
                color = context.getResources().getColor(android.R.color.holo_green_light);
                status = context.getString(R.string.prop_ready);
            }
        }

        ((TextView) view.findViewById(R.id.status_tv)).setText(status);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(color);
        ((ImageView) view.findViewById(R.id.status_iv)).setImageDrawable(shape);

        ((LinearLayout) view).addView(this.getTypeDescriptiveView(context), ((LinearLayout) view).getChildCount());
        return view;
    }

    @Override
    public int compareTo(Trigger o) {
        return this.getStatusNumber() - o.getStatusNumber();
    }

    private int getStatusNumber() {
        if(!mSingleUse) return 3;
        if(isReady()) return 2;
        return 1;
    }
}
