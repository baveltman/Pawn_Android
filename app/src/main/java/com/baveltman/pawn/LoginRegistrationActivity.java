package com.baveltman.pawn;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;

import com.baveltman.pawn.Models.Token;
import com.baveltman.pawn.Validation.ValidationHelper;
import com.google.gson.Gson;

import java.util.Date;


public class LoginRegistrationActivity extends Activity
        implements FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "LoginRegActivity";
    public static final int FADE_ANIMATION_DURATION = 300;
    public static final String LOGIN_TOKEN = "com.baveltman.pawn.LOGIN_TOKEN";

    //typeFaces
    private Typeface mLogoTypeFace;
    private Typeface mRegularTextTypeFace;
    private Typeface mBoldTextTypeFace;
    private Typeface mBlackTypeFace;

    //boolean to track whether we are in login or registration fragment
    private boolean mShowingRegistration = false;

    //boolean to track whether we are in login or recover password fragment
    private boolean mShowingForgotPassword = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(LOGIN_TOKEN, null);
//        editor.commit();

        checkUserLoginStatus();

        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new LoginFragment())
                    .commit();
        } else {
            mShowingRegistration = (getFragmentManager().getBackStackEntryCount() > 0);
            mShowingForgotPassword = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        // Monitor back stack changes to ensure the action bar shows the appropriate
        getFragmentManager().addOnBackStackChangedListener(this);

        setupTypefaces();
    }

    private void checkUserLoginStatus() {
        SharedPreferences sharedPref = getSharedPreferences("settings",Context.MODE_PRIVATE);
        String tokenJson = sharedPref.getString(LOGIN_TOKEN, null);

        if (tokenJson != null){
            Gson gson = new Gson();
            Token token = gson.fromJson(tokenJson, Token.class);
            boolean isTokenCurrent = ValidationHelper.isTokenCurrent(token);

            if (isTokenCurrent){
                Log.i(TAG, "found current token for user: " + token.getUser().getEmail());
                redirectToPawnActivity();
            }
        }
    }

    public void redirectToPawnActivity() {
        Intent i = new Intent();
        i.setClass(this, PawnActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }


    private void setupTypefaces() {
        mLogoTypeFace = Typeface.createFromAsset(getAssets(), "Good Day.ttf");
        mRegularTextTypeFace = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        mBoldTextTypeFace = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        mBlackTypeFace = Typeface.createFromAsset(getAssets(), "Lato-Black.ttf");
    }

    public Typeface getLogoTypeFace(){
        return mLogoTypeFace;
    }

    public Typeface getRegularTextTypeFace(){
        return mRegularTextTypeFace;
    }

    public Typeface getBoldTextTypeFace(){
        return mRegularTextTypeFace;
    }

    public Typeface getBlackTypeFace(){
        return mBlackTypeFace;
    }

    @Override
    public void onBackStackChanged() {
        mShowingRegistration = (getFragmentManager().getBackStackEntryCount() > 0);
        mShowingForgotPassword = (getFragmentManager().getBackStackEntryCount() > 0);
    }

    public void slideFragment(){
        if (mShowingForgotPassword) {
            getFragmentManager().popBackStack();
            return;
        }

        //slide to recover password
        mShowingForgotPassword = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right,
                        R.animator.slide_in_left, R.animator.slide_out_right)
                .replace(R.id.fragmentContainer, RecoverPasswordFragment
                        .instantiate(this, RecoverPasswordFragment.class.getName()))
                .addToBackStack(null)
                .commit();
    }

    public void flipCard() {
        if (mShowingRegistration) {
            getFragmentManager().popBackStack();
            return;
        }

        // Flip to register fragment
        mShowingRegistration = true;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.fragmentContainer, new RegisterFragment())

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
                .commit();
    }

    public void saveTokenToSharedPrefs(Token token){
        SharedPreferences sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String jsonToken = gson.toJson(token);
        editor.putString(LOGIN_TOKEN, jsonToken);
        editor.commit();
    }
}
