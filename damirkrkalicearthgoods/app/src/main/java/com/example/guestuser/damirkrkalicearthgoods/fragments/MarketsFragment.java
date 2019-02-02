package com.example.guestuser.damirkrkalicearthgoods.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guestuser.damirkrkalicearthgoods.R;
import com.example.guestuser.damirkrkalicearthgoods.data.Market;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyFragmentHelper;

import static com.example.guestuser.damirkrkalicearthgoods.MarketDetailsActivity.DETAIL_VIEW_RESTORAN;


public class MarketsFragment extends Fragment {
    public static String Tag = "marketsFragment";

    private Market market;

    private ImageButton btnCloseMarket;
    private ImageView marketImage;

    private TextView marketName, likedBy, marketTitle;


    public static MarketsFragment newInstance(Market restoran) {
        MarketsFragment fragment = new MarketsFragment();

        Bundle args = new Bundle();
        args.putSerializable(DETAIL_VIEW_RESTORAN, restoran);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_information_layout, container, false);
        market = (Market) getArguments().getSerializable(DETAIL_VIEW_RESTORAN);

        btnCloseMarket = view.findViewById(R.id.btnRestoranClose);
        btnCloseMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


       marketImage = view.findViewById(R.id.imageDetaljnoRestoranSlika);
        Glide.with(this)
                .load(market.getSlika())
                .centerCrop()
                .into(marketImage);

        marketName = view.findViewById(R.id.textDetaljnoRestoranNaziv);
        marketName.setText(market.getNaziv());

        marketTitle = view.findViewById(R.id.titleDetaljnoRestoranNaziv);
        marketTitle.setText(market.getNaziv());

        likedBy = view.findViewById(R.id.textDetaljnoRestoranStats);
        likedBy.setText(getString(R.string.liked_by, market.getLikesCount()));


        bindData();

        return view;
    }

    private void bindData() {

        MyFragmentHelper.fragmentReplace((AppCompatActivity)getActivity(),R.id.detailsContainer,MarketDetailsFragment.newInstance(market),"ResAbout",false);


    }

}
