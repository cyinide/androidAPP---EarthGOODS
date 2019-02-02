package com.example.guestuser.damirkrkalicearthgoods.data;

import java.io.Serializable;

public class MarketTypeVM implements Serializable {
    private static int counter = 0;

    private int id;

    private String name;

    private String description;

    public MarketTypeVM(String name, String description)  {
        this.id = counter++;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
