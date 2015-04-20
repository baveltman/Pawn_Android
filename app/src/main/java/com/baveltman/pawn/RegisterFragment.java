package com.baveltman.pawn;

import android.app.Fragment;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by borisvelt on 4/18/15.
 */
public class RegisterFragment extends Fragment {

    private static String TAG = "RegisterFragment";

    //member variables
    private TextView mLogoText;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhoneNumber;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mRegisterButton;
    private ImageView mBackToLoginImage;
    private TextView mBackToLoginText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, parent, false);

        bindRegisterElements(v);
        bindBackToLoginClickEvents();

        return v;
    }

    private void bindBackToLoginClickEvents() {
        mBackToLoginImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginRegistrationActivity)getActivity()).flipCard();
            }
        });

        mBackToLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginRegistrationActivity)getActivity()).flipCard();
            }
        });
    }

    private void bindRegisterElements(View v) {
        mLogoText = (TextView)v.findViewById(R.id.logo_text);
        mLogoText.setTypeface(((LoginRegistrationActivity)getActivity()).getLogoTypeFace());

        mFirstName = (EditText)v.findViewById(R.id.first_name);
        mFirstName.setTypeface(((LoginRegistrationActivity)getActivity()).getRegularTextTypeFace());

        mLastName = (EditText)v.findViewById(R.id.last_name);
        mLastName.setTypeface(((LoginRegistrationActivity)getActivity()).getRegularTextTypeFace());

        mPhoneNumber = (EditText)v.findViewById(R.id.phone_number);
        mPhoneNumber.setTypeface(((LoginRegistrationActivity)getActivity()).getRegularTextTypeFace());

        mEmail = (EditText)v.findViewById(R.id.email);
        mEmail.setTypeface(((LoginRegistrationActivity)getActivity()).getRegularTextTypeFace());

        mPassword = (EditText)v.findViewById(R.id.password);
        mPassword.setTypeface(((LoginRegistrationActivity)getActivity()).getRegularTextTypeFace());

        mRegisterButton = (TextView)v.findViewById(R.id.register_button);
        mRegisterButton.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mBackToLoginImage = (ImageView)v.findViewById(R.id.image_back_log_in);

        mBackToLoginText = (TextView)v.findViewById(R.id.action_back_log_in);
        mBackToLoginText.setTypeface(((LoginRegistrationActivity)getActivity()).getBlackTypeFace());
    }

}
