package com.f2f.incls.model;

import java.util.ArrayList;

public class CustomerAddServiceWorkModel {
    String attendant_name,status,issue,image_path,date,inv_select,service_id,image_1,image_2,image_3;
    ArrayList<String>warrantyList;

    public ArrayList<String> getWarrantyList() {
        return warrantyList;
    }

    public void setWarrantyList(ArrayList<String> warrantyList) {
        this.warrantyList = warrantyList;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    public String getImage_3() {
        return image_3;
    }

    public void setImage_3(String image_3) {
        this.image_3 = image_3;
    }

    ArrayList<EmpUploadImgModel> empuploadList=new ArrayList<>();
    ArrayList<String>testArray=new ArrayList<>();

    public ArrayList<String> getTestArray() {
        return testArray;
    }

    public void setTestArray(ArrayList<String> testArray) {
        this.testArray = testArray;
    }

    public ArrayList<EmpUploadImgModel> getEmpuploadList() {
        return empuploadList;
    }

    public void setEmpuploadList(ArrayList<EmpUploadImgModel> empuploadList) {
        this.empuploadList = empuploadList;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getInv_select() {
        return inv_select;
    }

    public void setInv_select(String inv_select) {
        this.inv_select = inv_select;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttender_name() {
        return attendant_name;
    }

    public String setAttendant_name(String attendant_name) {
        this.attendant_name = attendant_name;
        return attendant_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
