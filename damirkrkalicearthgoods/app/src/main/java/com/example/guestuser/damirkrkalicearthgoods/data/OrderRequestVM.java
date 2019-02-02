package com.example.guestuser.damirkrkalicearthgoods.data;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderRequestVM implements Serializable {

    private class StavkaRequest
    {
        public int hranaID;
        public int kolicina;

        public StavkaRequest(int hranaID, int kolicina) {
            this.hranaID = hranaID;
            this.kolicina = kolicina;
        }
    }

    public LoginUser credentials;

    public double ukupnaCijena;

    public List<StavkaRequest> stavke;

    public OrderRequestVM(LoginUser credentials, double ukupnaCijena, List<StavkaRequest> stavke) {
        this.credentials = credentials;
        this.ukupnaCijena = ukupnaCijena;
        this.stavke = stavke;
    }

    public OrderRequestVM(LoginUser credentials, Cart cart) {
        this.credentials = credentials;
        this.ukupnaCijena = cart.getUkupnaCijena();
        this.stavke = new ArrayList<>();
        for (CartItemVM s :
                cart.getHranaStavke()) {
            stavke.add(new StavkaRequest(s.getFoodItemVM().getId(), s.getKolicina()));
        }
    }

}
