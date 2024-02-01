package com.ammar.fypadmin.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final ValidatorUtil ourInstance = new ValidatorUtil();

    public static ValidatorUtil getInstance() {
        return ourInstance;
    }

    private ValidatorUtil() {
    }

    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public boolean validEmail(String emailSU) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailSU.matches(emailPattern);
    }

    public boolean validPassword(String passwordSU) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[_@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(passwordSU);

        return matcher.matches();
    }

    public boolean isDigitsOnly(String phone) {
        try {
            int ph = Integer.parseInt(phone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
