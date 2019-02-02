package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.RegisterUser;
import com.example.guestuser.damirkrkalicearthgoods.data.UserVM;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;


public class UserChangePasswordFragment extends DialogFragment {

    public static String Tag = "userChangePasswordFragment";

    private TextInputEditText textCurrPassword;
    private TextInputEditText textReenterNewPassword;
    private TextInputEditText textNewPassword;
    private Button btnCancel;
    private Button btnSave;
    private UserVM user;
    private View view;

    public static UserChangePasswordFragment newInstance() {
        UserChangePasswordFragment fragment = new UserChangePasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AlertDialogsTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.change_password_layout, container, false);

        user = MySession.getKorisnik();

        textCurrPassword = view.findViewById(R.id.textProfilOpcijeLozinkaTrenutna);
        textReenterNewPassword = view.findViewById(R.id.textProfilOpcijeLozinkaNewPonovo);
        textNewPassword = view.findViewById(R.id.textProfilOpcijeLozinkaNew);

        btnCancel = view.findViewById(R.id.btnProfilLozinkaOdustani);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btnSave = view.findViewById(R.id.btnProfilLozinkaSnimi);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.progressBar_snimiPromjene) != null) {
                    view.findViewById(R.id.progressBar_snimiPromjene).setVisibility(View.VISIBLE);
                }

                if (textCurrPassword == null ||
                        !user.correctLozinka(textCurrPassword.getText().toString())) {
                    // iftrue:
                    textCurrPassword.setError(getString(R.string.wrong_pw));
                } else {
                    textCurrPassword.setError(null);
                }

                if (textNewPassword == null ||
                        textReenterNewPassword == null ||
                        textNewPassword.length() < 6 ||
                        textReenterNewPassword.length() < 6  ||
                        !textNewPassword.getText().toString().equals(textReenterNewPassword.getText().toString())) {
                    // iftrue:
                    textNewPassword.setError(getString(R.string.new_pw_err));
                    textReenterNewPassword.setError(getString(R.string.reenter_pw_er));
                } else {
                    textNewPassword.setError(null);
                    textReenterNewPassword.setError(null);
                }

                if (textCurrPassword.getError() != null ||
                    textNewPassword.getError() != null ||
                    textReenterNewPassword.getError() != null) {
                    Snackbar.make(getView(), getString(R.string.invalid_data), Snackbar.LENGTH_SHORT).show();

                    if (view.findViewById(R.id.progressBar_snimiPromjene) != null) {
                        view.findViewById(R.id.progressBar_snimiPromjene).setVisibility(View.INVISIBLE);
                    }

                    return;
                }

                RegisterUser userPostObj = new RegisterUser(user);
                userPostObj.setPassword(textNewPassword.getText().toString());

                MyApiRequest.post(MyApiRequest.endpoint_update_user, userPostObj, new MyAbstractRunnable<UserVM>() {
                    @Override
                    public void run(UserVM userVM) {
                        updatePassword(userVM, null, null);
                    }

                    @Override
                    public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                        updatePassword(null, statusCode, errorMessage);
                    }
                });
            }
        });

        return view;
    }

    private void updatePassword(@Nullable UserVM korisnik, @Nullable Integer statusCode, @Nullable String errorMessage) {
        if (view.findViewById(R.id.progressBar_snimiPromjene) != null) {
            view.findViewById(R.id.progressBar_snimiPromjene).setVisibility(View.INVISIBLE);
        }

        if (korisnik == null) {
            Snackbar.make(getView(), R.string.try_again_err, Snackbar.LENGTH_SHORT).show();
        } else {
            MySession.setKorisnik(korisnik);
            Snackbar.make(getView(), R.string.new_pw_succ, Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    getDialog().dismiss();
                }
            }).show();
        }
    }
}
