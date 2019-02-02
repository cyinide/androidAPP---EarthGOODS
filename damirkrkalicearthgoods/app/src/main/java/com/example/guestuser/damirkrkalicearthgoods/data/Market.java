package com.example.guestuser.damirkrkalicearthgoods.data;

import com.example.guestuser.damirkrkalicearthgoods.helper.MySession;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Market implements Serializable {
    public int id;
    public String naziv;
    public String opis;
    public Vlasnik vlasnik;
    public String telefon;
    public String adresa;
    public String lokacija;
    public List<MarketFavourite> lajkovi;
    public int likeCount;
    public String slika;
    public String slogan;
    public String email;

    @SerializedName(value = "webUrl")
    public String webUrl;

    @SerializedName(value = "tipoviKuhinje")
    public List<KitchenType> tipoviKuhinje;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getSlika() {
        return "http://" + slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public List<MarketFavourite> getLajkovi() {
        return lajkovi;
    }

    public int getLikesCount() {
        return lajkovi.size();
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<KitchenType> getTipoviKuhinje() {
        return tipoviKuhinje;
    }

    public void setTipoviKuhinje(List<KitchenType> tipoviKuhinje) {
        this.tipoviKuhinje = tipoviKuhinje;
    }

    public boolean userHasLiked() {
        String username = MySession.getKorisnik().getUsername();
        for (MarketFavourite like :
                getLajkovi()) {
            if (like.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean toggleLike() {
        String username = MySession.getKorisnik().getUsername();
        for (MarketFavourite like :
                getLajkovi()) {
            if (like.getUsername().equals(username)) {
                getLajkovi().remove(like);
                //setLikeCount(getLajkovi().size());
                return false;
            }
        }
        getLajkovi().add(new MarketFavourite(MySession.getKorisnik()));
        //setLikeCount(getLajkovi().size());
        return true;
    }
}
