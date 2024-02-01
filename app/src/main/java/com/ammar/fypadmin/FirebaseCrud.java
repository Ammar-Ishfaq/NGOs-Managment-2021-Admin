package com.ammar.fypadmin;

import com.ammar.fypadmin.Interface.response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCrud {
    private static final FirebaseCrud ourInstance = new FirebaseCrud();

    public static FirebaseCrud getInstance() {
        return ourInstance;
    }

    public FirebaseCrud() {
    }

    /**
     * make the login response of the admin
     *
     * @param email
     * @param passwrod
     * @param response
     */
    public void login(String email, String passwrod, response response) {
        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, passwrod)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            response.onSuccess("Login Successfully");
                        } else {
                            response.onFail("Login Failed");
                        }
                    });
        } catch (Exception e) {
            response.onFail("Something went wrong please try again later!");
        }
    }

    public void blockUser(String email, response response) {
        try {
            String temp = email;
            email = email.replace(".", ",");
            FirebaseDatabase.getInstance().getReference()
                    .child("User")
                    .child(email)
                    .child("isAllow")
                    .setValue("false")
                    .addOnSuccessListener(aVoid -> {
                        response.onSuccess("Successfully Blocked " + temp);
                    })
                    .addOnFailureListener(e -> response.onFail("Failed \nErr: " + e));
        } catch (Exception e) {
            response.onFail("Failed \nErr: " + e);
        }
    }

    public void unBlockUser(String email, response response) {
        try {
            String temp = email;
            email = email.replace(".", ",");
            FirebaseDatabase.getInstance().getReference()
                    .child("User")
                    .child(email)
                    .child("isAllow")
                    .setValue("true")
                    .addOnSuccessListener(aVoid -> {
                        response.onSuccess("Successfully Un-Blocked " + temp);
                    })
                    .addOnFailureListener(e -> response.onFail("Failed \nErr: " + e));
        } catch (Exception e) {
            response.onFail("Failed \nErr: " + e);
        }
    }

    String switchStatus(String oldStatus) {
        if (oldStatus.equals("true"))
            return "false";
        else
            return "true";

    }
}
