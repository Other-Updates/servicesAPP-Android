package com.f2f.incls.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerLogin {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }



    public class Datum {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("state_id")
        @Expose
        private String state_id;
        @SerializedName("name")
        @Expose
        private String name;
        @Expose
        @SerializedName("store_name")
        private String store_name;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("mobil_number")
        @Expose
        private String mobil_number;
        @SerializedName("email_id")
        @Expose
        private String email_id;
        @SerializedName("staff_id")
        @Expose
        private String staff_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState_id() {
            return state_id;
        }

        public void setState_id(String state_id) {
            this.state_id = state_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobil_number() {
            return mobil_number;
        }

        public void setMobil_number(String mobil_number) {
            this.mobil_number = mobil_number;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(String staff_id) {
            this.staff_id = staff_id;
        }
    }
}

