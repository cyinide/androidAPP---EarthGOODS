package com.example.guestuser.damirkrkalicearthgoods.data;

public class LoginUser {
    public String username;
    public String password;

    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginUser(UserVM korisnik) {
        this.username = korisnik.getUsername();
        this.password = korisnik.getPassword();
    }


}
