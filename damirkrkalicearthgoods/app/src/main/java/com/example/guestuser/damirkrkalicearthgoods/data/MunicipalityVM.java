package com.example.guestuser.damirkrkalicearthgoods.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by asus on 11/12/2018.
 */

public class MunicipalityVM implements Serializable {
    @SerializedName(value = "id")
    private int id;

    @SerializedName(value = "naziv")
    private String naziv;

    @SerializedName(value = "drzava")
    private String drzava;

    @SerializedName(value = "postanskiBroj")
    private String postanskiBroj;

    public MunicipalityVM(int id, String naziv, String drzava) {
        this.id = id;
        this.naziv = naziv;
        this.drzava = drzava;
    }

    public MunicipalityVM(int id, String naziv, String drzava, String postanskiBroj) {
        this.id = id;
        this.naziv = naziv;
        this.drzava = drzava;
        this.postanskiBroj = postanskiBroj;
    }

    public String getdrzava(){ return drzava; }

    public void setdrzava(String value){ drzava = value; }

    public String getnaziv(){ return naziv; }

    public void setnaziv(String value){ naziv = value; }

    public int getid(){ return id; }

    public void setid(int value){ id = value; }

}