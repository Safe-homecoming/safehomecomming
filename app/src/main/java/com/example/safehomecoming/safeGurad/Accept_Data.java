package com.example.safehomecoming.safeGurad;

/*
* class :  Accept_Data
* 내용 : 요청 현황 리사이클러뷰 Data CLASS
* */

public class Accept_Data {

    private int leftKm;   //안심이와 요청지 까지의 거리
    private int workKm;   //요청지와 목적지 사이의 거리
    private int resId;    // 성별 icon
    private int idx; //db 고유 index 값
    private String gender;    // 요구자 성별
    private String name;  // 요청자  이름


    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

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

    public int getLeftKm() {
        return leftKm;
    }

    public void setLeftKm(int leftKm) {
        this.leftKm = leftKm;
    }

    public int getWorkKm() {
        return workKm;
    }

    public void setWorkKm(int workKm) {
        this.workKm = workKm;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
