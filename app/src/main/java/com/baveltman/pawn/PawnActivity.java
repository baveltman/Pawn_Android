package com.baveltman.pawn;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by borisvelt on 4/24/15.
 */
public class PawnActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new PawnFragment())
                .commit();
    }

}
