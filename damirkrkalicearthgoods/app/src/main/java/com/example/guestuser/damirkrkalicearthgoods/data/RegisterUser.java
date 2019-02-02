package com.example.guestuser.damirkrkalicearthgoods.data;

public class RegisterUser {
    public int id;
    public String username;
    public String password;
    public String ime;
    public String prezime;
    public String adresa;
    public String imageUrl;
    public int blokID;

    public RegisterUser() {}

    public RegisterUser(UserVM korisnik) {
        this.id = korisnik.getId();
        this.username = korisnik.getUsername();
        this.password = korisnik.getPassword();
        this.ime = korisnik.getIme();
        this.prezime = korisnik.getPrezime();
        this.adresa = korisnik.getAdresa();
        this.imageUrl = korisnik.getImageUrl();
        this.blokID = korisnik.getBlok().getId();
    }

    public RegisterUser(int id, String username, String password, String ime, String prezime, String adresa, String imageUrl, int blokID) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
        this.imageUrl = imageUrl;
        this.blokID = blokID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getBlokID() {
        return blokID;
    }

    public void setBlokID(int blokID) {
        this.blokID = blokID;
    }
}
