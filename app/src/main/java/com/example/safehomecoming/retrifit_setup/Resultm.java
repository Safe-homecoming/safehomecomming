package com.example.safehomecoming.retrifit_setup;

import java.util.List;

public class Resultm {

    // 기본으로 보여주는 json 결과값
    private  String result;
    private  String result_code;
    private  String description;
    private  int result_row;

    // 회원 로그인시
    private String memId;
    private String phone;
    private String name;
    private String age;
    private String memtype;
    private String gender;
    private String wstatus;
    private String token;

    //안심이가 수락한후에 select 내용 추가
    private String curaddress;
    private String peraddress;
    private String reqgender;





    // 시민 요청 현황 list  조회시
    private List<ItemRequest> items;// 안심이 수락 가능 현황list  하위


    public String getCuraddress() {
        return curaddress;
    }

    public void setCuraddress(String curaddress) {
        this.curaddress = curaddress;
    }

    public String getPeraddress() {
        return peraddress;
    }

    public void setPeraddress(String peraddress) {
        this.peraddress = peraddress;
    }

    public String getReqgender() {
        return reqgender;
    }

    public void setReqgender(String reqgender) {
        this.reqgender = reqgender;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getResult_row() {
        return result_row;
    }

    public void setResult_row(int result_row) {
        this.result_row = result_row;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemtype() {
        return memtype;
    }

    public void setMemtype(String memtype) {
        this.memtype = memtype;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWstatus() {
        return wstatus;
    }

    public void setWstatus(String wstatus) {
        this.wstatus = wstatus;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
