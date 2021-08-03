package com.f2f.incls.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EmpStatusUpdateModel {
    String attenter_name,date;
    String[] task_status = {"Pending", "Inprogress", "Complete"};

    public String getAttenter_name() {
        return attenter_name;
    }

    public void setAttenter_name(String attenter_name) {
        this.attenter_name = attenter_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
