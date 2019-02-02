package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.KitchenType;
import com.example.guestuser.damirkrkalicearthgoods.data.Market;

public class MarketDetailsFragment extends Fragment {
    private static final String MARKET_DETAILS = "marketSelected";
    public static final String PAGE_TITLE = "About";

    private TextView marketDescription;
    private TextView marketAddress;
    private TextView marketLocation;
    private TextView marketPhone;
    private TextView marketWeb;
    private TextView marketEmail;
    private TextView ownerFullname;
    private TextView ownerEmail;
    private ChipGroup foodTypes;
    private Market market;

    public static MarketDetailsFragment newInstance(Market market) {
        MarketDetailsFragment fragment = new MarketDetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable(MARKET_DETAILS, market);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(MARKET_DETAILS)) {
            market = (Market) getArguments().getSerializable(MARKET_DETAILS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_details_layouit, container, false);

        marketDescription = view.findViewById(R.id.textDetaljnoRestoranOpis);
        marketDescription.setText(market.getOpis());

        foodTypes = view.findViewById(R.id.chipGroupRestoranTipovihrane);
        prepareFoodTypes();

        marketAddress = view.findViewById(R.id.textDetaljnoRestoranAdresa);
        marketAddress.setText(market.getAdresa());

        marketLocation = view.findViewById(R.id.textDetaljnoRestoranLokacija);
        marketLocation.setText(market.getLokacija());

        marketPhone = view.findViewById(R.id.textDetaljnoRestoranTelefon);
        marketPhone.setText(market.getTelefon());

        marketWeb = view.findViewById(R.id.textDetaljnoRestoranWebUrl);
        marketWeb.setText(market.getWebUrl());

        marketEmail = view.findViewById(R.id.textDetaljnoRestoranEmail);
        marketEmail.setText(market.getEmail());

        // Vlasnik section
        ownerFullname = view.findViewById(R.id.textDetaljnoRestoranVlasnikImePrezime);
        ownerFullname.setText(market.getVlasnik().getImePrezime());

        ownerEmail = view.findViewById(R.id.textDetaljnoRestoranVlasnikEmail);
        ownerEmail.setText(market.getEmail());

        return view;
    }

    private void prepareFoodTypes() {
        foodTypes.removeAllViews();
        foodTypes.setFocusable(false);
        try {
            for (KitchenType t:
                    market.getTipoviKuhinje()) {
                Chip c = new Chip(getActivity());
                c.setText(t.naziv);
                c.setFocusable(false);
                c.setClickable(false);
                foodTypes.addView(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
