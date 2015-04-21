package com.baveltman.pawn.CustomViews;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class ViewAnimationHelper {

    public static void crossfade(final View fadeIn, final View fadeout, final int duration) {

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        fadeout.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fadeout.setVisibility(View.GONE);

                        // Set the content view to 0% opacity but visible, so that it is visible
                        // (but fully transparent) during the animation.
                        fadeIn.setAlpha(0f);
                        fadeIn.setVisibility(View.VISIBLE);

                        // Animate the content view to 100% opacity, and clear any animation
                        // listener set on the view.
                        fadeIn.animate()
                                .alpha(1f)
                                .setDuration(duration)
                                .setListener(null);
                    }
                });




    }

}
