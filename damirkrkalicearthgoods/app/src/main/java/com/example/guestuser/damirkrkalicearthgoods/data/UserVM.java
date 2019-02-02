package com.example.guestuser.damirkrkalicearthgoods.data;

import android.util.Log;

import com.example.guestuser.damirkrkalicearthgoods.helper.MyApiConfig;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserVM implements Serializable{


    @SerializedName(value = "id", alternate = { "Id", "ID" })
    private Integer id;

    @SerializedName(value="username", alternate= { "Username" })
    private String username;

    @SerializedName(value="password", alternate= { "Password" })
    private String password;

    @SerializedName(value="token")
    private String token;

    @SerializedName(value="ime", alternate= { "Ime" })
    private String ime;

    @SerializedName(value="prezime", alternate= { "Prezime" })
    private String prezime;

    @SerializedName(value="adresa", alternate= { "Adresa" })
    private String adresa;

    private int BlokID;

    @SerializedName(value="blok", alternate= { "Blok", "BlokVm", "BlokVM", "blokVm", "blokvm" })
    private BlocVM blok;

    @SerializedName(value="imageUrl", alternate= { "ImageUrl", "imageurl", "ImageURL", "image", "Image" })
    private String imageUrl;

    @SerializedName(value = "datumKreiranja", alternate = { "datumkreiranja", "DatumKreiranja" })
    private String datumKreiranjaString;

    private transient Date datumKreiranja;

    @SerializedName(value = "zadnjiLogin", alternate = { "ZadnjiLogin", "zadnjilogin" })
    private String zadnjiLoginString;

    private transient Date zadnjiLogin;

    @SerializedName(value = "narudzbeCount", alternate = { "NarudzbeCount", "narudzbecount" })
    private int narudzbeCount;

    public UserVM(String username, String password, String ime, String prezime, String imageUrl, BlocVM blok, String adresa) {
        this.username = username;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.blok = blok;
        this.BlokID = blok.getId();
        this.imageUrl = imageUrl;
        this.adresa = adresa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BlocVM getBlok(){ return blok; }

    public void setBlok(BlocVM value){ BlokID = value.getId(); blok = value; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getToken() {
        return token;
    }

    public String getIme(){ return ime; }

    public void setIme(String value){ ime = value; }

    public String getPrezime(){ return prezime; }

    public void setPrezime(String value){ prezime = value; }

    public String getImageUrlForDisplay() {
        return MyApiConfig.APP_BASE + imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLokacija() {
        return getBlok().getNaziv() + ", " + getBlok().getOpstina().getnaziv();
    }

    public String getAdresa() {
        return adresa != null && adresa.length() > 0 ? adresa : "-";
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getNarudzbeCount() {
        return narudzbeCount;
    }

    public String getZadnjiLogin() {
        if (zadnjiLogin == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat wantedFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            try {
                zadnjiLogin = format.parse(zadnjiLoginString);
                zadnjiLoginString = wantedFormat.format(zadnjiLogin);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("Test", "Error kod parsiranja datuma: " + e.getMessage());
            }
        }
        return zadnjiLoginString;
    }

    public String getDatumRegistracije() {
        if (datumKreiranja == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat wantedFormat = new SimpleDateFormat("dd.MM.yyyy");
            try {
                datumKreiranja = format.parse(datumKreiranjaString);
                datumKreiranjaString = wantedFormat.format(datumKreiranja);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("Test", "Error kod parsiranja datuma: " + e.getMessage());
            }
        }
        return datumKreiranjaString;
    }

    public boolean correctLozinka(String lozinka) {
        if (this.getPassword().equals(lozinka)) {
            return true;
        }
        return false;
    }

    public String getImePrezime() {
        return ime + " " + prezime;
    }

    public String getAdresaLokacija() {
        return getAdresa() + ", " + getLokacija();
    }
}
