package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.guestuser.damirkrkalicearthgoods.MarketDetailsActivity;
import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.LoginUser;
import com.example.guestuser.damirkrkalicearthgoods.data.Market;
import com.example.guestuser.damirkrkalicearthgoods.data.MarketDisplayVM;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;


import java.util.List;

import static com.example.guestuser.damirkrkalicearthgoods.MarketDetailsActivity.DETAIL_VIEW_RESTORAN;
import static com.example.guestuser.damirkrkalicearthgoods.MarketDetailsActivity.DETAIL_VIEW_RESTORAN_FRAGMENT_FLAG;

public class MarketMainListFragment extends Fragment {
    public static String Tag = "marketMainListFragment";
    private static final String FILTER_FAVOURITE = "favourite_markets";

    private ListView marketList;
    private List<Market> data;
    private BaseAdapter marketListAdapter;

    private ProgressBar progressBar;
    private TextView noFavMessage;

    private boolean showFavouriteOnly;
    private View listView;


    public static MarketMainListFragment newInstance(boolean filterByOmiljeni) {
        MarketMainListFragment fragment = new MarketMainListFragment();

        Bundle args = new Bundle();
        args.putBoolean(FILTER_FAVOURITE, filterByOmiljeni);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(FILTER_FAVOURITE)) {
            showFavouriteOnly = getArguments().getBoolean(FILTER_FAVOURITE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listView = inflater.inflate(R.layout.market_list_fragment_layout, container, false);

        progressBar = listView.findViewById(R.id.progressBar_restoraniList);
        noFavMessage = listView.findViewById(R.id.textNemateOmiljenih);

        marketList = listView.findViewById(R.id.listViewRestorani);

        restoraniListRequest();

        marketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                do_transitionCardView(view, data.get(position),  MarketDetailsActivity.DETAIL_VIEW_GOTO_INFO);
            }
        });

        return listView;
    }

    private void restoraniListRequest() {
        MyApiRequest.get(MyApiRequest.endpoint_markets, new MyAbstractRunnable<MarketDisplayVM>() {
            @Override
            public void run(MarketDisplayVM marketDisplayVM) {
                popuniPodatke(marketDisplayVM);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                Snackbar.make(getActivity().findViewById(R.id.fragmentRestoranListContainer),
                        "Error occured, please try again.",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.try_again), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                restoraniListRequest();
                            }
                        })
                        .show();
            }
        });
    }

    private void do_transitionCardView(View view, Market restoran, String fragmentFlag) {
         Intent intent = new Intent(getActivity(), MarketDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(DETAIL_VIEW_RESTORAN, restoran);
            bundle.putSerializable(DETAIL_VIEW_RESTORAN_FRAGMENT_FLAG, fragmentFlag);
            intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popuniPodatke(MarketDisplayVM model) {
        if (showFavouriteOnly) {
            data = model.getOmiljeniRestorani();
        } else {
            data = model.getRestorani();
        }

        if (data == null || data.size() == 0) {
            noFavMessage.setVisibility(View.VISIBLE);
        }

        marketListAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) { return data.get(position); }

            @Override
            public long getItemId(int position) {
                return data.get(position).getId();
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    view = inflater.inflate(R.layout.market_item_layout, parent, false);
                }

                Market market = data.get(position);

                ProgressBar progressBar_restoranLike = view.findViewById(R.id.progressBar_restoranLike);

                TextView restoranStatsCount = view.findViewById(R.id.textStavkaRestoranStats);

                ImageButton btnStavkaRestoranLike = view.findViewById(R.id.btnStavkaRestoranLike);
                btnStavkaRestoranLike.setImageResource(market.userHasLiked() ?
                                R.drawable.ic_thumb_up : R.drawable.ic_thumb_up_default);

                btnStavkaRestoranLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar_restoranLike.setVisibility(View.VISIBLE);
                        btnStavkaRestoranLike.setVisibility(View.INVISIBLE);
                        if (market.userHasLiked()) {
                            MyApiRequest.post(String.format(MyApiRequest.endpoint_dislike, market.getId()),
                                new LoginUser(MySession.getKorisnik().getUsername(), MySession.getKorisnik().getPassword()),
                                new MyAbstractRunnable<String>() {
                                    @Override
                                    public void run(String response) {
                                        LikeOrDislikeFunc(response,
                                                null,
                                                null,
                                                progressBar_restoranLike,
                                                restoranStatsCount,
                                                btnStavkaRestoranLike,
                                                market);
                                    }

                                    @Override
                                    public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                                        LikeOrDislikeFunc(null,
                                                statusCode, errorMessage,
                                                progressBar_restoranLike,
                                                null,
                                                null,
                                                null);
                                    }
                                });
                        } else {
                            MyApiRequest.post(String.format(MyApiRequest.endpoint_likes, market.getId()),
                                new LoginUser(MySession.getKorisnik().getUsername(), MySession.getKorisnik().getPassword()),
                                new MyAbstractRunnable<String>() {
                                    @Override
                                    public void run(String response) {
                                        LikeOrDislikeFunc(response, null, null, progressBar_restoranLike,restoranStatsCount, btnStavkaRestoranLike, market);
                                    }

                                    @Override
                                    public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                                        LikeOrDislikeFunc(null, statusCode, errorMessage, progressBar_restoranLike, null,null, null);
                                    }
                            });
                        }

                    }
                });

                TextView restoranNaziv = view.findViewById(R.id.textStavkaRestoranNaziv);
                TextView restoranOpis = view.findViewById(R.id.textStavkaRestoranOpis);
                TextView textStavkaRestoranLokacija = view.findViewById(R.id.textStavkaRestoranLokacija);

                MaterialButton btnStavkaRestoranJelovnik = view.findViewById(R.id.btnStavkaRestoranJelovnik);
                btnStavkaRestoranJelovnik.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        do_transitionCardView(listView, market, MarketDetailsActivity.DETAIL_VIEW_GOTO_JELOVNIK);
                    }
                });
                restoranNaziv.setText(market.getNaziv());

                int limit = 120;
                String opis;
                if (market.getOpis().length() > limit ) {
                    opis = market.getOpis().substring(0, limit - 3) + "...";
                } else {
                    opis = market.getOpis();
                }
                restoranOpis.setText(opis);

                restoranStatsCount.setText(getString(R.string.liked_by, market.getLikesCount()));

                textStavkaRestoranLokacija.setText(market.getLokacija());

                return view;
            }

        };

        marketList.setAdapter(marketListAdapter);
    }

    private void LikeOrDislikeFunc(@Nullable String response,
                                   @Nullable Integer statusCode,
                                   @Nullable String errorMessage,
                                   @Nullable ProgressBar progressBar,
                                   @Nullable TextView restoranStatsCount,
                                   ImageButton btnStavkaRestoranLike,
                                   Market restoran
    ) {
        try {
            progressBar.setVisibility(View.INVISIBLE);
            btnStavkaRestoranLike.setVisibility(View.VISIBLE);
            if (response != null) {
                btnStavkaRestoranLike.setImageResource(restoran.toggleLike() ?
                        R.drawable.ic_thumb_up : R.drawable.ic_thumb_up_default);
                restoranStatsCount.setText(getString(R.string.liked_by, restoran.getLikesCount()));

                Snackbar.make(getActivity().findViewById(R.id.fragmentRestoranListContainer),
                        response,
                        Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getActivity().findViewById(R.id.fragmentRestoranListContainer),
                        errorMessage != null ? errorMessage : getString(R.string.error),
                        Snackbar.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
