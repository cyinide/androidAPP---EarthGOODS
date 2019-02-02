package com.example.guestuser.damirkrkalicearthgoods.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

    public double ukupnaCijena;
    public List<CartItemVM> hranaStavke;

    public Cart() {
        ukupnaCijena = 0;
        hranaStavke = new ArrayList<CartItemVM>();
    }

    public int getHranaStavkeTotalCount() {
        int total = 0;
        for (CartItemVM s :
                hranaStavke) {
            total += s.getKolicina();
        }
        return total;
    }

    public double getUkupnaCijena() {
        ukupnaCijena = 0;
        for (CartItemVM k :
                hranaStavke) {
            ukupnaCijena += (double) k.getUkupnaCijena();
        }
        return ukupnaCijena;
    }

    public CartItemVM dodajStavku(FoodItemVM stavka) {
        for (CartItemVM s :
                hranaStavke) {
            if (s.equals(stavka)) {
                s.dodajKolicinu();
                return s;
            }
        }
        CartItemVM novaStavka = new CartItemVM(stavka, 1);
        hranaStavke.add(novaStavka);
        return novaStavka;
    }

    public CartItemVM ukloniStavku(FoodItemVM stavka) {
        for (CartItemVM s :
                hranaStavke) {
            if (s.equals(stavka)) {
                if (s.getKolicina() > 1) {
                    s.smanjiKolicinu();
                    return s;
                } else {
                    hranaStavke.remove(s);
                    return null;
                }
            }
        }
        return null;
    }


    public List<CartItemVM> getHranaStavke() {
        return hranaStavke;
    }

    public CartItemVM getStavka(FoodItemVM stavka) {
        for (CartItemVM s :
                hranaStavke) {
            if (s.equals(stavka)) {
                return s;
            }
        }
        return null;
    }
}
