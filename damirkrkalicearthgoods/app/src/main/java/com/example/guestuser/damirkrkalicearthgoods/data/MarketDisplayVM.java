package com.example.guestuser.damirkrkalicearthgoods.data;


import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MarketDisplayVM {
    @SerializedName(value = "restorani")
    public List<Market> restorani;

    public List<Market> omiljeniRestorani;

    public List<Market> getRestorani() {
        return restorani;
    }

    public List<Market> getOmiljeniRestorani() {
        if (omiljeniRestorani != null) {
            return omiljeniRestorani;
        }
        MarketFavourite userLike = new MarketFavourite(MySession.getKorisnik());
        omiljeniRestorani = new ArrayList<>();
        for (Market r : restorani) {
            if (r.userHasLiked()) {
                omiljeniRestorani.add(r);
            }
        }
        return omiljeniRestorani;
    }
}
