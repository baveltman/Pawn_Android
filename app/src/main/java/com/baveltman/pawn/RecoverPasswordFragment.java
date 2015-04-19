package com.baveltman.pawn;


import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class RecoverPasswordFragment extends Fragment {

    private static final String TAG = "RecoverPasswordFragment";

    //recover password instance elements
    private TextView mLogoText;
    private EditText mEmail;
    private TextView mRecoverPasswordButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot_password, parent, false);

        bindUiElements(v);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float displayWidth = size.x;

        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationX", displayWidth,  0);
        animator.setDuration(500);

        return v;
    }

    private void bindUiElements(View v) {
        mLogoText = (TextView)v.findViewById(R.id.logo_text);
        mLogoText.setTypeface(((LoginRegistrationActivity)getActivity()).getLogoTypeFace());

        mEmail = (EditText)v.findViewById(R.id.email);
        mEmail.setTypeface(((LoginRegistrationActivity)getActivity()).getRegularTextTypeFace());

        mRecoverPasswordButton = (TextView)v.findViewById(R.id.recover_password_button);
        mRecoverPasswordButton.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());
    }
}
