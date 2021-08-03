package com.f2f.incls.model;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllServiceModel  {
    String invoince,date,issue,ticket_number,warranty,image_path,workperformed,attendant_name,emp_upload_img,service_details_id,attendant_mobile,service_id,invoince_amount,product_name,product_desc;
    String product_img,status;
    ArrayList<String> imageList;;
    String service_date,color,work_performed,attendant_name1,statuses,attendantmobile_number,Emp_upload_image,service_history,history_id;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatuses(){
        return status;
    }

    public void setStatuses(String statuses) {
        this.statuses = statuses;
    }

    public String getAttendantmobile_number() {
        return attendantmobile_number;
    }

    public void setAttendantmobile_number(String attendantmobile_number) {
        this.attendantmobile_number = attendantmobile_number;
    }

    public String getAttendant_namework() {
        return attendant_name1;
    }
    public void setAttendant_namework(String attendant_name1) {
        this.attendant_name1 = attendant_name1;
    }




    public String getService_date() {
        return service_date;
    }

    public void setService_date(String service_date) {
        this.service_date = service_date;
    }

    public  String getWork_performed(){return work_performed;}
    public void setWork_performed(String work_performed){this.work_performed = work_performed;}





    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public String getservice_id(){return service_id;}
    public void setservice_id(String service_id){this.service_id = service_id;}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public  String gethistoryid(){return history_id;}
    public void sethistory_id(String history_id){this.history_id=history_id;}

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getService_details_id() {
        return service_details_id;
    }

    public void setService_details_id(String service_details_id) {
        this.service_details_id = service_details_id;
    }


    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }


    public String getinvoince_amount() {
        return invoince_amount;
    }

    public void setinvoince_amount(String invoince_amount) {
        this.invoince_amount = invoince_amount;
    }
    public String getAttendant_mobile() {
        return attendant_mobile;
    }

    public void setAttendant_mobile(String attendant_mobile) {
        this.attendant_mobile = attendant_mobile;
    }

    public String getInvoince() {
        return invoince;
    }

    public void setInvoince(String invoince) {
        this.invoince = invoince;
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

    public String getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(String ticket_number) {
        this.ticket_number = ticket_number;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getAttendant_name() {
        return attendant_name;
    }

    public void setAttendant_name(String attendant_name) {
        this.attendant_name = attendant_name;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

   public String getEmp_upload_image(){return emp_upload_img;}
   public void setEmp_upload_image(String path1){this.emp_upload_img= emp_upload_img;}

   public String getImage_path(){return image_path;}
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getservice_history(){return service_history;}
    public void setservice_history(String service_history) {
        this.service_history = service_history;
    }

   /* public String getWorkperformed() {
        return workperformed;
    }

    public void setWorkperformed(String Workperformed) {
        this.workperformed = Workperformed;
    }
*/
}
