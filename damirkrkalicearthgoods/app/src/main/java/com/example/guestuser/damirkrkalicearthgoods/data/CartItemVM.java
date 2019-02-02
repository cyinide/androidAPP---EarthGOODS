package com.example.guestuser.damirkrkalicearthgoods.data;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class CartItemVM implements Serializable {
    private FoodItemVM foodItemVM;
    private int kolicina;
    private double ukupnaCijena;

    public CartItemVM(FoodItemVM foodItemVM, int kolicina) {
        this.foodItemVM = foodItemVM;
        this.kolicina = 1;
        ukupnaCijena = foodItemVM.getCijena();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ( obj instanceof FoodItemVM && this.foodItemVM.getId() == ((FoodItemVM) obj).getId() ) ||
                ( obj instanceof CartItemVM && this == obj );
    }

    public FoodItemVM getFoodItemVM() {
        return foodItemVM;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void dodajKolicinu() {
        ++this.kolicina;
        ukupnaCijena += foodItemVM.getCijena();
    }

    public void smanjiKolicinu() {
        --this.kolicina;
        ukupnaCijena -= foodItemVM.getCijena();
    }

    public void umanjiKolicinu() {
//        if (kolicina == 1) {
//
//        }
//        --this.kolicina;
//        ukupnaCijena -= foodItemVM.getCijena();
    }

    public double getUkupnaCijena() {
        return ukupnaCijena;
    }
}
