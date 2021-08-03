package com.f2f.incls.model;

import java.util.ArrayList;
import java.util.List;

public class RecMainModel {
    ArrayList<RecinsideModel> list;
    String at_name,create_date,issue,status,id,adapter;

    public void setList(ArrayList<RecinsideModel> list) {
        this.list = list;
    }

    public String getAt_name() {
        return at_name;
    }

    public void setAt_name(String at_name) {
        this.at_name = at_name;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdapter() {
        return adapter;
    }

    public void setAdapter(String adapter) {
        this.adapter = adapter;
    }

    public RecMainModel(ArrayList<RecinsideModel> list, String at_name,
                        String create_date, String issue, String status,
                        String id, String adapter) {
        this.list = list;

    }

    public List<RecinsideModel> getList() {
        return list;
    }

}
