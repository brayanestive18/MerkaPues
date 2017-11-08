package com.brayadiaz.merka;

/**
 * Created by braya on 18/10/2017.
 */

public class User {
    private String name, email, url, password;
    private String uid, listas;

    public User(String name, String email, String url, String password,String uid, String listas) {
        this.name = name;
        this.email = email;
        this.url = url;
        this.password = password;
        this.uid = uid;
        this.listas = listas;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getListas() {
        return listas;
    }

    public void setListas(String listas) {
        this.listas = listas;
    }
}