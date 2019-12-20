package com.example.vender.Model;

import java.io.Serializable;

public class Event implements Serializable {
    private int id, user_id;
    private String nama_event, deskripsi, kota, venue, tanggal_event, foto, status, lowongan, min_fee, max_fee;

    public Event(){

    }

    public Event(int id, int user_id, String nama_event, String deskripsi, String kota,
                 String venue, String tanggal_event, String foto,
                 String status, String lowongan, String min_fee, String max_fee) {
        this.id = id;
        this.user_id = user_id;
        this.nama_event = nama_event;
        this.deskripsi = deskripsi;
        this.kota = kota;
        this.venue = venue;
        this.tanggal_event = tanggal_event;
        this.foto = foto;
        this.status = status;
        this.lowongan = lowongan;
        this.min_fee = min_fee;
        this.max_fee = max_fee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNama_event() {
        return nama_event;
    }

    public void setNama_event(String nama_event) {
        this.nama_event = nama_event;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTanggal_event() {
        return tanggal_event;
    }

    public void setTanggal_event(String tanggal_event) {
        this.tanggal_event = tanggal_event;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLowongan() {
        return lowongan;
    }

    public void setLowongan(String lowongan) {
        this.lowongan = lowongan;
    }

    public String getMin_fee() {
        return min_fee;
    }

    public void setMin_fee(String min_fee) {
        this.min_fee = min_fee;
    }

    public String getMax_fee() {
        return max_fee;
    }

    public void setMax_fee(String max_fee) {
        this.max_fee = max_fee;
    }
}
