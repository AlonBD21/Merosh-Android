package alonbd.simpler.TaskLogic;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import alonbd.simpler.R;

public abstract class Trigger implements Serializable {
    private boolean mUsed;

    public abstract boolean matchIntent(Intent intent);

    public abstract boolean matchLocation(Location location);

    public Trigger() {
        mUsed = false;
    }

    protected abstract View getTypeDescriptiveView(Context context);

    public boolean isUsed() {
        return mUsed;
    }

    public void setUsedTrue() {
        mUsed = true;
    }

    public void setReady() {mUsed = false;}

    public View getDescriptiveView(Context context, boolean onlyOnce) {
        View view = View.inflate(context, R.layout.layout_view_trigger, null);
        ((TextView) view.findViewById(R.id.activation_tv)).setText(onlyOnce ? "One Time" : "Reusable");
        ((TextView) view.findViewById(R.id.status_tv)).setText(mUsed ? "Used Already" : "Ready");
        view.findViewById(R.id.status_tv).setVisibility(onlyOnce ? View.VISIBLE : View.INVISIBLE);

        int color;
        if(!onlyOnce)
            color = context.getResources().getColor(android.R.color.holo_blue_light);
        else {
            if(isUsed()) color = context.getResources().getColor(android.R.color.holo_red_light);
            else
                color = context.getResources().getColor(android.R.color.holo_green_light);
        }

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(color);
        ((ImageView) view.findViewById(R.id.status_iv)).setImageDrawable(shape);

        ((LinearLayout) view).addView(this.getTypeDescriptiveView(context), ((LinearLayout) view).getChildCount());
        return view;
    }


}
