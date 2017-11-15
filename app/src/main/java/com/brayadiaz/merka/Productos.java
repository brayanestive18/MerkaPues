package com.brayadiaz.merka;

/**
 * Created by braya on 7/11/2017.
 */

public class Productos {

    private String name, code, precio;
    private String uid;

    public Productos (String name, String code, String precio, String uid) {
        this.name = name;
        this.code = code;
        this.precio = precio;
        this.uid = uid;
    }

    public Productos() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}