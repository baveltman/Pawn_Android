package com.baveltman.pawn.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Client validation helper
 */
public class ValidationHelper {


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPasswordValid(String password){
        return password != null && password.length() >= 6;
    }
}
