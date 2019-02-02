package com.example.guestuser.damirkrkalicearthgoods.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderItemVM implements Serializable {
    @SerializedName(value = "naziv")
    public String naziv;

    @SerializedName(value = "cijena")
    public double cijena;

    @SerializedName(value = "kolicina")
    public int kolicina;

    public OrderItemVM(String naziv, double cijena, int kolicina) {
        this.naziv = naziv;
        this.cijena = cijena;
        this.kolicina = kolicina;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }
}
