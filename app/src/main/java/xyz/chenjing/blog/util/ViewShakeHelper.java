package xyz.chenjing.blog.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by london on 16/2/20.
 * shake the EditText
 */
public class ViewShakeHelper {
    private Animation shakeAnimation;

    public ViewShakeHelper() {
        shakeAnimation = new TranslateAnimation(0, 16, 0, 0);
        shakeAnimation.setDuration(400);
        CycleInterpolator cycleInterpolator = new CycleInterpolator(4);
        shakeAnimation.setInterpolator(cycleInterpolator);

    }

    /**
     * shake the view
     *
     * @param v to be shaken
     */
    public void shake(View v) {
        v.startAnimation(shakeAnimation);
    }
}