package alonbd.simpler.UI;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public abstract class Animator {
    public static void animatePencil(ImageView iv){
        Animation flip = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.45f);
        flip.setDuration(1200);
        flip.setStartOffset(2000);
        flip.setInterpolator(new AnticipateOvershootInterpolator());
        flip.setRepeatCount(Animation.INFINITE);
        flip.setRepeatMode(Animation.REVERSE);

        TranslateAnimation inAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        inAnim.setDuration(500);

        setEndListener(inAnim,()->{
           iv.startAnimation(flip);
        });

        iv.startAnimation(inAnim);
    }

    public static void animatePaperPlane(ImageView iv){
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_PARENT,1f,Animation.RELATIVE_TO_PARENT,0.35f,Animation.RELATIVE_TO_PARENT,-0.35f);
        anim.setDuration(4500);
        anim.setStartOffset(7000);
        anim.setInterpolator(new DecelerateInterpolator(4));
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.RESTART);
        anim.setFillAfter(true);
        anim.setFillBefore(true);
        iv.startAnimation(anim);
    }

    public static void animateWavingHand(ImageView iv){
        RotateAnimation anim = new RotateAnimation(-35,35,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.8f);
        anim.setDuration(1300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(anim);
    }


    interface EndListener{
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
