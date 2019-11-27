package com.example.safehomecoming.retrifit_setup;

import java.util.List;

public class Resultm {

    private  String result;
    private  String result_code;
    private  String description;
    private  int result_row;


    private List<ItemRequest> items;

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
}
