package alonbd.simpler.UI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.print.PrintAttributes;
import android.view.View;
import android.widget.Checkable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.DrawableUtils;

import alonbd.simpler.R;

public class ColorOption extends AppCompatImageView implements Checkable {
    private int mColor;
    private boolean mChecked;


    public ColorOption(Context context, int color){
        super(context);
        mColor = color;
        mChecked = false;
        updateState();
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        updateState();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        updateState();
    }

    private void updateState(){
        if(!mChecked){
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(mColor);
            setBackground(shape);
        }else{
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(mColor);
            shape.setStroke(10, getContext().getResources().getColor(R.color.secondaryColor));
            setBackground(shape);
        }
    }

    public int getColor(){return mColor;}
}
