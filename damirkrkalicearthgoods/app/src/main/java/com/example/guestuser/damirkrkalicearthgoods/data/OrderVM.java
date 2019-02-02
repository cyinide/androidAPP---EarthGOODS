package com.example.guestuser.damirkrkalicearthgoods.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderVM implements Serializable {

    @SerializedName(value = "id")
    private int id;

    @SerializedName(value = "guidSifra")
    private UUID uId;

    @SerializedName(value = "datumKreiranja")
    public Date dateCreated;

    @SerializedName(value = "status")
    public String status;

    @SerializedName(value = "ukupnaCijena")
    public double totalPrice;

    @SerializedName(value = "hranaStavke")
    public List<OrderItemVM> orderItems;

    @SerializedName(value = "narucenoIzRestorana")
    public List<String> orderedFrom;

    public UUID getuId() {
        return uId;
    }

    public Date getDatumNapravljena() {
        return dateCreated;
    }

    public String getDatumNapravljenaString() {
        return new SimpleDateFormat("dd.MM.yyyy").format(dateCreated);
    }

    public String getTimeString() {
        return new SimpleDateFormat("HH:mm:ss").format(dateCreated);
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItemVM> getOrderItems() {
        return orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getNarudzbaStavkeSize() {
        return orderItems.size();
    }

    public List<String> getOrderedFrom() {
        return orderedFrom;
    }

    public String getSearchIndex() {
        String searchData = status + uId + this.getDatumNapravljenaString();
        for (OrderItemVM stavka :
                orderItems) {
            searchData += stavka.getNaziv();
        }
        for (String restoran :
                orderedFrom) {
            searchData += restoran;
        }

        return searchData.toLowerCase();
    }
}
