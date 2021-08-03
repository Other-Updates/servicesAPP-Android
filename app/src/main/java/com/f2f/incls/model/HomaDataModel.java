package com.f2f.incls.model;

import java.util.List;

public class HomaDataModel {
    List<RecMainModel> data;

    public HomaDataModel(List<RecMainModel> data) {
        this.data = data;
    }

    public List<RecMainModel> getData() {
        return data;
    }
}
