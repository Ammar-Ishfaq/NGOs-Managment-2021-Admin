package com.ammar.fypadmin.tools;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Alert {
    private static final  Alert ourInstance = new  Alert();

    public static  Alert getInstance() {
        return ourInstance;
    }

    public Alert() {
    }

    public boolean showSnackbar(View view, String MSG, boolean b) {
        Snackbar snackbar = Snackbar.make(
                view,
                MSG,
                1000
        );
        snackbar.setAction("OK", v -> snackbar.dismiss());
        snackbar.show();
        return b;
    }
}
