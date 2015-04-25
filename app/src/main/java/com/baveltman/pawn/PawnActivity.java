package com.baveltman.pawn;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;

import com.baveltman.pawn.CustomViews.TypefaceSpan;


public class PawnActivity extends ActionBarActivity {

    //typeFaces
    private Typeface mLogoTypeFace;
    private Typeface mRegularTextTypeFace;
    private Typeface mBoldTextTypeFace;
    private Typeface mBlackTypeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        setupTypefaces();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new PawnFragment())
                .commit();

        bindLogoToActionBar();

    }

    private void bindLogoToActionBar() {
        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new TypefaceSpan(this, "Good Day.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);
        actionBar.show();
    }


    private void setupTypefaces() {
        mLogoTypeFace = Typeface.createFromAsset(getAssets(), "Good Day.ttf");
        mRegularTextTypeFace = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        mBoldTextTypeFace = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        mBlackTypeFace = Typeface.createFromAsset(getAssets(), "Lato-Black.ttf");
    }

}
