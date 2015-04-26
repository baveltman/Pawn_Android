package com.baveltman.pawn;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class PawnActivity extends ActionBarActivity {

    //typeFaces
    private Typeface mLogoTypeFace;
    private Typeface mRegularTextTypeFace;
    private Typeface mBoldTextTypeFace;
    private Typeface mBlackTypeFace;

    //action bar
    private TextView mLogo;
    private ImageView mMenuButton;

    //drawer
    private static final String[] mDrawerTitles = {"Log out"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);

        setupTypefaces();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, new PawnFragment())
                .commit();

        bindCustomActionBar();
        bindDrawer();

    }

    private void bindDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mDrawerTitles));

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
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mMenuButton = (ImageView)mCustomView.findViewById(R.id.menue_button);

        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

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
