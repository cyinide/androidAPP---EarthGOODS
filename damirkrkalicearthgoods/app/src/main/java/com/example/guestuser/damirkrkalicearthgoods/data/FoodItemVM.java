package com.example.guestuser.damirkrkalicearthgoods.data;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FoodItemVM implements Serializable {
    private static int hranaItemIdCounter = 0;

    @SerializedName(value = "id")
    private int id;

    @SerializedName(value = "naziv")
    private String naziv;

    @SerializedName(value = "opis")
    private String opis;

    private List<String> sastojci;

    private List<FoodCategoryVM> kategorije;

    @SerializedName(value = "tipKuhinje")
    private KitchenType KitchenType;

    @SerializedName(value = "restoranNaziv")
    private String restoranNaziv;

    @SerializedName(value = "restoranId")
    private int restoranId;

    @SerializedName(value = "cijena")
    private double cijena;

    @SerializedName(value = "imageUrl")
    private String imageUrl;

    public FoodItemVM(String naziv, String opis, List<String> sastojci, List<FoodCategoryVM> kategorije, double cijena) {
        this.id = hranaItemIdCounter++;
        this.naziv = naziv;
        this.opis = opis;
        this.sastojci = sastojci == null ? new ArrayList<String>() : sastojci;
        this.kategorije = kategorije;
        this.cijena = cijena;
        this.imageUrl = imageUrl;
    }

    public FoodItemVM(String naziv, String opis, List<String> sastojci, List<FoodCategoryVM> kategorije, String imageUrl, double cijena) {
        this.id = hranaItemIdCounter++;
        this.naziv = naziv;
        this.opis = opis;
        this.sastojci = sastojci;
        this.kategorije = kategorije;
        this.cijena = cijena;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
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

    public List<String> getSastojci() {
        return sastojci;
    }

    public void setSastojci(List<String> sastojci) {
        this.sastojci = sastojci;
    }

    public List<FoodCategoryVM> getKategorije() {
        return kategorije;
    }

    public void setKategorije(List<FoodCategoryVM> kategorije) {
        this.kategorije = kategorije;
    }

    public String getRestoranNaziv() {
        return restoranNaziv;
    }

    public int getRestoranId() {
        return restoranId;
    }

    public String getImageUrl() {
        return "http://" + imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

    public String getSearchIndex() {
        String searchData = naziv + opis + cijena;
        return searchData.toLowerCase();
    }
}
