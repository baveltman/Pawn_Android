package com.baveltman.pawn;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Type;

public class LoginRegisterFragment extends Fragment {

    private static final String TAG = "LoginRegsitrationFragment";

    //typeFaces
    private Typeface mLogoTypeFace;
    private Typeface mRegularTextTypeFace;
    private Typeface mBoldTextTypeFace;
    private Typeface mBlackTypeFace;

    //login member variables
    private TextView mLogoText;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
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
        View v = inflater.inflate(R.layout.fragment_login_register, parent, false);

        setupTypefaces();
        bindLoginElements(v);

        return v;
    }

    private void bindLoginElements(View v) {
        mLogoText = (TextView)v.findViewById(R.id.logo_text);
        mLogoText.setTypeface(mLogoTypeFace);

        mUsername = (EditText)v.findViewById(R.id.username);
        mUsername.setTypeface(mRegularTextTypeFace);

        mPassword = (EditText)v.findViewById(R.id.password);
        mPassword.setTypeface(mRegularTextTypeFace);

        mLoginButton = (Button)v.findViewById(R.id.login_button);
        mLoginButton.setTypeface(mBoldTextTypeFace);

        mForgotPasswordText = (TextView)v.findViewById(R.id.forgot_password);
        mForgotPasswordText.setTypeface(mBoldTextTypeFace);

        mNotMemberText = (TextView)v.findViewById(R.id.not_member_text);
        mNotMemberText.setTypeface(mBoldTextTypeFace);

        mRegisterText = (TextView)v.findViewById(R.id.register_text);
        mRegisterText.setTypeface(mBlackTypeFace);
    }

    private void setupTypefaces() {
        mLogoTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "Good Day.ttf");
        mRegularTextTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");
        mBoldTextTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Bold.ttf");
        mBlackTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Black.ttf");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().hide();
        }
    }
}
