package com.mobilefactory.whosnext.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Maxime on 27/12/2015.
 */
public class Animations {

    public static void reveal(final View myView, @Nullable final AnimatorListenerAdapter callback){

        // get the center for the clipping circle
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        Animator anim;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // create the animator for this view (the start radius is zero)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
            if(callback!=null)
                anim.addListener(callback);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        }else{
            myView.setVisibility(View.VISIBLE);

            if(callback!=null)
                callback.onAnimationEnd(null);
        }
    }

    public static void hide(final View myView, @Nullable final AnimatorListenerAdapter callback){

        // get the center for the clipping circle
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth();

        Animator anim;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // create the animation (the final radius is zero)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

            if(callback!=null)
                anim.addListener(callback);

            // start the animation
            anim.start();
        }else {
            myView.setVisibility(View.INVISIBLE);

            if(callback!=null)
                callback.onAnimationEnd(null);
        }
    }

}
