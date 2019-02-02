package com.example.guestuser.damirkrkalicearthgoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.guestuser.damirkrkalicearthgoods.fragments.CartFragment;
import com.example.guestuser.damirkrkalicearthgoods.fragments.MarketMainListFragment;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyAbstractRunnable;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiRequest;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyFragmentHelper;
import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;

    BottomNavigationView bottomnavigationMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MySession.getKorisnik() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        bottomnavigationMain = findViewById(R.id.bottomnavigationMain);
        bottomnavigationMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.bottommain_restorani:
                        MyFragmentHelper.fragmentReplace(MainActivity.this,
                                R.id.fragmentContainer,
                                MarketMainListFragment.newInstance(false),
                                MarketMainListFragment.Tag,
                                false);
                        break;
                    case R.id.bottommain_omiljeni_restorani:
                        MyFragmentHelper.fragmentReplace(MainActivity.this,
                                R.id.fragmentContainer,
                                MarketMainListFragment.newInstance(true),
                                MarketMainListFragment.Tag,
                                false);
                        break;
                    case R.id.bottommain_korpa:
                        MyFragmentHelper.fragmentReplace(MainActivity.this,
                                R.id.fragmentContainer,
                                CartFragment.newInstance(false),
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupMainNavigation();

        MyFragmentHelper.fragmentReplace(this,
                R.id.fragmentContainer,
                MarketMainListFragment.newInstance(false),
                MarketMainListFragment.Tag,
                false);
    }


    private void setupMainNavigation() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_nav, R.string.close_nav);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_restorani) {
            MyFragmentHelper.fragmentReplace(this,
                    R.id.fragmentContainer,
                    MarketMainListFragment.newInstance(false),
                    MarketMainListFragment.Tag,
                    false);
        } else if (id == R.id.nav_omiljeni) {
            MyFragmentHelper.fragmentReplace(this,
                    R.id.fragmentContainer,
                    MarketMainListFragment.newInstance(true),
                    MarketMainListFragment.Tag,
                    false);
        } else if (id == R.id.nav_korpa) {
            MyFragmentHelper.fragmentReplace(this,
                    R.id.fragmentContainer,
                    CartFragment.newInstance(false),
                    CartFragment.Tag,
                    false);
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, UserInformationActivity.class));
        } else if (id == R.id.nav_logout) {

            MyApiRequest.get(MyApiRequest.endpoint_logout, new MyAbstractRunnable<Object>() {
                @Override
                public void run(Object o) {
                    onLogout(null, null);
                }

                @Override
                public void error(@Nullable Integer statusCode, @Nullable String errorMessage) {
                    onLogout(statusCode, errorMessage);
                }
            });

        }

        navigationView.setCheckedItem(item);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onLogout(Integer statusCode, String errorMessage) {
        Snackbar.make(findViewById(R.id.content), "Uspješan logout", Snackbar.LENGTH_SHORT).show();
        MySession.setKorisnik(null);
        MySession.setKorpa(null);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
