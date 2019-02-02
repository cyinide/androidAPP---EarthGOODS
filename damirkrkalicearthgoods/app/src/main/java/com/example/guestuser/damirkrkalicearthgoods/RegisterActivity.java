package com.example.guestuser.damirkrkalicearthgoods;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;


import com.example.guestuser.damirkrkalicearthgoods.data.Blocs;
import com.example.guestuser.damirkrkalicearthgoods.data.RegisterUser;
import com.example.guestuser.damirkrkalicearthgoods.data.UserVM;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyBlocsBinder;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyUtils;

import java.util.List;

import static com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest.ENDPOINT_LOCATIONS;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText inputUsername, inputPassword, inputIme, inputPrezime;
    private Spinner spinnerBlok;
    private Button btnRegister, btnOpenLogin;
    private ProgressBar progressBar_blokSpinner;
    private ProgressBar progressBar_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_activity_layout);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        //statusBarColor
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        progressBar_blokSpinner = findViewById(R.id.progressBar_blokSpinner);
        progressBar_register = findViewById(R.id.progressBar_register);

        inputUsername = findViewById(R.id.inputRegisterUsername);
        inputPassword = findViewById(R.id.inputRegisterPassword);
        inputIme = findViewById(R.id.inputRegisterIme);
        inputPrezime = findViewById(R.id.inputRegisterPrezime);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnRegisterClick();
            }
        });

        btnOpenLogin = findViewById(R.id.btnOpenLogin);
        btnOpenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnOpenLoginClick();
            }
        });

        spinnerBlok = findViewById(R.id.spinnerRegisterBlok);
        blokPodaciRequest();
    }

    private void blokPodaciRequest() {
        MyApiRequest.get(ENDPOINT_LOCATIONS, new MyAbstractRunnable<Blocs>() {
            @Override
            public void run(Blocs Blocs) {
                List<String> blokPodaci = MyBlocsBinder.getStringListBlokovi(Blocs);
                onBlokPodaciReceived(blokPodaci, null, null);

            }

            @Override
            public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                onBlokPodaciReceived(null, statusCode, errorMessage);
            }
        });
    }

    private void onBlokPodaciReceived(@Nullable List<String> blokPodaci, @Nullable Integer statusCode, @Nullable String errorMessage) {
        progressBar_blokSpinner.setVisibility(View.INVISIBLE);

        if (blokPodaci != null) {
            MyUtils.popuniSpinner(this,
                    blokPodaci,
                    android.R.layout.simple_spinner_item,
                    android.R.layout.simple_spinner_dropdown_item,
                    spinnerBlok);
        } else {
            btnRegister.setEnabled(false);
            Snackbar.make(findViewById(android.R.id.content),
                    errorMessage != null ? errorMessage : getString(R.string.error),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            blokPodaciRequest();
                        }
                    })
                    .show();
        }
    }

    private void do_btnOpenLoginClick() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void do_btnRegisterClick() {
        if (inputPassword.length() <= 4) {
            inputPassword.setError(getString(R.string.reg_pw_err));
        } else {
            inputPassword.setError(null); // Clear the error
        }
        if (inputUsername.length() <= 4) {
            inputUsername.setError(getString(R.string.username_reg_error));
        }  else {
            inputUsername.setError(null); // Clear the error
        }
        if (inputIme.length() <= 4) {
            inputIme.setError(getString(R.string.reg_name_err));
        } else {
            inputIme.setError(null); // Clear the error
        }
        if (inputPrezime.length() <= 4) {
            inputPrezime.setError(getString(R.string.reg_lastname_err));
        } else {
            inputPrezime.setError(null); // Clear the error
        }

        if (inputUsername.getError() != null
                || inputPassword.getError() != null
                || inputIme.getError() != null
                || inputPassword.getError() != null) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.invalid_data), Snackbar.LENGTH_SHORT).show();
            return;
        }

        RegisterUser newAccount = new RegisterUser(
                0,
                inputUsername.getText().toString(),
                inputPassword.getText().toString(),
                inputIme.getText().toString(),
                inputPrezime.getText().toString(),
                "",
                "",
                MyBlocsBinder.getBlocs().get(spinnerBlok.getSelectedItemPosition()).getId()
        );

        MyApiRequest.post(MyApiRequest.endpoint_register, newAccount, new MyAbstractRunnable<UserVM>() {
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
        progressBar_register.setVisibility(View.INVISIBLE);

        if (korisnik == null) {
            Snackbar.make(findViewById(android.R.id.content),
                    errorMessage != null ? errorMessage : getString(R.string.invalid_data) , Snackbar.LENGTH_SHORT).show();
        } else {
            MySession.setKorisnik(korisnik);
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.valid_registration) , Snackbar.LENGTH_SHORT).show();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
