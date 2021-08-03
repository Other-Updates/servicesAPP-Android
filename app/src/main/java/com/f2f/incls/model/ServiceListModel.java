package com.f2f.incls.model;

import android.content.Context;

import java.util.ArrayList;

public class ServiceListModel {
    String tn_no,service_issue,service_date,color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTn_no() {
        return tn_no;
    }

    public void setTn_no(String tn_no) {
        this.tn_no = tn_no;
    }

    public String getService_issue() {
        return service_issue;
    }

    public void setService_issue(String service_issue) {
        this.service_issue = service_issue;
    }

    public String getService_date() {
        return service_date;
    }

    public void setService_date(String service_date) {
        this.service_date = service_date;
    }
}
