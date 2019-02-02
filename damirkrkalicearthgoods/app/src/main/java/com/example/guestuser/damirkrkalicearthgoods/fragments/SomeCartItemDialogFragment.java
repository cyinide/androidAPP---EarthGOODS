package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.CartItemVM;


public class SomeCartItemDialogFragment extends DialogFragment {

    public static String Tag = "someCartItemDialogFragment";

    private static String KORPA_ITEM = "korpa_item";
    private CartItemVM stavka;

    private TextView textStavkaKorpaDialogTitle,
            textStavkaKorpaDialogNaziv,
            textStavkaKorpaDialogOpis,
            textStavkaKorpaDialogCijena,
            textStavkaKorpaDialogKolicina,
            textStavkaKorpaDialogRestoran;
    private Button btnStavkaKorpaDialogUkloni;
    private ImageButton btnStavkaKorpaDialogNazad;
    private ImageView imageStavkaKorpaDialog;

    public static SomeCartItemDialogFragment newInstance(CartItemVM obj) {
        Bundle args = new Bundle();
        args.putSerializable(KORPA_ITEM, obj);

        SomeCartItemDialogFragment fragment = new SomeCartItemDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogsTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_item_dialog, container, false);

        stavka = (CartItemVM) getArguments().getSerializable(KORPA_ITEM);

        textStavkaKorpaDialogTitle = view.findViewById(R.id.textStavkaKorpaDialogTitle);
        textStavkaKorpaDialogTitle.setText(stavka.getFoodItemVM().getNaziv());

        textStavkaKorpaDialogNaziv = view.findViewById(R.id.textStavkaKorpaDialogNaziv);
        textStavkaKorpaDialogNaziv.setText(stavka.getFoodItemVM().getNaziv());

        textStavkaKorpaDialogCijena = view.findViewById(R.id.textStavkaKorpaDialogCijena);
        textStavkaKorpaDialogCijena.setText(getString(R.string.price, stavka.getFoodItemVM().getCijena()));

        textStavkaKorpaDialogKolicina = view.findViewById(R.id.textStavkaKorpaDialogKolicina);
        textStavkaKorpaDialogKolicina.setText(getString(R.string.item_quantity, stavka.getKolicina()));

        textStavkaKorpaDialogRestoran = view.findViewById(R.id.textStavkaKorpaDialogRestoran);

        textStavkaKorpaDialogOpis = view.findViewById(R.id.textStavkaKorpaDialogOpis);
        textStavkaKorpaDialogOpis.setText(stavka.getFoodItemVM().getOpis());

        btnStavkaKorpaDialogNazad = view.findViewById(R.id.btnStavkaKorpaDialogNazad);
        btnStavkaKorpaDialogNazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnStavkaKorpaDialogUkloni = view.findViewById(R.id.btnStavkaKorpaDialogUkloni);
        btnStavkaKorpaDialogUkloni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stavka.umanjiKolicinu();
                // deal with this
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
