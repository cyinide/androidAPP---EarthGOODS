package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.Cart;
import com.example.guestuser.damirkrkalicearthgoods.data.CartItemVM;
import com.example.guestuser.damirkrkalicearthgoods.data.FoodDisplayVM;
import com.example.guestuser.damirkrkalicearthgoods.data.FoodItemVM;
import com.example.guestuser.damirkrkalicearthgoods.data.Market;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;

import java.util.ArrayList;
import java.util.List;

public class MarketMenuFragment extends Fragment {
    public static String Tag = "marketMenuFragment";

    private static final String MARKET_MENU = "marketMenu";

    private Cart cart;
    private Market market;

    private ListView listViewFood;
    private List<FoodItemVM> data;
    private List<FoodItemVM> initialData;
    private BaseAdapter listFoodAdapter;

    private ProgressBar progressBar;
    private TextView marketMenuNoDataMessage;
    private ImageButton btnCloseMenu;
    private TextView titleCart;

    public static MarketMenuFragment newInstance(Market restoran) {
        MarketMenuFragment fragment = new MarketMenuFragment();

        Bundle args = new Bundle();
        args.putSerializable(MARKET_MENU, restoran);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(MARKET_MENU)) {
            market = (Market) getArguments().getSerializable(MARKET_MENU);
        }
    }

    @Override
    public void onResume() {
        getKorpaSession();
        super.onResume();
    }

    private void getKorpaSession() {
        if (MySession.getKorpa() == null) {
            MySession.setKorpa(new Cart());
        }
        cart = MySession.getKorpa();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getKorpaSession();
        View view = inflater.inflate(R.layout.market_menu_layout, container, false);

        titleCart = view.findViewById(R.id.titleKorpaNaziv);
        titleCart.setText(getString(R.string.menu_bar, market.getNaziv()));

        progressBar = view.findViewById(R.id.progressBar_hranaList);

        btnCloseMenu = view.findViewById(R.id.btnJelovnikClose);
        btnCloseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getActivity().onBackPressed();
                } catch (NullPointerException e) {
                    //e.printStackTrace();
                }
            }
        });

        marketMenuNoDataMessage = view.findViewById(R.id.restoranJelovnikNoData);

        SearchView searchViewPretraga = view.findViewById(R.id.searchViewPretraga);
        searchViewPretraga.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterListViewHrana(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterListViewHrana(newText);
                return true;
            }
        });

        listViewFood = view.findViewById(R.id.listViewHrana);

        MyApiRequest.get(String.format(MyApiRequest.endpoint_food, market.getId()),
                new MyAbstractRunnable<FoodDisplayVM>() {
            @Override
            public void run(FoodDisplayVM foodDisplayVM) {
                onHranaListReceived(foodDisplayVM, null, null);
            }

            @Override
            public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                onHranaListReceived(null, statusCode, errorMessage);
            }
        });

        return view;
    }

    private void onHranaListReceived(@Nullable FoodDisplayVM hranaPodaci, @Nullable Integer statusCode, @Nullable String errorMessage) {
        progressBar.setVisibility(View.INVISIBLE);

        int noDataTextVisibility = hranaPodaci != null && hranaPodaci.hrana != null && hranaPodaci.hrana.size() > 0 ? View.INVISIBLE: View.VISIBLE;
        marketMenuNoDataMessage.setVisibility(noDataTextVisibility);

        if (hranaPodaci != null) {
            data = initialData = hranaPodaci.hrana;
            popuniPodatke();
        }
    }

    private void filterListViewHrana(String query) {
        try {
            List<FoodItemVM> filtered = new ArrayList<FoodItemVM>();

            for (FoodItemVM n: initialData) {
                if (n.getSearchIndex().contains(query.toLowerCase())) {
                    filtered.add(n);
                }
            }

            data = filtered;
            listFoodAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popuniPodatke() {
        listFoodAdapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return getCount();
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    view = inflater != null ? inflater.inflate(R.layout.market_menu_item_layout, parent, false) : null;
                }

                FoodItemVM hranaStavka = data.get(position);

                LinearLayout stavkaJelovnikRoot = view.findViewById(R.id.stavkaJelovnikRoot);

                TextView textStavkaJelovnikNaziv = view.findViewById(R.id.textStavkaJelovnikNaziv);
                textStavkaJelovnikNaziv.setText(hranaStavka.getNaziv());

                TextView textStavkaJelovnikOpis = view.findViewById(R.id.textStavkaJelovnikOpis);
                textStavkaJelovnikOpis.setText(hranaStavka.getOpis());

                TextView textStavkaJelovnikCijena = view.findViewById(R.id.textStavkaJelovnikCijena);
                textStavkaJelovnikCijena.setText(getString(R.string.price, hranaStavka.getCijena()));


                LinearLayout jelovnikStavkaAddedStatsHolder = view.findViewById(R.id.jelovnikStavkaAddedStatsHolder);
                TextView textStavkaJelovnikStats = view.findViewById(R.id.textStavkaJelovnikStats);


                ImageButton btnDodajKorpaStavku = view.findViewById(R.id.btnDodajKorpaStavku);
                btnDodajKorpaStavku.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartItemVM s = do_addItem(hranaStavka);
                        stavkaJelovnikRoot.setPressed(true);
                        stavkaJelovnikRoot.setPressed(false);
                        updateCostInfo(s,  jelovnikStavkaAddedStatsHolder, textStavkaJelovnikStats, btnDodajKorpaStavku);
                    }
                });

                ImageButton btnUkloniKorpaStavku = view.findViewById(R.id.btnUkloniKorpaStavku);
                btnUkloniKorpaStavku.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartItemVM s = do_deleteItem(hranaStavka);
                        stavkaJelovnikRoot.setPressed(true);
                        stavkaJelovnikRoot.setPressed(false);
                        updateCostInfo(s,  jelovnikStavkaAddedStatsHolder, textStavkaJelovnikStats, btnDodajKorpaStavku);
                    }
                });


                CartItemVM korpaStavka = cart.getStavka(hranaStavka);
                if (korpaStavka != null) {
                    updateCostInfo(korpaStavka, jelovnikStavkaAddedStatsHolder, textStavkaJelovnikStats, btnDodajKorpaStavku);
                }

                return view;
            }
        };

        listViewFood.setAdapter(listFoodAdapter);
    }

    private CartItemVM do_addItem(FoodItemVM stavka) {
        CartItemVM s = cart.dodajStavku(stavka);
        MySession.setKorpa(cart);
        return s;
    }

    private CartItemVM do_deleteItem(FoodItemVM stavka) {
        CartItemVM s = cart.ukloniStavku(stavka);
        MySession.setKorpa(cart);
        return s;
    }


    private void updateCostInfo(CartItemVM korpaStavka, LinearLayout statsContainer, TextView stanje, ImageButton btn) {
        if (cart.getHranaStavke().contains(korpaStavka)) {
            stanje.setText(getString(R.string.cost, korpaStavka.getKolicina(), korpaStavka.getUkupnaCijena()));
            btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus_round_added));

            statsContainer.setVisibility(View.VISIBLE);
        } else {
            btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus_round));
            statsContainer.setVisibility(View.GONE);
        }
    }
}
