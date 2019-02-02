package com.example.guestuser.damirkrkalicearthgoods.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BlocVM implements Serializable {
    public static int blokIdCounter = 0;

    @SerializedName(value = "id", alternate = { "Id", "ID", "BlokID", "BlokId", "blokID", "blokId", "blokid" })
    private int id;

    @SerializedName(value = "naziv", alternate = { "Naziv" })
    private String naziv;

    @SerializedName(value = "grad", alternate = { "Grad", "Opstina", "opstina" })
    private MunicipalityVM opstina;

    public BlocVM(String naziv, MunicipalityVM opstina) {
        this.id = blokIdCounter++;
        this.naziv = naziv;
        this.opstina = opstina;
    }

    public int getId() {
        return id;
    }

    public String getDrzava(){ return opstina.getdrzava(); }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public MunicipalityVM getOpstina() {
        return opstina;
    }

    public void setOpstina(MunicipalityVM opstina) {
        this.opstina = opstina;
    }
}
