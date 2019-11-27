package com.example.safehomecoming.retrifit_setup;

public class ItemRequest {

    private int idx;
    private String cur_latitude;
    private String cur_longitude;
    private String homecoming_distance;
    private String peraddress;
    private String per_latitude;
    private String per_longitude;
    private String reqgender;
    private String gender;
    private String reqid;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getCur_latitude() {
        return cur_latitude;
    }

    public void setCur_latitude(String cur_latitude) {
        this.cur_latitude = cur_latitude;
    }

    public String getCur_longitude() {
        return cur_longitude;
    }

    public void setCur_longitude(String cur_longitude) {
        this.cur_longitude = cur_longitude;
    }

    public String getHomecoming_distance() {
        return homecoming_distance;
    }

    public void setHomecoming_distance(String homecoming_distance) {
        this.homecoming_distance = homecoming_distance;
    }

    public String getPeraddress() {
        return peraddress;
    }

    public void setPeraddress(String peraddress) {
        this.peraddress = peraddress;
    }

    public String getPer_latitude() {
        return per_latitude;
    }

    public void setPer_latitude(String per_latitude) {
        this.per_latitude = per_latitude;
    }

    public String getPer_longitude() {
        return per_longitude;
    }

    public void setPer_longitude(String per_longitude) {
        this.per_longitude = per_longitude;
    }

    public String getReqgender() {
        return reqgender;
    }

    public void setReqgender(String reqgender) {
        this.reqgender = reqgender;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }
}
