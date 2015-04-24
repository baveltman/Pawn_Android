package com.baveltman.pawn;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baveltman.pawn.CustomViews.ViewAnimationHelper;
import com.baveltman.pawn.Models.Token;
import com.baveltman.pawn.Models.User;
import com.baveltman.pawn.Models.UserResponse;
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

/**
 * controller for register view
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

    private LinearLayout mRegisterFields;
    private LinearLayout mRegisterLoading;
    private LinearLayout mBackToLogin;
    private TextView mRegisteringUserText;

    private TextView mFirstNameValidationMessage;
    private TextView mLastNameValidationMessage;
    private TextView mEmailValidationMessage;
    private TextView mPasswordValidationMessage;

    private RestAdapter mRegisterRestAdapter;
    private UserService mUsersService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        //create rest adapter and userService
        if (mRegisterRestAdapter == null) {
            mRegisterRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(UserService.USERS_ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
        }

        if (mUsersService == null) {
            mUsersService = mRegisterRestAdapter.create(UserService.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, parent, false);


        bindRegisterElements(v);
        bindBackToLoginClickEvents();
        bindRegisterEvents();

        return v;
    }

    private void bindRegisterEvents() {

        mFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    if (mFirstNameValidationMessage.getVisibility() == View.VISIBLE
                        && mFirstName.getText().length() > 0){
                        ViewAnimationHelper.fadeOut(mFirstNameValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
                    }
            }
        });

        mLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    if (mLastNameValidationMessage.getVisibility() == View.VISIBLE
                            && mLastName.getText().length() > 0){
                        ViewAnimationHelper.fadeOut(mLastNameValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
                    }
            }
        });

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
                            && ValidationHelper.isPasswordValid(mPassword.getText().toString())){
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
                                    && ValidationHelper.isPasswordValid(mPassword.getText().toString())){
                                ViewAnimationHelper.fadeOut(mPasswordValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
                            }

                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mPassword.getWindowToken(), 0);
                            return true;
                        }
                        return false;
                    }
                }
        );

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideValidationMessages();
                boolean isRegistrationValid = validateRegistrationForm();

                if (isRegistrationValid){
                    ViewAnimationHelper.fadeOut(mBackToLogin, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
                    ViewAnimationHelper.crossfade(mRegisterLoading, mRegisterFields, LoginRegistrationActivity.FADE_ANIMATION_DURATION);

                    User user = new User();
                    user.setFirstName(mFirstName.getText().toString());
                    user.setLastName(mLastName.getText().toString());
                    user.setEmail(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());

                    mUsersService.createUser(user, new Callback<Token>() {
                        @Override
                        public void success(Token token, Response response) {
                            Log.i(TAG, "user creation succeeded: " + token.toString());
                            ((LoginRegistrationActivity)getActivity()).saveTokenToSharedPrefs(token);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, "user creation failed: " + error.getMessage().toString());
                        }
                    });
                }

            }
        });
    }

    private void hideValidationMessages() {
        if (mFirstNameValidationMessage.getVisibility() == View.VISIBLE) {
            ViewAnimationHelper.fadeOut(mFirstNameValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
        }

        if (mLastNameValidationMessage.getVisibility() == View.VISIBLE){
            ViewAnimationHelper.fadeOut(mLastNameValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
        }

        if (mEmailValidationMessage.getVisibility() == View.VISIBLE){
            ViewAnimationHelper.fadeOut(mEmailValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
        }

        if (mPasswordValidationMessage.getVisibility() == View.VISIBLE){
            ViewAnimationHelper.fadeOut(mPasswordValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
        }
    }

    private boolean validateRegistrationForm() {
        boolean isRegistrationValid = true;

        if(mFirstName.getText().length() == 0){
            ViewAnimationHelper.fadeIn(mFirstNameValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
            isRegistrationValid = false;
        }

        if(mLastName.getText().length() == 0){
            ViewAnimationHelper.fadeIn(mLastNameValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
            isRegistrationValid = false;
        }

        if(mEmail.getText().length() == 0 || !ValidationHelper.isEmailValid(mEmail.getText().toString())){
            ViewAnimationHelper.fadeIn(mEmailValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
            isRegistrationValid = false;
        }

        if(mPassword.getText().length() == 0 || !ValidationHelper.isPasswordValid(mPassword.getText().toString())){
            ViewAnimationHelper.fadeIn(mPasswordValidationMessage, LoginRegistrationActivity.FADE_ANIMATION_DURATION);
            isRegistrationValid = false;
        }


        return isRegistrationValid;
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
        mBackToLoginText.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mRegisterFields = (LinearLayout)v.findViewById(R.id.registration_fields);
        mRegisterLoading = (LinearLayout)v.findViewById(R.id.register_loading);
        mBackToLogin = (LinearLayout)v.findViewById(R.id.back_to_login_view);

        mRegisteringUserText = (TextView)v.findViewById(R.id.text_registering_user);
        mRegisteringUserText.setTypeface(((LoginRegistrationActivity)getActivity()).getBlackTypeFace());

        mFirstNameValidationMessage = (TextView)v.findViewById(R.id.first_name_validation_message);
        mFirstNameValidationMessage.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mLastNameValidationMessage = (TextView)v.findViewById(R.id.last_name_validation_message);
        mLastNameValidationMessage.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mEmailValidationMessage = (TextView)v.findViewById(R.id.email_validation_message);
        mEmailValidationMessage.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());

        mPasswordValidationMessage = (TextView)v.findViewById(R.id.password_validation_message);
        mPasswordValidationMessage.setTypeface(((LoginRegistrationActivity)getActivity()).getBoldTextTypeFace());
    }

}
