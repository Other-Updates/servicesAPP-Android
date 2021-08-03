package com.f2f.incls.model;

public class WorkPerformedModel {
    String service_date,color,work_performed,service_id,attendant_name,work_not_select,status,attendantmobile_number;
    String Emp_upload_image;

    public String getwork_not_select() {
        return work_not_select;
    }

    public void setWork_not_select(String work_not_select) {
        this.work_not_select = work_not_select;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttendantmobile_number() {
        return attendantmobile_number;
    }

    public void setAttendantmobile_number(String attendantmobile_number) {
        this.attendantmobile_number = attendantmobile_number;
    }

    public String getAttendant_name() {
        return attendant_name;
    }
    public void setAttendant_name(String attendant_name) {
        this.attendant_name = attendant_name;
    }

    public String getEmp_upload_image() {
        return Emp_upload_image;
    }
    public void setEmp_upload_image(String Emp_upload_image) {
        this.Emp_upload_image = Emp_upload_image;
    }
    public String getservice_id() {
        return service_id;
    }
    public void setservice_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_date() {
        return service_date;
    }

    public void setService_date(String service_date) {
        this.service_date = service_date;
    }

    public  String getWork_performed(){return work_performed;}
    public void setWork_performed(String work_performed){this.work_performed = work_performed;}


}
