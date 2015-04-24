package com.baveltman.pawn.Services;

import com.baveltman.pawn.Models.AuthCredentials;
import com.baveltman.pawn.Models.Token;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * client to interact with /login/ endpoint
 */
public interface LoginService {

    static final String LOGGER_TAG = "LoginService";
    static final String ENDPOINT = "http://pawn-baveltman.rhcloud.com";

    @POST("/login/")
    void authenticateUser(@Body AuthCredentials credentials, Callback<Token> callback);
}
