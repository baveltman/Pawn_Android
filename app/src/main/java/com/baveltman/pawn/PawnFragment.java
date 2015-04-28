package com.baveltman.pawn;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by borisvelt on 4/24/15.
 */
public class PawnFragment extends Fragment {

    private TextView mPawnTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pawn, parent, false);

        mPawnTitle = (TextView)v.findViewById(R.id.pawn_title);
        mPawnTitle.setTypeface(((PawnActivity)getActivity()).getBoldTextTypeFace());

        return v;
    }
}
