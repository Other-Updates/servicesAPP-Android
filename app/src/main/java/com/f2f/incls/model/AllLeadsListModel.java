package com.f2f.incls.model;

public class AllLeadsListModel {
    String date;
    String issue;
    String image;
    String color;
    String leads_id;
    String cat_id;
    String invoince_no,mobile,mobile2,status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getLeads_id() {
        return leads_id;
    }

    public void setLeads_id(String leads_id) {
        this.leads_id = leads_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }



    public String getInvoince_no() {
        return invoince_no;
    }

    public void setInvoince_no(String invoince_no) {
        this.invoince_no = invoince_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
