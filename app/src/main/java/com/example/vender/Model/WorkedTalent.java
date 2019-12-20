package com.example.vender.Model;

import java.io.Serializable;

public class WorkedTalent implements Serializable {
    private int talentid, rating, numberofjobs;
    private String namatalent, phonenumber, skill, bio, kota, min_fee, max_fee, experience, instagram, youtube, foto;

    public WorkedTalent(int talentid, int rating, int numberofjobs, String namatalent, String phonenumber, String skill, String bio, String kota,
                       String min_fee, String max_fee, String experience, String instagram, String youtube, String foto) {
        this.talentid = talentid;
        this.rating = rating;
        this.numberofjobs = numberofjobs;
        this.namatalent = namatalent;
        this.phonenumber = phonenumber;
        this.skill = skill;
        this.bio = bio;
        this.kota = kota;
        this.min_fee = min_fee;
        this.max_fee = max_fee;
        this.experience = experience;
        this.instagram = instagram;
        this.youtube = youtube;
        this.foto = foto;
    }

    public WorkedTalent(){

    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public int getTalentid() {
        return talentid;
    }

    public void setTalentid(int talentid) {
        this.talentid = talentid;
    }

    public int getRating() {
        return rating;
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

    public String getNamatalent() {
        return namatalent;
    }

    public void setNamatalent(String namatalent) {
        this.namatalent = namatalent;
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

}
