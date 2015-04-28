package com.baveltman.pawn;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by borisvelt on 4/27/15.
 */
public class DrawerFragment extends Fragment {

    private static final String TAG = "DrawerFragment";

    private TextView mUsername;
    private TextView mActionLogOut;
    private TextView mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer, parent, false);

        mUsername = (TextView)v.findViewById(R.id.user_name);
        mUsername.setTypeface(((PawnActivity)getActivity()).getBoldTextTypeFace());
        mUsername.setText(((PawnActivity) getActivity()).getCurrentUser().getFullName());

        mSettings = (TextView)v.findViewById(R.id.action_settings);
        mSettings.setTypeface(((PawnActivity) getActivity()).getBoldTextTypeFace());

        mActionLogOut = (TextView)v.findViewById(R.id.action_log_out);
        mActionLogOut.setTypeface(((PawnActivity)getActivity()).getBoldTextTypeFace());
        mActionLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(LoginRegistrationActivity.LOGIN_TOKEN, null);
                editor.commit();

                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        return v;
    }

}
