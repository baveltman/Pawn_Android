package com.baveltman.pawn.Models;

import com.google.gson.annotations.SerializedName;

public class AuthCredentials {

    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
