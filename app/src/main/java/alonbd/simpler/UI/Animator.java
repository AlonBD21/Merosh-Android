package alonbd.simpler.UI;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public abstract class Animator {
    public static void animatePencil(ImageView iv){
        Animation flip = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.45f);
        flip.setDuration(1200);
        flip.setStartOffset(2400);
        flip.setInterpolator(new AnticipateOvershootInterpolator());
        flip.setRepeatCount(Animation.INFINITE);
        flip.setRepeatMode(Animation.REVERSE);

        TranslateAnimation inAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,0);
        inAnim.setDuration(500);

        setEndListener(inAnim,()->{
           iv.startAnimation(flip);
        });

        iv.startAnimation(inAnim);
    }




    static interface EndListener{
        void onEnd();
    }
    private static void setEndListener(Animation animation,EndListener listener){
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.onEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
