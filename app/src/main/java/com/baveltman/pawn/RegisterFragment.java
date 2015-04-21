package com.baveltman.pawn;

import android.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baveltman.pawn.CustomViews.ViewAnimationHelper;
import com.baveltman.pawn.Models.User;
import com.baveltman.pawn.Models.UserResponse;
import com.baveltman.pawn.Services.UserService;
import com.baveltman.pawn.Validation.ValidationHelper;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * controller for register view
 */
public class RegisterFragment extends Fragment {

    private static String TAG = "RegisterFragment";
    private static final int FADE_ANIMATION_DURATION = 300;

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
    private TextView mRegisteringUserText;

    private RestAdapter mRegisterRestAdapter;
    private UserService mUsersService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        //create rest adapter and userService
        if (mRegisterRestAdapter == null) {
            mRegisterRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(UserService.USERS_ENDPOINT)
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

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ViewAnimationHelper.crossfade(mRegisterLoading, mRegisterFields, FADE_ANIMATION_DURATION);

                User user = new User();
                user.setFirstName(mFirstName.getText().toString());
                user.setLastName(mLastName.getText().toString());
                user.setEmail(mEmail.getText().toString());
                user.setPassword(mPassword.getText().toString());

                mUsersService.createUser(user, new Callback<UserResponse>() {
                    @Override
                    public void success(UserResponse userResponse, Response response) {
                        Log.i(TAG, "user creation succeeded: " + userResponse.toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "user creation failed: " + error.getMessage().toString());
                    }
                });
            }
        });
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

        mRegisteringUserText = (TextView)v.findViewById(R.id.text_registering_user);
        mRegisteringUserText.setTypeface(((LoginRegistrationActivity)getActivity()).getBlackTypeFace());
    }

}
