package com.example.vender.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String nama, email, password, foto, istalent, phone_number;
    private int id;

    public User(){

    }

    public User(String nama, String email, String password, String foto, int id, String istalent, String phone_number) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.foto = foto;
        this.id = id;
        this.istalent = istalent;
        this.phone_number = phone_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getIstalent() {
        return istalent;
    }

    public void setIstalent(String istalent) {
        this.istalent = istalent;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
