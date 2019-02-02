package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.guestuser.damirkrkalicearthgoods.LoginActivity;
import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.RegisterUser;
import com.example.guestuser.damirkrkalicearthgoods.data.UserVM;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApp;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyFragmentHelper;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;

public class UserInformationFragment extends Fragment {

    public static String Tag = "userInformationFragment";

    private TextView fullNameHeader;
    private TextView fullname;
    private TextView address;
    private TextView username;
    private TextView dateRegistered;
    private TextView totalOrders;
    private TextView lastLogged;
    private TextView btnMyOrders;
    private TextView btnChangePassword;
    private TextView btnDeleteAccount;
    private TextView btnChangeAddress;
    private TextView token;


    public static UserInformationFragment newInstance() {
        UserInformationFragment fragment = new UserInformationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_information_fragment_layout, container, false);

        UserVM korisnik = MySession.getKorisnik();
        fullNameHeader = view.findViewById(R.id.textProfilImePrezimeHeader);
        fullNameHeader.setText(korisnik.getImePrezime());

        fullname = view.findViewById(R.id.textProfilImePrezime);
        fullname.setText(getString(R.string.user_fullname, korisnik.getImePrezime()));

        address = view.findViewById(R.id.textProfilAdresa);
        address.setText(getString(R.string.user_info_address, korisnik.getAdresaLokacija()));

        username = view.findViewById(R.id.textProfilUsername);
        username.setText(getString(R.string.user_info_username, korisnik.getUsername()));

        dateRegistered = view.findViewById(R.id.textProfilDatumRegistracije);
        dateRegistered.setText(getString(R.string.date_reg, korisnik.getDatumRegistracije()));

        totalOrders = view.findViewById(R.id.chipProfilUkupnoNarudzbi);
        totalOrders.setText(getString(R.string.total_orders, korisnik.getNarudzbeCount()));

        lastLogged = view.findViewById(R.id.chipProfilZadnjiLogin);
        lastLogged.setText(getString(R.string.last_logged, korisnik.getZadnjiLogin()));

        lastLogged = view.findViewById(R.id.chipProfilZadnjiLogin);
        lastLogged.setText(getString(R.string.last_logged, korisnik.getZadnjiLogin()));

        token = view.findViewById(R.id.textProfilToken);
        token.setText(getString(R.string.token, korisnik.getToken()));

        btnMyOrders = view.findViewById(R.id.btnProfilMojeNarudzbe);
        btnMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentHelper.fragmentReplace((AppCompatActivity)getActivity(),
                        R.id.fragmentProfilContainer,
                        UserOrdersFragment.newInstance(""),
                        UserOrdersFragment.Tag,
                        true);
            }
        });

        btnChangeAddress = view.findViewById(R.id.btnChangeAddress);
        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentHelper.dodajDialog((AppCompatActivity)getActivity(),
                        UserOrdersFragment.Tag,
                        UserChangeAddressFragment.newInstance());

            }
        });

        btnChangePassword = view.findViewById(R.id.btnProfilChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentHelper.dodajDialog((AppCompatActivity)getActivity(),
                        UserOrdersFragment.Tag,
                        UserChangePasswordFragment.newInstance());
            }
        });

        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dlgBuilder = new AlertDialog.Builder((AppCompatActivity)getActivity());
                dlgBuilder.setTitle(R.string.delete_acc)
                        .setMessage(getString(R.string.delete_acc_title) +
                                getString(R.string.delete_acc_msg))
                        .setPositiveButton(R.string.btn_delete_acc, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RegisterUser userPostObj = new RegisterUser();
                                userPostObj.setId(korisnik.getId());

                                MyApiRequest.post(MyApiRequest.endpoint_delete_user, userPostObj, new MyAbstractRunnable<Object>() {
                                    @Override
                                    public void run(Object o) {
                                        onAccountDeleted(dialog,view,204, null);
                                    }

                                    @Override
                                    public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                                        onAccountDeleted(dialog,view, statusCode, errorMessage);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


        ImageButton btnProfilClose = view.findViewById(R.id.btnProfilClose);
        btnProfilClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void onAccountDeleted(@Nullable DialogInterface dialog,View view, @Nullable Integer statusCode, @Nullable String errorMessage) {
        if (view.findViewById(R.id.progressBar_snimiPromjene) != null) {
            view.findViewById(R.id.progressBar_snimiPromjene).setVisibility(View.INVISIBLE);
        }

        if (statusCode == 204) {
            Snackbar.make(getView(), R.string.acc_deleted, Snackbar.LENGTH_LONG).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    dialog.dismiss();

                    MySession.setKorisnik(null);
                    Intent intent = new Intent(MyApp.getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                }
            }).show();

        } else {
            Snackbar.make(getView(), getString(R.string.try_again_err) , Snackbar.LENGTH_LONG).show();
        }
    }

}
