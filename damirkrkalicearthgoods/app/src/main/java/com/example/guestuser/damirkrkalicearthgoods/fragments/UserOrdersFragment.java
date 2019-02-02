package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.LoginUser;
import com.example.guestuser.damirkrkalicearthgoods.data.OrderItemVM;
import com.example.guestuser.damirkrkalicearthgoods.data.OrderVM;
import com.example.guestuser.damirkrkalicearthgoods.data.OrdersDisplayVM;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserOrdersFragment extends Fragment {

    private static final String QUERY = "query_data";
    public static String Tag = "userOrdersFragment";

    private SearchView searchViewOrders;
    private ImageButton btnOrdersClose;
    private ListView listViewOrders;


    private List<OrderVM> data;
    private List<OrderVM> initialData;
    private BaseAdapter listOrdersAdapter;
    private TextView noDataMessage;
    private ProgressBar progressBar;
    private String predefinedQuery = "";

    public static UserOrdersFragment newInstance(String query) {
        UserOrdersFragment fragment = new UserOrdersFragment();

        Bundle args = new Bundle();
        args.putString(QUERY, query);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(QUERY)) {
            predefinedQuery = getArguments().getString(QUERY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_orders_list_fragment_layout, container, false);

        progressBar = view.findViewById(R.id.progressBar_narudzbaList);
        noDataMessage = view.findViewById(R.id.profilnarudzbeNoData);

        btnOrdersClose = view.findViewById(R.id.btnNarudzbeClose);
        btnOrdersClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        searchViewOrders = view.findViewById(R.id.searchViewNarudzbe);
        searchViewOrders.setQuery(predefinedQuery, false);
        searchViewOrders.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateNarudzbaList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateNarudzbaList(newText);
                return true;
            }
        });

        listViewOrders = view.findViewById(R.id.listViewNarudzbe);
        LoginUser credentialsObj = new LoginUser(MySession.getKorisnik().getUsername(), MySession.getKorisnik().getPassword());
        MyApiRequest.post(MyApiRequest.endpoint_orders, credentialsObj, new MyAbstractRunnable<OrdersDisplayVM>() {
            @Override
            public void run(OrdersDisplayVM ordersDisplayVM) {
                onNarudzbaListReceived(ordersDisplayVM, null, null);
            }

            @Override
            public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                onNarudzbaListReceived(null, statusCode, errorMessage);
            }
        });


        return view;
    }

    private void onNarudzbaListReceived(@Nullable OrdersDisplayVM narudzbePodaci, @Nullable Integer statusCode, @Nullable String errorMessage) {
        progressBar.setVisibility(View.INVISIBLE);

        int noDataTextVisibility = narudzbePodaci != null &&
                    narudzbePodaci.narudzbe != null &&
                    narudzbePodaci.narudzbe.size() > 0
                ? View.INVISIBLE: View.VISIBLE;
        noDataMessage.setVisibility(noDataTextVisibility);

        if (narudzbePodaci != null) {
            initialData = narudzbePodaci.narudzbe;
            if (predefinedQuery != null && predefinedQuery.length() > 0 ) {
                data = getFiltered(predefinedQuery);
            } else {
                data = initialData;
            }
            popuniPodatke();
        }
    }

    private void updateNarudzbaList(String query) {
        data = getFiltered(query);
        listOrdersAdapter.notifyDataSetChanged();
    }

    private List<OrderVM> getFiltered(String query) {
        List<OrderVM> filtered = new ArrayList<OrderVM>();

        for (OrderVM n: initialData) {
            if (n.getSearchIndex().contains(query.toLowerCase())) {

            }
        }
        return filtered;
    }

    private void popuniPodatke() {
        listOrdersAdapter = new BaseAdapter() {
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
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater != null ? inflater.inflate(R.layout.user_order_item_layout, parent, false) : null;
                }
                OrderVM n = data.get(position);


                TextView textStavkaNarudzbaDatum = view.findViewById(R.id.textStavkaNarudzbaDatum);
                textStavkaNarudzbaDatum.setText(n.getDatumNapravljenaString() + " | ");

                TextView textStavkaNarudzbaTime = view.findViewById(R.id.textStavkaNarudzbaTime);
                textStavkaNarudzbaTime.setText( n.getTimeString() + " | " );

                TextView textStavkaNarudzbaCijena = view.findViewById(R.id.textStavkaNarudzbaCijena);
                textStavkaNarudzbaCijena.setText(getString(R.string.price, n.getTotalPrice()));

                try {

                    Set<OrderItemVM> hrana = new HashSet<OrderItemVM>(n.getOrderItems());
                    ChipGroup chipGroupHrana = view.findViewById(R.id.chipgroupStavkaNarudzbaHrana);
                    chipGroupHrana.removeAllViews();
                    chipGroupHrana.setFocusable(false);
                    for (OrderItemVM stavka :
                            hrana) {
                        Chip c = new Chip(getActivity());
                        c.setText("x" + stavka.getKolicina() + " " + stavka.getNaziv());
                        c.setFocusable(false);
                        c.setClickable(false);
                        chipGroupHrana.addView(c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return view;}

        };

        listViewOrders.setAdapter(listOrdersAdapter);

        listViewOrders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setMessage("Are you sure about dismissing this order?");
                dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApiRequest.post(String.format(MyApiRequest.endpoint_order_delete, data.get(position).getId()),
                                new LoginUser(MySession.getKorisnik().getUsername(), MySession.getKorisnik().getPassword()),
                                new MyAbstractRunnable<String>() {
                                    @Override
                                    public void run(String response) {
                                        onNarudzbaDeleted(response, null, null);
                                        data.remove(data.get(position));
                                        listOrdersAdapter.notifyDataSetChanged();

                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                                        onNarudzbaDeleted(null, statusCode, errorMessage);
                                        dialog.dismiss();
                                    }
                                });
                        dialog.dismiss();
                    }
                });

                dlg.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dlg.show();


                return false;
            }
        });
    }

    private void onNarudzbaDeleted(@Nullable String response , @Nullable Integer statusCode, @Nullable String errorMessage) {
        if (response == null) {
            Snackbar.make(getView(), R.string.order_err, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getView(), R.string.order_delete, Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
            }).show();
        }
    }
}
