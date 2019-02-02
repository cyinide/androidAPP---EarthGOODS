package com.example.guestuser.damirkrkalicearthgoods.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.guestuser.damirkrkalicearthgoods.data.Cart;
import com.example.guestuser.damirkrkalicearthgoods.data.LoginUser;
import com.example.guestuser.damirkrkalicearthgoods.data.UserVM;


public class MySession {

    private static String SHARED_PREFS_STORAGE_NAME = "DatotekaZaSharedPreferences";
    private static String PREF_LOGGED_USER = "LoginraniUser";
    private static String PREF_KORPA = "MojaKorpa";
    private static LoginUser loginUser;

    public static Cart getKorpa() {
        Cart cart;

        SharedPreferences prefs = MyApp.getContext().getSharedPreferences(SHARED_PREFS_STORAGE_NAME, Context.MODE_PRIVATE);

        String jsonKorpa = prefs.getString(PREF_KORPA, "");

        if (jsonKorpa.isEmpty()) {
            return null;
        }

        cart = MyGson.build().fromJson(jsonKorpa, Cart.class);

        return cart;
    }


    public static void setKorpa(Cart cart) {
        String jsonKorpa = cart == null ? "" : MyGson.build().toJson(cart);

        SharedPreferences prefs = MyApp.getContext().getSharedPreferences(SHARED_PREFS_STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_KORPA, jsonKorpa);
        editor.apply();
    }

    public static LoginUser getLoginUser() {
        if (loginUser != null) {
            return loginUser;
        }

        UserVM korisnik = getKorisnik();
        if (korisnik == null) {
            return null;
        }

        loginUser = new LoginUser(korisnik);
        return loginUser;
    }

    public static UserVM getKorisnik() {
        UserVM korisnik;

        SharedPreferences prefs = MyApp.getContext().getSharedPreferences(SHARED_PREFS_STORAGE_NAME, Context.MODE_PRIVATE);

        String jsonKorisnik = prefs.getString(PREF_LOGGED_USER, "");

        if (jsonKorisnik.isEmpty()) {
            return null;
        }

        korisnik = MyGson.build().fromJson(jsonKorisnik, UserVM.class);

        return korisnik;
    }

    public static void setKorisnik(UserVM korisnik) {
        String jsonKorisnik = korisnik == null ? "" : MyGson.build().toJson(korisnik);

        SharedPreferences prefs = MyApp.getContext().getSharedPreferences(SHARED_PREFS_STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_LOGGED_USER, jsonKorisnik);
        editor.apply();
    }

    public static SharedPreferences getPrefs() {
        return MyApp.getContext().getSharedPreferences(SHARED_PREFS_STORAGE_NAME, Context.MODE_PRIVATE);
    }
}
