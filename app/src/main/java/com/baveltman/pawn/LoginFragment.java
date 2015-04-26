package com.baveltman.pawn;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baveltman.pawn.CustomViews.ViewAnimationHelper;
import com.baveltman.pawn.Models.AuthCredentials;
import com.baveltman.pawn.Models.Token;
import com.baveltman.pawn.Models.User;
import com.baveltman.pawn.Services.LoginService;
import com.baveltman.pawn.Services.UserService;
import com.baveltman.pawn.Validation.DateDeserializer;
import com.baveltman.pawn.Validation.ValidationHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    //login member variables
    private TextView mLogoText;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mLoginButton;
    private TextView mForgotPasswordText;
    private TextView mNotMemberText;
    private TextView mRegisterText;

    private TextView mEmailValidationMessage;
    private TextView mPasswordValidationMessage;

    private LinearLayout mLoginFieldsLayout;
    private LinearLayout mLoginLoadingLayout;
    private LinearLayout mNotMemberLayout;

    private RestAdapter mLoginRestAdapter;
    private LoginService mLoginService;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        //create rest adapter and userService
        if (mLoginRestAdapter == null) {
            mLoginRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(LoginService.ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
        }

        if (mLoginService == null) {
            mLoginService = mLoginRestAdapter.create(LoginService.class);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, parent, false);

        bindLoginUiElements(v);
        setRegisterRedirect();
        setRecoverPasswordRedirect();
        bindLoginFormInteractionEvents();

        return v;
    }

    private void setRecoverPasswordRedirect() {
        mForgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginRegistrationActivity) getActivity()).slideFragment();
            }
        });
    }

    private void bindLoginFormInteractionEvents() {
        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    if (mEmailValidationMessage.getVisibility() == View.VISIBLE
                            && mEmail.getText().length() > 0
                            && ValidationHelper.isEmailValid(mEmail.getText().toString())){
                        ViewAnimationHelper.fadeOut(mEmailValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
                    }
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    if (mPasswordValidationMessage.getVisibility() == View.VISIBLE
                            && mPassword.getText().length() > 0
                            && ValidationHelper.isPasswordValid(mPassword.getText().toString())) {
                        ViewAnimationHelper.fadeOut(mPasswordValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
                    }
            }
        });

        mPassword.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            if (mPasswordValidationMessage.getVisibility() == View.VISIBLE
                                    && mPassword.getText().length() > 0
                                    && ValidationHelper.isPasswordValid(mPassword.getText().toString())) {
                                ViewAnimationHelper.fadeOut(mPasswordValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
                            }

                            hideSoftKeyboard();
                            return true;
                        }
                        return false;
                    }
                }
        );

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                hideValidationMessages();
                boolean isLoginValid = validateLoginForm();

                if (isLoginValid){
                    ViewAnimationHelper.fadeOut(mNotMemberLayout, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
                    ViewAnimationHelper.crossfade(mLoginLoadingLayout, mLoginFieldsLayout, LoginRegistrationActivity.FADE_ANIMATION_DURATION);

                    AuthCredentials credentials = new AuthCredentials();
                    credentials.setEmail(mEmail.getText().toString());
                    credentials.setPassword(mPassword.getText().toString());

                    mLoginService.authenticateUser(credentials, new Callback<Token>() {
                        @Override
                        public void success(Token token, Response response) {
                            Log.i(TAG, "user auth succeeded: " + token.toString());
                            ((LoginRegistrationActivity)getActivity()).saveTokenToSharedPrefs(token);
                            ((LoginRegistrationActivity)getActivity()).redirectToPawnActivity();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, "user auth failed: " + error.getMessage().toString());
                        }
                    });
                }

            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e){
            //no soft keyboard to hide
        }
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

    private void bindLoginUiElements(View v) {
        mLogoText = (TextView)v.findViewById(R.id.logo_text);
        mLogoText.setTypeface(((LoginRegistrationActivity)getActivity()).getLogoTypeFace());

        mEmail = (EditText)v.findViewById(R.id.email);
        mEmail.setTypeface(((LoginRegistrationActivity) getActivity()).getRegularTextTypeFace());

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

        mEmailValidationMessage = (TextView)v.findViewById(R.id.email_validation_message);
        mEmailValidationMessage.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mPasswordValidationMessage = (TextView)v.findViewById(R.id.password_validation_message);
        mPasswordValidationMessage.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mLoginFieldsLayout = (LinearLayout)v.findViewById(R.id.login_form_layout);
        mLoginLoadingLayout = (LinearLayout)v.findViewById(R.id.login_loading);
        mNotMemberLayout = (LinearLayout)v.findViewById(R.id.not_member_text_layout);
    }

    private void hideValidationMessages() {

        if (mEmailValidationMessage.getVisibility() == View.VISIBLE){
            ViewAnimationHelper.fadeOut(mEmailValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
        }

        if (mPasswordValidationMessage.getVisibility() == View.VISIBLE){
            ViewAnimationHelper.fadeOut(mPasswordValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
        }
    }

    private boolean validateLoginForm() {
        boolean isLoginValid = true;

        if(mEmail.getText().length() == 0 || !ValidationHelper.isEmailValid(mEmail.getText().toString())){
            ViewAnimationHelper.fadeIn(mEmailValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
            isLoginValid = false;
        }

        if(mPassword.getText().length() == 0 || !ValidationHelper.isPasswordValid(mPassword.getText().toString())){
            ViewAnimationHelper.fadeIn(mPasswordValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
            isLoginValid = false;
        }


        return isLoginValid;
    }



}
