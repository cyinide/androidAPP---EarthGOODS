package com.example.guestuser.damirkrkalicearthgoods;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.guestuser.damirkrkalicearthgoods.data.LoginUser;
import com.example.guestuser.damirkrkalicearthgoods.data.UserVM;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyUtils;


public class LoginActivity extends AppCompatActivity {

    private static String BUNDLE_KEY_USERNAME = "MyBundleKeyUsername";
    private static String BUNDLE_KEY_PASSWORD = "MyBundleKeyPassword";

    private TextInputEditText inputUsername, inputPassword;
    private Button btnLogin, btnOpenRegistration;
    private ProgressBar progressBar_login;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        //statusBarColor
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));



        progressBar_login = findViewById(R.id.progressBar_login);

        inputUsername = findViewById(R.id.inputLoginUsername);
        inputPassword = findViewById(R.id.inputLoginPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                do_btnLoginClick();
            }
        });

        btnOpenRegistration = findViewById(R.id.btnOpenRegistration);
        btnOpenRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnOpenRegistrationClick();
            }
        });

        try {
            if (savedInstanceState.containsKey(BUNDLE_KEY_USERNAME)) {
                inputUsername.setText(BUNDLE_KEY_USERNAME);
            }
            if (savedInstanceState.containsKey(BUNDLE_KEY_USERNAME)) {
                inputPassword.setText(BUNDLE_KEY_PASSWORD);
            }
        } catch (NullPointerException e) {
            Log.i("Error", "login nullpointer on savedinstance");
            // e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putString(BUNDLE_KEY_USERNAME, inputUsername.getText().toString());
            outState.putString(BUNDLE_KEY_PASSWORD, inputPassword.getText().toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void do_btnOpenRegistrationClick() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void do_btnLoginClick() {
        MyUtils.dismissKeyboard(this);
        progressBar_login.setVisibility(View.VISIBLE);

        if (inputPassword.length() < 3) {
            inputPassword.setError(getString(R.string.password_error));
        } else {
            inputPassword.setError(null); // Clear the error
        }
        if (inputUsername.length() < 3) {
            inputUsername.setError(getString(R.string.username_error));
        } else {
            inputUsername.setError(null); // Clear the error
        }

        if (inputUsername.getError() != null || inputPassword.getError() != null) {
            Snackbar.make(findViewById(android.R.id.content), R.string.invalid_data, Snackbar.LENGTH_LONG).show();
            findViewById(R.id.progressBar_login).setVisibility(View.INVISIBLE);
            return;
        }

        LoginUser credentialsObj = new LoginUser(inputUsername.getText().toString(), inputPassword.getText().toString());
        MyApiRequest.post(MyApiRequest.endpoint_auth, credentialsObj, new MyAbstractRunnable<UserVM>() {
            @Override
            public void run(UserVM userVM) {
                loginUser(userVM, null, null);
            }

            @Override
            public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                loginUser(null, statusCode, errorMessage);
            }

        });
    }

    private void loginUser(@Nullable UserVM korisnik, @Nullable Integer statusCode, @Nullable String errorMessage) {
        progressBar_login.setVisibility(View.INVISIBLE);
        if (korisnik == null) {
            Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.incorrect_username_or_pw) ,
                    Snackbar.LENGTH_LONG).show();
        } else {
            MySession.setKorisnik(korisnik);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
