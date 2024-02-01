package com.ammar.fypadmin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ammar.fypadmin.Home.HomeActivity;
import com.ammar.fypadmin.Interface.response;
import com.ammar.fypadmin.tools.Alert;
import com.ammar.fypadmin.tools.ValidatorUtil;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mLoginEmail)
    TextInputLayout email;
    @BindView(R.id.mlogin_password)
    TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
    }


    String getEmail() {
        return email.getEditText().getText().toString();
    }

    String getPasswrod() {
        return password.getEditText().getText().toString();
    }

    @OnClick(R.id.mloginbtn)
    void login() {
        if (isValidInput())
            FirebaseCrud.getInstance().login(getEmail(), getPasswrod(), new response() {
                @Override
                public void onSuccess(String msg) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                @Override
                public void onFail(String msg) {
                    Alert.getInstance().showSnackbar(findViewById(android.R.id.content), msg, false);
                }
            });
    }

    @OnClick(R.id.mforgetpass)
    void resetWindow() {
        Intent intent = new Intent(MainActivity.this, ResetPassword.class);
        startActivity(intent);

    }

    private boolean isValidInput() {
        if (TextUtils.isEmpty(getEmail()))
            return Alert.getInstance().showSnackbar(findViewById(android.R.id.content), "Email Required", false);
        if (TextUtils.isEmpty(getPasswrod()))
            return Alert.getInstance().showSnackbar(findViewById(android.R.id.content), "Password Required", false);
        if (!ValidatorUtil.getInstance().validEmail(getEmail()))
            return Alert.getInstance().showSnackbar(findViewById(android.R.id.content), "Email Not Valid \ne.g: ammarishfaq25@gmail.com", false);
        if (!getEmail().equals(getString(R.string.adminMail)))
            return Alert.getInstance().showSnackbar(findViewById(android.R.id.content), "Email Not Authorize For That App", false);

        return true;
    }
}