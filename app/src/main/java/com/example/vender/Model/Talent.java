package com.example.vender.Model;

import java.io.Serializable;

public class Talent implements Serializable {
    private int id, rating, numberofjobs;
    private String skill, bio, kota, min_fee, max_fee, experience, nama, instagram, youtube;

    public Talent(){

    }

    public Talent(int id, String nama, String skill, String bio, String kota, String min_fee, String max_fee,
                  String experience, int rating, int numberofjobs, String instagram, String youtube) {
        this.id = id;
        this.skill = skill;
        this.bio = bio;
        this.kota = kota;
        this.min_fee = min_fee;
        this.max_fee = max_fee;
        this.experience = experience;
        this.rating = rating;
        this.numberofjobs = numberofjobs;
        this.nama = nama;
        this.instagram = instagram;
        this.youtube = youtube;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public int getRating() {
        return rating;
    }

    public String getNama(){
        return this.nama;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getNumberofjobs() {
        return numberofjobs;
    }

    public void setNumberofjobs(int numberofjobs) {
        this.numberofjobs = numberofjobs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
