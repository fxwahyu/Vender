package com.example.vender.Model;

public class Offer {
    private int id, event_id, talent_id;
    private String status, dari;

    public Offer(int id, int event_id, int talent_id, String status, String dari) {
        this.id = id;
        this.event_id = event_id;
        this.talent_id = talent_id;
        this.status = status;
        this.dari = dari;
    }

    public Offer(int id) {
        this.id = id;
    }

    public Offer(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getTalent_id() {
        return talent_id;
    }

    public void setTalent_id(int talent_id) {
        this.talent_id = talent_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDari() {
        return dari;
    }

    public void setDari(String dari) {
        this.dari = dari;
    }
}
