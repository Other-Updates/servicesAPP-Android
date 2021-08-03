package com.f2f.incls.utilitty;

public class Constants {
  // public static String BASE_URL="https://ierp.synology.me:6257/api";
 //  public static String BASE_URL="https://demo.f2fsolutions.co.in/incsolERP/api";
 public static String BASE_URL="https://demo.f2fsolutions.co.in/serviceAPP/api";

 //  public static String BASE_URL="https://incsolerp.synology.me:6257/api";
// public static String BASE_URL="https://103.35.142.25:6257/api";


 /*  public static String AUTH_KEY="Authorization";
   public static String AUTH_VALUE="Basic YWRtaW46MTIzNA==";*/



    public static String authToken="Basic YWRtaW46MTIzNA==";
    public static String user_name="admin";
    public static String password="1234";
    public static String access="JHFTyhf68GY9nhk";
    public static String api_key="admin@123";
    public static String con_type="application/x-www-form-urlencoded";

    public  static String LOGIN_OUT =Constants.BASE_URL+"/customer_log_out";
    public  static String EMP_LOGIN_OUT =Constants.BASE_URL+"/user_log_out";
    public  static String LOGIN_URL =Constants.BASE_URL+"/cust_login";
    public  static String REGISTER_URL =Constants.BASE_URL+"/customer_register";
    public  static String EMP_LOGIN_URL =Constants.BASE_URL+"/emp_login";
    public  static String CUS_LOGIN_OTP =Constants.BASE_URL+"/verify_otp_password";
    public  static String CUS_LOGIN_RESEND_OTP =Constants.BASE_URL+"/resend_otp";
    public  static String CUS_LOGIN_RESEND_VERIFY =Constants.BASE_URL+"/verify_resend_otp";
    public  static String CUS_LEADS =Constants.BASE_URL+"/leads_list";
    public  static String CUS_PENDING_LEADS=Constants.BASE_URL+"/pending_leads";
    public  static String CUS_ADD_LEADS=Constants.BASE_URL+"/add_leads";
    public  static String CUS_EDIT_LEADS=Constants.BASE_URL+"/edit_leads";
    public  static String CUS_PRODUCT_CATEGORIES=Constants.BASE_URL+"/get_all_checked_categories";
    public  static String CUS_LEADS_NUMBER=Constants.BASE_URL+"/get_leads_number";
    public  static String CUS_ADD_SERVICE=Constants.BASE_URL+"/add_service";
    public  static String CUS_PENDING_SERVICE=Constants.BASE_URL+"/get_pending_service_list";
    public  static String CUS_SERVICE_LIST =Constants.BASE_URL+"/get_service_list";
    public  static String CUS_PROFILE_UPDATE =Constants.BASE_URL+"/update_customer_profile";
    public  static String ADS_API =Constants.BASE_URL+"/get_adverstisment_details";
    public  static String CUS_PROFILE_PIC =Constants.BASE_URL+"/api_customer_image_upload";
    public  static String CUS_SERVICE_SPINNER_INV =Constants.BASE_URL+"/get_all_invoice_id";
    public  static String CUS_SERVICE_SPINNER_INV_DETAILS =Constants.BASE_URL+"/get_invoice_details_by_id";
    public  static String EMP_PROFILE_UPDATE =Constants.BASE_URL+"/update_employee_profile";
    public  static String EMP_PROFILE_PIC =Constants.BASE_URL+"/api_employee_image_upload";
    public  static String EMP_GET_PENDING_SERVICE_LIST =Constants.BASE_URL+"/get_service_pending_list";
    public  static String EMP_EDIT_SERVICE =Constants.BASE_URL+"/edit_service";
    public  static String CUS_ATTENTANT_DETAILS =Constants.BASE_URL+"/get_all_attendant_details_by_invno";
    public  static String LAST_TICKET_NO_GET =Constants.BASE_URL+"/get_last_ticket_number";
    //public  static String YOUTUBE =Constants.BASE_URL+"/get_project_link_by_invno";
    public  static String YOUTUBE =Constants.BASE_URL+"/get_link_details";
    public static String SERVICE_HISTORY=Constants.BASE_URL+"/get_service_history";
    public static String LOGIN_STATUS=Constants.BASE_URL+"/user_login_status";






    public static String TIME_OUT = "Oops! Time out while connecting to server! Please try again";
    public static String AUTH_FAILURE_ERROR = "There is an authentication failure error while connecting to server. Please try again";
    public static String SERVER_ERROR = "There is an unexpected server error while connecting to server. Please try again";
    public static String NETWORK_ERROR = "There is a network error while connecting to server. Please try again";
    public static String PARSE_ERROR = "There is an unexpected Parse error while connecting to server. Please try again";

    public static String PLEASE_WAIT = "Please wait...";
    public static String AUTHENTICATING = "Authenticating...";

}
