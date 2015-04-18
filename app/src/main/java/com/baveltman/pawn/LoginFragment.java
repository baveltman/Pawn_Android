package com.baveltman.pawn;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginRegsitrationFragment";

    //login member variables
    private TextView mLogoText;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mLoginButton;
    private TextView mForgotPasswordText;
    private TextView mNotMemberText;
    private TextView mRegisterText;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, parent, false);

        bindLoginElements(v);
        setRegisterRedirect();

        return v;
    }

    private void setRegisterRedirect() {
        mNotMemberText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((LoginRegistrationActivity)getActivity()).flipCard();
            }
        });

        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginRegistrationActivity)getActivity()).flipCard();
            }
        });
    }

    private void bindLoginElements(View v) {
        mLogoText = (TextView)v.findViewById(R.id.logo_text);
        mLogoText.setTypeface(((LoginRegistrationActivity)getActivity()).getLogoTypeFace());

        mUsername = (EditText)v.findViewById(R.id.username);
        mUsername.setTypeface(((LoginRegistrationActivity)getActivity()).getRegularTextTypeFace());

        mPassword = (EditText)v.findViewById(R.id.password);
        mPassword.setTypeface(((LoginRegistrationActivity)getActivity()).getRegularTextTypeFace());

        mLoginButton = (TextView)v.findViewById(R.id.login_button);
        mLoginButton.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mForgotPasswordText = (TextView)v.findViewById(R.id.forgot_password);
        mForgotPasswordText.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mNotMemberText = (TextView)v.findViewById(R.id.not_member_text);
        mNotMemberText.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mRegisterText = (TextView)v.findViewById(R.id.register_text);
        mRegisterText.setTypeface(((LoginRegistrationActivity)getActivity()).getBlackTypeFace());
    }



}
