package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.Cart;
import com.example.guestuser.damirkrkalicearthgoods.data.CartItemVM;
import com.example.guestuser.damirkrkalicearthgoods.data.LoginUser;
import com.example.guestuser.damirkrkalicearthgoods.data.OrderRequestVM;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyUrlConnection;


public class CartFragment extends Fragment {

    public static String Tag = "cartFragment";
    public static String RENDER_KORPA_APPBAR = "showCartAppBar";

    private Cart cart;

    private BaseAdapter itemListAdapter;
    private ListView cartItemsListView;

    private Button btnOrder;
    private MaterialButton btnClear;
    private ImageButton btnCartClose;
    private TextView textCartIntro, textCartTotal;
    private View view;
    private AppBarLayout appBarCart;
    private boolean renderCartAppBar;
    private RelativeLayout progresBar;

    public static CartFragment newInstance(boolean renderKorpaAppBar) {
        CartFragment fragment = new CartFragment();

        Bundle args = new Bundle();
        args.putBoolean(RENDER_KORPA_APPBAR, renderKorpaAppBar);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        if (getArguments().containsKey(RENDER_KORPA_APPBAR)) {
            renderCartAppBar = getArguments().getBoolean(RENDER_KORPA_APPBAR);
        }

        cart = prepareSessionCart();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cart_fragment_layout, container, false);

        appBarCart = view.findViewById(R.id.appbarKorpa);
        if (!renderCartAppBar) {
            appBarCart.getLayoutParams().height = 0;
        }

        progresBar = view.findViewById(R.id.progressBarKorpaHolder);
        textCartIntro = view.findViewById(R.id.textKorpaIntro);
        textCartTotal = view.findViewById(R.id.textKorpaTotal);

        cartItemsListView = view.findViewById(R.id.listKorpaStavke);
        cartItemsDataPrepare();
        cartItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CartItemVM stavka = cart.getHranaStavke().get(position);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setMessage("Are you sure about removing this product from the cart?");
                dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cart.getHranaStavke().remove(stavka);
                        MySession.setKorpa(cart);
                        itemListAdapter.notifyDataSetChanged();
                        prepareData();
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

        btnCartClose = view.findViewById(R.id.btnKorpaClose);
        btnCartClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnClear = view.findViewById(R.id.btnKorpaOdbaci);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_discardOrderClick();
            }
        });

        btnOrder = view.findViewById(R.id.btnKorpaNaruci);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnMakeOrderClick();
            }
        });

        prepareData();
        return view;
    }

    private void prepareData() {
        if(cart.getHranaStavke().size()==0) {
            textCartIntro.setVisibility(View.VISIBLE);
            textCartIntro.setText(getString(R.string.empty_cart));
        }
        textCartTotal.setText(cart.getUkupnaCijena() == 0 ? "-" : cart.getUkupnaCijena() + " KM");

        if (cart.getHranaStavke().size() == 0) {
            btnOrder.setEnabled(false);
            btnClear.setEnabled(false);
            btnClear.setTextColor(getResources().getColor(R.color.colorTypographySemiDark));
            btnClear.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorTypographySemiDark)));
        }
    }

    private void do_discardOrderClick() {
                MySession.setKorpa(null);
                cart = prepareSessionCart();
                prepareData();
    }

    private void do_btnMakeOrderClick() {
        progresBar.setVisibility(View.VISIBLE);

        MyApiRequest.request(MyApiRequest.endpoint_order_create,
                MyUrlConnection.HttpMethod.POST,
                new OrderRequestVM(
                        new LoginUser(MySession.getKorisnik().getUsername(), MySession.getKorisnik().getPassword()),
                        MySession.getKorpa()),
                new MyAbstractRunnable<String>() {
                    @Override
                    public void run(String s) {
                        onOrderCreated(s, null, null);
                    }

                    @Override
                    public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                        onOrderCreated(null, statusCode, errorMessage);
                    }
                });

        MySession.setKorpa(null);
        cart = prepareSessionCart();
    }

    private void onOrderCreated(@Nullable String response, @Nullable Integer statusCode, @Nullable String errorMessage) {
        progresBar.setVisibility(View.INVISIBLE);

        if (response != null) {
            MySession.setKorpa(null);
            cart = prepareSessionCart();
            prepareData();
            final AlertDialog.Builder dlgBuilder = new AlertDialog.Builder((AppCompatActivity)getActivity());
            dlgBuilder.setTitle(R.string.cart_created_title)
                    .setMessage(R.string.order_created_msg)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            Snackbar.make(getActivity().findViewById(R.id.content),
                    getString(R.string.order_error),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private Cart prepareSessionCart() {
        if (itemListAdapter != null ) {
            itemListAdapter.notifyDataSetChanged();
        }
        if (MySession.getKorpa() == null) {
            MySession.setKorpa(new Cart());
        }
        return MySession.getKorpa();
    }

    private void cartItemsDataPrepare() {
        itemListAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return cart.getHranaStavke().size();
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
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    view = inflater != null ? inflater.inflate(R.layout.cart_item_layout, parent, false) : null;
                }

                CartItemVM stavka = cart.getHranaStavke().get(position);

                TextView textStavkaKorpaSuper = view.findViewById(R.id.textStavkaKorpaSuper);
                textStavkaKorpaSuper.setText("Restoran " + stavka.getFoodItemVM().getRestoranNaziv());

                TextView textStavkaKorpaNaziv = view.findViewById(R.id.textStavkaKorpaNaziv);
                textStavkaKorpaNaziv.setText("x" + stavka.getKolicina() + " " + stavka.getFoodItemVM().getNaziv());

                TextView textStavkaKorpaOpis = view.findViewById(R.id.textStavkaKorpaOpis);
                textStavkaKorpaOpis.setText("Cijena " + stavka.getFoodItemVM().getCijena());

                TextView textStavkaKorpaCijena = view.findViewById(R.id.textStavkaKorpaCijena);
                textStavkaKorpaCijena.setText(stavka.getUkupnaCijena() == 0 ? "- KM" : stavka.getUkupnaCijena() + " KM");

                return view;
            }
        };

        cartItemsListView.setAdapter(itemListAdapter);
    }

}
