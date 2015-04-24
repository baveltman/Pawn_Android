package com.baveltman.pawn.Models;

import com.google.gson.annotations.SerializedName;

/**
 * model for token
 */
public class Token {

    @SerializedName("token")
    private String mToken;

    @SerializedName("expires")
    private String mExpirationDate;

    @SerializedName("user")
    private User mUser;

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public String getExpirationDate() {
        return mExpirationDate;
    }

    public void setExpirationDate(String mExpirationDate) {
        this.mExpirationDate = mExpirationDate;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}
