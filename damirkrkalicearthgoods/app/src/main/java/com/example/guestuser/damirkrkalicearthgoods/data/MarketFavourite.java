package com.example.guestuser.damirkrkalicearthgoods.data;

import java.io.Serializable;

public class MarketFavourite implements Serializable {

    public String username;
    public String imePrezime;
    public String imageUrl;

    public String getUsername() {
        return username;
    }

    public MarketFavourite(UserVM k) {
        this.username = k.getUsername();
        this.imePrezime = k.getImePrezime();
        this.imageUrl = k.getImageUrl();
    }
}
