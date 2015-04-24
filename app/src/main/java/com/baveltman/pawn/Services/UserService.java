package com.baveltman.pawn.Services;

import com.baveltman.pawn.Models.Token;
import com.baveltman.pawn.Models.User;
import com.baveltman.pawn.Models.UserResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Client to interact with /users/ endpoint
 */
public interface UserService {

    static final String LOGGER_TAG = "UserService";
    static final String USERS_ENDPOINT = "http://pawn-baveltman.rhcloud.com";

    @POST("/users/")
    void createUser(@Body User user, Callback<Token> callback);

}
