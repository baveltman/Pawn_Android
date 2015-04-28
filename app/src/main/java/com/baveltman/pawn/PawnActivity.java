package com.baveltman.pawn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baveltman.pawn.Models.Token;
import com.baveltman.pawn.Models.User;
import com.baveltman.pawn.Validation.ValidationHelper;
import com.google.gson.Gson;


public class PawnActivity extends ActionBarActivity {

    private static final String TAG = "PawnActivity";

    //typeFaces
    private Typeface mLogoTypeFace;
    private Typeface mRegularTextTypeFace;
    private Typeface mBoldTextTypeFace;
    private Typeface mBlackTypeFace;

    //action bar
    private TextView mLogo;
    private ImageView mMenuButton;
    private User mCurrentUser;

    //drawer_layout
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean mIsDrawerOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);

        checkLoginStatus();

        bindDrawer();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, new PawnFragment())
                .commit();

        setupTypefaces();

        bindCustomActionBar();


    }

    private void checkLoginStatus() {
        SharedPreferences sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String tokenJson = sharedPref.getString(LoginRegistrationActivity.LOGIN_TOKEN, null);

        if (tokenJson != null){
            Gson gson = new Gson();
            Token token = gson.fromJson(tokenJson, Token.class);
            boolean isTokenCurrent = ValidationHelper.isTokenCurrent(token);

            if (!isTokenCurrent){
                Log.i(TAG, "found expired token for user: " + token.getUser().getEmail());
                redirectToLoginRegistrationActivity();
            } else {
                mCurrentUser = token.getUser();
            }
        }
    }

    public User getCurrentUser(){
        return mCurrentUser;
    }

    private void redirectToLoginRegistrationActivity() {
        Intent i = new Intent();
        i.setClass(this, LoginRegistrationActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    private void bindDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.left_drawer, new DrawerFragment())
                .commit();

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                null,  /* nav drawer icon to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                animateDrawerToggle();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                animateDrawerToggle();

            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }


    private void bindCustomActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        mLogo = (TextView) mCustomView.findViewById(R.id.title_text);
        mLogo.setTypeface(getLogoTypeFace());

        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        mMenuButton = (ImageView)mCustomView.findViewById(R.id.menue_button);

        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setElevation(0);

    }

    private void animateDrawerToggle() {
        final Animation a = AnimationUtils.loadAnimation(PawnActivity.this,
                R.anim.rotate);
        a.setDuration(300);
        mMenuButton.startAnimation(a);
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mMenuButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_white_36dp));
                    a.setRepeatCount(0);
                }
            }, 200);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mMenuButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_white_36dp));
                    a.setRepeatCount(0);
                }
            }, 200);
        }
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

}
