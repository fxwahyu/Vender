package com.example.vender.Model;

public class Jobs {
    private String foto, nama;
    private int eventid, talentid, rating;

    public Jobs(String foto, String nama, int eventid, int talentid, int rating) {
        this.foto = foto;
        this.nama = nama;
        this.eventid = eventid;
        this.talentid = talentid;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public int getTalentid() {
        return talentid;
    }

    public void setTalentid(int talentid) {
        this.talentid = talentid;
    }
}
