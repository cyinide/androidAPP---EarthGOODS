package com.example.guestuser.damirkrkalicearthgoods.data;

import com.example.guestuser.damirkrkalicearthgoods.data.FoodItemVM;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodDisplayVM {
    @SerializedName(value = "hrana")
    public List<FoodItemVM> hrana;

}
