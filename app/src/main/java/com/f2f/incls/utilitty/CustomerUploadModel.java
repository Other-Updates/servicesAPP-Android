package com.f2f.incls.utilitty;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class CustomerUploadModel {
    String image_path,emp_upload_image,service_id,type,workperformed,status,warranty,date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getEmp_upload_image() {
        return emp_upload_image;
    }

    public void setEmp_upload_image(String emp_upload_image) {
        this.emp_upload_image = emp_upload_image;
    }

    Bitmap bitmapArrayList;

    public Bitmap getBitmapArrayList() {
        return bitmapArrayList;
    }

    public void setBitmapArrayList(Bitmap bitmapArrayList) {
        this.bitmapArrayList = bitmapArrayList;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    public String getWorkperformed() {
        return workperformed;
    }

    public void setWorkperformed(String Workperformed) {
        this.workperformed = Workperformed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
