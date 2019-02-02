package com.example.guestuser.damirkrkalicearthgoods.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KitchenType implements Serializable {

    @SerializedName(value = "tipKuhinjeID")
    public int id;

    public String naziv;
    public String opis;

}
