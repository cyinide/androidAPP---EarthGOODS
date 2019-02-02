package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.Blocs;
import com.example.guestuser.damirkrkalicearthgoods.data.RegisterUser;
import com.example.guestuser.damirkrkalicearthgoods.data.UserVM;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyBlocsBinder;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyFragmentHelper;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyUtils;

import java.util.List;

public class UserChangeAddressFragment extends DialogFragment {

    public static String Tag = "userChangeAddressFragment";

    private TextInputEditText newAddresInput;
    private Spinner blockSpinner;
    private Button btnCancel;
    private Button btnSave;
    private UserVM user;
    private TextView textCurrentAddress;
    private View view;
    private ProgressBar progressBar;

    public static UserChangeAddressFragment newInstance(){
        UserChangeAddressFragment fragment = new UserChangeAddressFragment();
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
        view = inflater.inflate(R.layout.change_address_layout, container, false);

        user = MySession.getKorisnik();

        textCurrentAddress = view.findViewById(R.id.tvProfilOpcijeTrenutnaAdresa);
        textCurrentAddress.setText(user.getAdresa() + ", " + user.getLokacija());

        progressBar = view.findViewById(R.id.progressBar_snimiPromjene);

        newAddresInput = view.findViewById(R.id.textProfilOpcijeAdresaNew);

        blockSpinner = view.findViewById(R.id.spinnerProfilOpcijeAdresaBlok);

        MyApiRequest.get(MyApiRequest.ENDPOINT_LOCATIONS, new MyAbstractRunnable<Blocs>() {
            @Override
            public void run(Blocs Blocs) {
                List<String> blokPodaci = MyBlocsBinder.getStringListBlokovi(Blocs);
                onBlocDataRecieved(blokPodaci, null, null);
            }

            @Override
            public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                onBlocDataRecieved(null, statusCode, errorMessage);
            }
        });

        btnCancel = view.findViewById(R.id.btnProfilAdresaOdustani);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btnSave = view.findViewById(R.id.btnProfilAdresaSnimi);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (newAddresInput == null || newAddresInput.length() <= 4) {
                    newAddresInput.setError(getString(R.string.err_address));
                    Snackbar.make(getView(), getString(R.string.invalid_data) , Snackbar.LENGTH_SHORT).show();
                    return;
                }

                RegisterUser userPostObj = new RegisterUser(user);
                userPostObj.setAdresa(newAddresInput.getText().toString());
                userPostObj.setBlokID(MyBlocsBinder.getBlocs().get(blockSpinner.getSelectedItemPosition()).getId());

                MyApiRequest.post(MyApiRequest.endpoint_update_user, userPostObj, new MyAbstractRunnable<UserVM>() {
                    @Override
                    public void run(UserVM userVM) {
                        updateLocation(userVM, null, null);
                    }

                    @Override
                    public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                        updateLocation(null, statusCode, errorMessage);
                    }
                });
            }
        });

        return view;
    }

    private void updateLocation(@Nullable UserVM korisnik, @Nullable Integer statusCode, @Nullable String errorMessage) {
        progressBar.setVisibility(View.INVISIBLE);

        if (korisnik == null) {
            Snackbar.make(getView(), getString(R.string.try_again_err) , Snackbar.LENGTH_SHORT).show();
        } else {
            MySession.setKorisnik(korisnik);
            Snackbar.make(getView(), R.string.add_changed_succ, Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    MyFragmentHelper.refreshFragment((AppCompatActivity)getActivity(),UserInformationFragment.Tag);
                    getDialog().dismiss();

                }
            }).show();
        }
    }

    private void onBlocDataRecieved(@Nullable List<String> blokPodaci, @Nullable Integer statusCode, @Nullable String errorMessage) {
        if (view.findViewById(R.id.progressBar_blokSpinner) != null) {
            view.findViewById(R.id.progressBar_blokSpinner).setVisibility(View.INVISIBLE);
        }

        if (blokPodaci != null) {
            MyUtils.popuniSpinner(getActivity(),
                    blokPodaci,
                    android.R.layout.simple_spinner_item,
                    android.R.layout.simple_spinner_dropdown_item,
                    blockSpinner);

            blockSpinner.setSelection(blokPodaci.indexOf(user.getLokacija()));
        } else {
            try {
                Snackbar.make(getActivity().findViewById(R.id.fragmentContainer),
                        getString(R.string.try_again_err),
                        Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
