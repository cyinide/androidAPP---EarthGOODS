package com.example.guestuser.damirkrkalicearthgoods;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.guestuser.damirkrkalicearthgoods.data.Market;
import com.example.guestuser.damirkrkalicearthgoods.fragments.CartFragment;
import com.example.guestuser.damirkrkalicearthgoods.fragments.MarketMenuFragment;
import com.example.guestuser.damirkrkalicearthgoods.fragments.MarketsFragment;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyFragmentHelper;


public class MarketDetailsActivity extends AppCompatActivity {

    public static String DETAIL_VIEW_RESTORAN = "detailViewRestoran";

    public static String DETAIL_VIEW_GOTO_INFO = "gotoDetaljnoRestoranInfo";
    public static String DETAIL_VIEW_GOTO_JELOVNIK = "gotoDetaljnoRestoranJelovnik";
    public static String DETAIL_VIEW_RESTORAN_FRAGMENT_FLAG = "detailviewRestoranFragment";

    BottomNavigationView bottomnavigationRestoranDetaljno;

    private Market restoran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_details_activity_layout);

        Bundle args = getIntent().getExtras();
        restoran = (Market) args.getSerializable(DETAIL_VIEW_RESTORAN);

        if (args.getString(DETAIL_VIEW_RESTORAN_FRAGMENT_FLAG).equals(DETAIL_VIEW_GOTO_JELOVNIK)) {
            MyFragmentHelper.fragmentReplace(this,
                    R.id.fragmentContainer,
                    MarketMenuFragment.newInstance(restoran),
                    MarketsFragment.Tag,
                    false);
        } else {
            MyFragmentHelper.fragmentReplace(this,
                    R.id.fragmentContainer,
                    MarketsFragment.newInstance(restoran),
                    MarketsFragment.Tag,
                    false);
        }

        bottomnavigationRestoranDetaljno = findViewById(R.id.bottomnavigationRestoranDetaljno);
        bottomnavigationRestoranDetaljno.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.nav_bottom_restoran_jelovnik:
                        MyFragmentHelper.fragmentReplace(MarketDetailsActivity.this,
                                R.id.fragmentContainer,
                                MarketMenuFragment.newInstance(restoran),
                                MarketMenuFragment.Tag,
                                false);
                        break;
                    case R.id.nav_bottom_restoran_vise:
                        MyFragmentHelper.fragmentReplace(MarketDetailsActivity.this,
                                R.id.fragmentContainer,
                                MarketsFragment.newInstance(restoran),
                                MarketsFragment.Tag,
                                false);
                        break;
                    case R.id.nav_bottom_restoran_korpa:
                        MyFragmentHelper.fragmentReplace(MarketDetailsActivity.this,
                                R.id.fragmentContainer,
                                CartFragment.newInstance(true),
                                CartFragment.Tag,
                                false);
                        break;
                    default:
                        return false;
                }

                menuItem.setChecked(true);
                return true;
            }
        });
    }
}
