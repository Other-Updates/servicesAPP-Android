package com.f2f.incls.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.CusAddServiceWorkAdapter;
import com.f2f.incls.adapter.CustomerUploadImgAdapter;
import com.f2f.incls.adapter.WarrantyListAdapter;
import com.f2f.incls.model.CustomerAddServiceWorkModel;
import com.f2f.incls.model.EmpUploadImgModel;
import com.f2f.incls.model.WarrantyListModel;
import com.f2f.incls.utilitty.AndroidMultiPartEntity;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.CustomerUploadModel;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;
import static com.f2f.incls.utilitty.SessionManager.KEY_ATN_ID;
import static com.f2f.incls.utilitty.SessionManager.KEY_ATN_NAME;
import static com.f2f.incls.utilitty.SessionManager.KEY_INV_NO;
import static com.f2f.incls.utilitty.SessionManager.KEY_INV_USER;
import static com.f2f.incls.utilitty.SessionManager.KEY_TK_NO;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.KEY_WARRANTY;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class CustomerWarrantyFragment extends Fragment implements LoadinInterface {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 456;
    private static final int RESULT_CANCELED = 0;
    Fragment fragment;
    ImageView test_image;
    Button add_service_submit,warranty_service_cancel;
    EditText add_service_issue;
    ImageView paid_service,paid_verify,warannty_verify,img_upload_icon,work_img_upload;
    RelativeLayout ads_img_ly;
    LinearLayout inv_amt_ly,about_issue_ly,recycler_ly;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    TextView invoice_amt,warranty_status,warranty_invoice_id,warranty_ticket_id,
            warranty_date_id,expiry_id,attendent_name,inv_date,at_status,paid_service_tv,project_des;
    ScrollView warranty_scroll;
    GifImageView loader_img;
    ProgressDialog dialog;
    RecyclerView warranty_recycler,upload_img_reg,work_per_rec,add_service_work_rec;
    private static final String IMAGE_DIRECTORY = "/Erp/uploaded";
    private int GALLERY = 1, CAMERA = 2;
    String imagePath,store_path,id,click_pos,warranty_color,date1;
    String[] image_store={};
    private int REQUEST_CAMERA = 100, SELECT_FILE = 200;
    String userChoosenTask, cus_id,adapter_id;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private static final int PERMISSION_REQUEST_CODE = 200;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Spinner spinner;
    String expiry,warranty,at_name,at_id,in_amt,invoice,tk_no,in_date,request_inv;
    String service_type="warranty";
    ArrayList<WarrantyListModel>arrayList;
    ArrayList<CustomerUploadModel>uploadArrayList=new ArrayList<>();
    ArrayList<String>uploadImage=new ArrayList<>();
    ArrayList<EmpUploadImgModel>empuploadList=new ArrayList<>();
    ArrayList<String>imageList=new ArrayList<>();
    public static String counter="0000000000";
    public static int count;
    public static String prefix= "TKNO-";
    public static String fx;
    private Object container;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviewid(view);
        ((CustomerDashBoard)getActivity()).adsclose();
        ((CustomerDashBoard)getActivity()).adsimageget();
        HashMap<String,String>user=session.getcustomerdetails();
        cus_id =user.get(KEY_USER_ID);
        Log.d("Success","add_service_emp_id::"+ cus_id);

        // workperformed();
        SpinnerInvoice(cus_id);
        paid_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Success","Paid_Service::");
             //   paid_verify.setVisibility(View.VISIBLE);
             //   warannty_verify.setVisibility(View.GONE);
                fragment=new CustomerPaidFragment();
                loadfragment(fragment);
            }
        });
        img_upload_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Success","upload_img::");
                click_pos="1";
                showPictureDialog();

            }
        });
       /* work_img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_pos="2";
                showPictureDialog();
            }
        });*/

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter_id= spinner.getSelectedItem().toString();
                Log.d("Success","adapter_selected::"+adapter_id);
                try {
                    warrantymethod();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_service_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String>map=session.getService();
                String inv=map.get(KEY_INV_NO);
                String tk=map.get(KEY_TK_NO);
                String warranty=map.get(KEY_WARRANTY);
                String at_name=map.get(KEY_ATN_ID);
                String issue=add_service_issue.getText().toString();

                Log.d("Success","image_path_test_string::"+uploadImage.size());
                if (issue.isEmpty() ||/* imagePath==null ||*/adapter_id.equals("Select Invoice")
                        || warranty.equalsIgnoreCase("Not available")){
                    if (issue.isEmpty()){
                        Toast.makeText(getActivity(),"Please enter about issue"
                                ,Toast.LENGTH_SHORT).show();
                    }
                   /* if (imagePath==null){
                        Toast.makeText(getActivity(),"Please upload picture"
                                ,Toast.LENGTH_SHORT).show();
                    }*/
                    if (adapter_id.equals("Select Invoice")){
                        Toast.makeText(getActivity(),"Please select invoice"
                                ,Toast.LENGTH_SHORT).show();
                    }if (warranty.equalsIgnoreCase("Not available")){
                        Toast.makeText(getActivity(),"Please visit paid service"
                                ,Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //  addservice(cus_id,inv,tk,warranty,at_name,issue,service_type,path);
                    new UploadFileToServer(uploadImage,imagePath).execute();
                }
            }
        });
        warranty_service_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new CustomerServiceFragment();
                loadfragment(fragment);
            }
        });

        inv_amt_ly.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        about_issue_ly.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        recycler_ly.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
    }

    private boolean permission() {
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                                        }, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    private void workperformed() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("customer_id", cus_id);
        object.put("inv_no", adapter_id);
        Log.d("Success", "Work_performed_request::" + object);
        runback(Constants.CUS_ATTENTANT_DETAILS, object);
    }

    private void runback(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","Work_performed_responce::"+response);
                ArrayList<CustomerAddServiceWorkModel> workList=new ArrayList<>();
                if (response.getString("status").equalsIgnoreCase("success")){
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++) {
                        JSONObject obj = array.getJSONObject(i);
                        CustomerAddServiceWorkModel model = new CustomerAddServiceWorkModel();
                        model.setAttendant_name(obj.getString("name"));
                        model.setDate(obj.getString("created_date"));
                        model.setIssue(obj.getString("work_performed"));
                        model.setStatus(obj.getString("status"));
                        model.setService_id(obj.getString("id"));
                        String id = (obj.getString("id"));
                        model.setInv_select(adapter_id);

                        JSONArray array1 = obj.getJSONArray("employee_image_upload");
                        ArrayList<String> images=new ArrayList<>();
                        for (int j = 0; j < array1.length(); j++) {
                            JSONObject obj1 = array1.getJSONObject(j);
                            String path = obj1.getString("img_path");
                            images.add(path);
                        }
                        model.setWarrantyList(images);
                        workList.add(model);
                    }

                    LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                    add_service_work_rec.setLayoutManager(lrt);
                    CusAddServiceWorkAdapter adapter=new CusAddServiceWorkAdapter(getActivity(),workList);
                    add_service_work_rec.setAdapter(adapter);

                    for(CustomerAddServiceWorkModel model:workList){
                        for (String s:model.getWarrantyList()){
                            Log.w("Images:",s);
                        }
                    }

                }else{
                    //This is recycler old data eraise
                    CustomerAddServiceWorkModel model=new CustomerAddServiceWorkModel();
                    model.setInv_select(adapter_id);
                    Log.d("Success","invoice_test_fragment::"+adapter_id);
                    workList.add(model);
                    add_service_work_rec.setAdapter(null);
                }
                return null;
            }
        });
    }
    private void findviewid(View view) {
        spinner=view.findViewById(R.id.spinner);
        invoice_amt=view.findViewById(R.id.invoice_amt);
        warranty_status=view.findViewById(R.id.warranty_status);
        warranty_invoice_id=view.findViewById(R.id.warranty_invoice_id);
        warranty_ticket_id=view.findViewById(R.id.warranty_ticket_id);
        warranty_date_id=view.findViewById(R.id.warranty_date_id);
        expiry_id=view.findViewById(R.id.expiry_id);
        attendent_name=view.findViewById(R.id.attendent_name);
        inv_date=view.findViewById(R.id.inv_date);
        at_status=view.findViewById(R.id.at_status_tv);
        warranty_recycler=view.findViewById(R.id.warranty_recycler);
        upload_img_reg=view.findViewById(R.id.upload_img_reg);
        paid_service=view.findViewById(R.id.paid_service);
      //  paid_verify=view.findViewById(R.id.paid_verify);
      //  warannty_verify=view.findViewById(R.id.warranty_verify);
        img_upload_icon=view.findViewById(R.id.img_upload_icon);
        work_img_upload=view.findViewById(R.id.work_img_upload);
        loader_img=view.findViewById(R.id.loader_img);
        warranty_scroll=view.findViewById(R.id.warranty_scroll);
        work_per_rec=view.findViewById(R.id.work_per_rec);
        add_service_submit=view.findViewById(R.id.add_service_submit);
        add_service_issue=view.findViewById(R.id.add_service_issue);
        inv_amt_ly=view.findViewById(R.id.inv_amt_ly);
        about_issue_ly=view.findViewById(R.id.about_issue_ly);
        recycler_ly=view.findViewById(R.id.recycler_ly);
        paid_service_tv=view.findViewById(R.id.paid_service_tv);
        add_service_work_rec=view.findViewById(R.id.add_service_work_rec);
        warranty_service_cancel=view.findViewById(R.id.warranty_service_cancel);
      //  project_des=view.findViewById(R.id.project_des);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session=new SessionManager(getActivity());
        sharpre =this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        dialog=new ProgressDialog(getContext());
        Log.d("Success","warranty_method_Oncreate::");
       /* if(checkPermission()){
            return;
        }else {
            requestPermission();
        }*/
    }
    private void showPictureDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (permission()) {
                        cameraIntent();
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (permission()) {
                        galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
    private void cameraIntent() {
        if (permission()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;

        if(requestCode==REQUEST_CAMERA && resultCode==RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selectedImageUri = getImageUri(getActivity(),photo);
            imagePath=getRealPathFromURI(selectedImageUri);

            Log.d("Success","selected_uri_camera::"+imagePath);

            CustomerUploadModel model=new CustomerUploadModel();
            model.setImage_path(imagePath);
            Log.d("Success","gallery_image_path::"+imagePath);
            uploadArrayList.add(model);

            Log.d("Success","upload_Array_size::"+uploadArrayList.size());
            Log.d("Success","click_pos::"+click_pos);
            LinearLayoutManager lrt = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            upload_img_reg.setLayoutManager(lrt);
            CustomerUploadImgAdapter adapter = new CustomerUploadImgAdapter(getActivity(), uploadArrayList);
            upload_img_reg.setAdapter(adapter);

            try {
                File sdcard = Environment.getExternalStorageDirectory();
                String targetPath = sdcard.getAbsolutePath() + File.separator + "INS" + File.separator + "Images" + File.separator;
                File folder = new File(targetPath);
                folder.mkdirs();

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                String imageFileName = randomUUIDString + ".jpg";
                String destination_file_name = targetPath + imageFileName;
                Log.d("success", "imageFilePath:::" +imagePath);
                File source_fiel = new File(imagePath);

                File destination_file = new File(destination_file_name);
                Log.d("success", "destination_file:: " + destination_file_name);

                if (!destination_file.exists()) {
                    destination_file.createNewFile();
                    Log.d("success", "created:: ");
                }

                if (copyFile(source_fiel, destination_file)) {
                    imagePath= destination_file_name;
                    uploadImage.add(imagePath);
                }
            } catch(Exception e){
                Log.e("success", "Exception:: " + e);

            }
        }


        if (requestCode==SELECT_FILE && resultCode==RESULT_OK){
            imagePath = CustomerPaidFragment.GetFilePathFromDevice.getPath(getActivity(), data.getData());
            Log.d("Success","selected_uri_gallery::"+imagePath);

            CustomerUploadModel model=new CustomerUploadModel();
            model.setImage_path(imagePath);
            Log.d("Success","gallery_image_path::"+imagePath);
            uploadArrayList.add(model);

            LinearLayoutManager lrt = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            upload_img_reg.setLayoutManager(lrt);
            CustomerUploadImgAdapter adapter = new CustomerUploadImgAdapter(getActivity(), uploadArrayList);
            upload_img_reg.setAdapter(adapter);

            try {
                File sdcard = Environment.getExternalStorageDirectory();
                String targetPath = sdcard.getAbsolutePath() + File.separator + "INS" + File.separator + "Images" + File.separator;
                File folder = new File(targetPath);
                folder.mkdirs();

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                String imageFileName = randomUUIDString + ".jpg";
                String destination_file_name = targetPath + imageFileName;
                File source_fiel = new File(imagePath);

                File destination_file = new File(destination_file_name);


                if (!destination_file.exists()) {
                    destination_file.createNewFile();
                    Log.d("success", "created:: ");
                }

                if (copyFile(source_fiel, destination_file)) {
                    imagePath= destination_file_name;
                    uploadImage.add(imagePath);
                }
            } catch(Exception e){
                Log.e("success", "Exception:: " + e);

            }
        }

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    public class UploadFileToServer extends AsyncTask<String, Integer, String> {
        ContentBody cbFile = null;
        long totalSize=0;
        boolean isAudio=false;
        String imagePath;
        ProgressDialog progressDialog;
        ArrayList<String> uploadImage;

        private UploadFileToServer(ArrayList<String> uploadImage, String imagePath) {
            this.uploadImage=uploadImage;
            this.imagePath=imagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Log.d("succes","updatng:::"+String.valueOf(progress[0]) + "%");
          //  Toast.makeText(getActivity(),progress[0]+"%",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return uploadFile(uploadImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(ArrayList<String> filePath) throws IOException {
            String responseString = null;
            String creds = String.format("%s:%s","admin","1234");
            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);

            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            DefaultHttpClient client = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            // Example send http request
            String url =Constants.CUS_ADD_SERVICE;
            HttpPost httppost = new HttpPost(url);


            httppost.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials("admin", "1234"), "UTF-8", false));

            Log.d("success","image_url::::"+url);
            Log.d("success","image_url_http_post::::"+httppost);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                HashMap<String,String>map=session.getService();
                String inv=map.get(KEY_INV_NO);
                String tk=map.get(KEY_TK_NO);
                String warranty=map.get(KEY_WARRANTY);
                String at_id=map.get(KEY_ATN_NAME);
                String issue=add_service_issue.getText().toString();
                String service_type="warranty";

              //  File sourceFile = new File(imagePath);
                ContentBody e_id = new StringBody(cus_id);
                ContentBody i_id = new StringBody(inv);
                ContentBody t_id = new StringBody(tk);
                ContentBody w_id = new StringBody(warranty);
                ContentBody a_id = new StringBody(at_id);
                ContentBody issue_id = new StringBody(issue);
                ContentBody service_id = new StringBody(service_type);
                //  ContentBody path_id = new FileBody(sourceFile);

                Log.d("success","image_path_id_send::::"+ cus_id);
                Log.d("success","image_path_id_send::::"+inv);
                Log.d("success","image_path_id_send::::"+tk);
                Log.d("success","image_path_id_send::::"+warranty);
                Log.d("success","image_path_id_send::::"+at_id);
                Log.d("success","image_path_id_send::::"+issue);
                Log.d("success","image_path_id_send::::"+service_type);
                Log.d("success","image_path_id_send::::"+cbFile);

                if (filePath!=null) {
                    for (int i = 0; i < filePath.size(); i++) {
                        File file = new File(filePath.get(i));
                        Log.d("Success", "multi_image_test::" + file);
                        cbFile = new FileBody(file);
                        entity.addPart("name[]",cbFile);
                    }
                }

                    entity.addPart("customer_id",e_id);
                    entity.addPart("inv_no",i_id);
                    entity.addPart("ticket_no",t_id);
                    entity.addPart("warranty",w_id);
                    entity.addPart("attendant_id",a_id);
                    entity.addPart("description",issue_id);
                    entity.addPart("service_type",service_id);

                    // entity.addPart("name[]",cbFile);


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                HttpResponse response = httpClient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = String.valueOf(statusCode);
                    Log.w("Upload_Image","Response_here:::: "+ EntityUtils.toString(r_entity));
                } else {
                    responseString = String.valueOf(statusCode);
                    Log.w("Upload_Image_Error","Response_here:::: "+EntityUtils.toString(r_entity));
                }

            } catch (ClientProtocolException e) {
                Log.e("excepto","ClientProtocolException:::: "+e);
                responseString = "404";
            } catch (IOException e) {
                Log.e("excepto","IOException:::: "+e);
                responseString = "404";
            }
            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            hideProgress();
            fragment=new CustomerServiceFragment();
            ((CustomerDashBoard)getActivity()).addservice();
            loadfragment(fragment);
            Log.e("seuccess", "Response from server: " + result);
            super.onPostExecute(result);
        }
    }


    private boolean copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return false;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            Log.d("success","destination:: "+destination);
            destination.close();
        }

        return true;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        String path = "";

        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                Log.d("Success", "image_path::" + path);
                cursor.close();
            }
        }
        return path;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadfragment(Fragment fragment) {
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void SpinnerInvoice(String id){
        JSONObject object=new JSONObject();
        try {
            object.put("customer_id",id);
            doback(Constants.CUS_SERVICE_SPINNER_INV,object);
            Log.d("Success","customer_spinner::"+object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doback(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","Warranty_service_spinner_inv_responce::"+response);

                if (response.getString("status").equalsIgnoreCase("success")){
                    JSONArray array=response.getJSONArray("data");
                    ArrayList arrayList=new ArrayList();
                    arrayList.add(0, "Select Invoice");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        String id=obj.getString("id");
                        String invoice=obj.getString("inv_id");
                        String[] iv={invoice};
                        for (int j=0;j<iv.length;j++){
                            arrayList.add(iv[j]);
                        }
                        ArrayAdapter<String>adapter=new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                                R.layout.support_simple_spinner_dropdown_item,arrayList);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);
                    }
                }else {
                    Toast.makeText(getActivity(),response.getString("message")
                            ,Toast.LENGTH_SHORT).show();
                }

                return null;
            }
        });
    }

    private void warrantymethod() throws JSONException {
        showProgress();
        JSONObject object=new JSONObject();
        //  object.put("customer_id",cus_id);
        object.put("inv_id",adapter_id);
        Log.d("Success","Spinner_details_Request::"+object);
        if (adapter_id.equals("Select Invoice")){
            hideProgress();
            Log.d("Success","adpater_id_setText_test_if::"+adapter_id);
            expiry_id.setText("");
            warranty_invoice_id.setText("");
      //      project_des.setText("");
            warranty_ticket_id.setText("");
            warranty_date_id.setText("");
            invoice_amt.setText("");
            warranty_status.setText("");
            paid_service_tv.setText("");
            arrayList=new ArrayList<>();
            WarrantyListModel model=new WarrantyListModel();
            model.setProduct_name("");
            model.setProduct_des("");
            model.setInv_not_select(adapter_id);
            arrayList.add(model);

            LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
            warranty_recycler.setLayoutManager(lrt);
            WarrantyListAdapter adapter=new WarrantyListAdapter(getActivity(), arrayList);
            Log.d("Success","arraysize_in_noselect::"+arrayList.size());
            warranty_recycler.setAdapter(adapter);

            workperformed();
        }else {
            Log.d("Success","adapter_id_send_request::"+object);
            SendinBackground(Constants.CUS_SERVICE_SPINNER_INV_DETAILS, object);
        }
    }

    private void SendinBackground(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url,object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
                hideProgress();
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","Spinner_details_responce::"+response);
                hideProgress();
                if (response.getString("status").equalsIgnoreCase("false")){
                    Toast.makeText(getActivity(),response.getString("message")
                            ,Toast.LENGTH_SHORT).show();
                }
                arrayList=new ArrayList<>();
                JSONArray array=response.getJSONArray("data");

                for (int i=0;i<array.length();i++){
                    JSONObject obj=array.getJSONObject(i);
                    expiry=obj.getString("warranty_to");
                    warranty=obj.getString("warranty");
                    //  at_id=obj.getString("attendant_id");
                    at_name=obj.getString("name");
                    in_amt=obj.getString("net_total");
                    invoice=obj.getString("inv_id");
                    tk_no=obj.getString("ticket_no");
                    in_date=obj.getString("created_date");

                    String s_value=obj.getString("ticket_no");
                    Log.d("Success","tk_no_test::"+s_value);

                    if (!s_value.equals("")){
                        request_inv=updateIndex(Integer.parseInt(s_value));
                        Log.w("request_inv_test::",s_value);
                        Log.w("request_inv_test::",request_inv);
                    }else {
                        request_inv=updateIndex(0);
                        Log.w("request_inv_test_else::",request_inv);
                    }

                    StringBuilder builder=new StringBuilder();
                    String[] split=expiry.split("-");
                    String y=split[0];
                    String m=split[1];
                    String d=split[2];
                    builder.append(d).append("/").append(m).append("/").append(y).toString();
                    String date= new String(builder);
                    StringBuilder builder1=new StringBuilder();
                    String[] split1=in_date.split("-");
                    String a=split1[0];
                    String b=split1[1];
                    String c=split1[2];
                    builder1.append(c).append("/").append(b).append("/").append(a).toString();
                    date1= new String(builder1);
                    Log.d("Success","builder_value2::"+date1);

                    if (warranty.equals("Available")){
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        SpannableString str1= new SpannableString("(Free Warranty is ");
                        spannableStringBuilder.append(str1);
                        SpannableString str2= new SpannableString(warranty);
                        str2.setSpan(new ForegroundColorSpan(Color.parseColor("#06801a")), 0, str2.length(), 0);
                        spannableStringBuilder.append(str2);
                        SpannableString str3= new SpannableString(")");
                        spannableStringBuilder.append(str3);
                        warranty_status.setText( spannableStringBuilder, TextView.BufferType.SPANNABLE);
                    }else {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        SpannableString str1= new SpannableString("(Free Warranty is ");
                        spannableStringBuilder.append(str1);
                        SpannableString str2= new SpannableString("Not Available");
                        str2.setSpan(new ForegroundColorSpan(Color.parseColor("#fd0606")), 0, str2.length(), 0);
                        spannableStringBuilder.append(str2);
                        SpannableString str3= new SpannableString(")");
                        spannableStringBuilder.append(str3);
                        warranty_status.setText( spannableStringBuilder, TextView.BufferType.SPANNABLE);
                        paid_service_tv.setText("Please visit paid service");
                    }

                    expiry_id.setText(date);
                    warranty_invoice_id.setText(invoice);
                    warranty_ticket_id.setText(request_inv);
                    warranty_date_id.setText(date1);
                    invoice_amt.setText(in_amt);


                    workperformed();
                    //  attendent_name.setText(at_name);
                    //  inv_date.setText(date1);

                    editor.remove(KEY_INV_USER);
                    editor.remove(KEY_INV_NO);
                    editor.remove(KEY_TK_NO);
                    editor.remove(KEY_WARRANTY);
                    editor.remove(KEY_ATN_NAME);
                    //  editor.remove(KEY_ISSUE);
                    //  editor.remove(KEY_SERVICE_TYPE);
                    editor.putString(KEY_INV_USER, cus_id);
                    editor.putString(KEY_INV_NO,invoice);
                    editor.putString(KEY_TK_NO,request_inv);
                    editor.putString(KEY_WARRANTY,warranty);
                    editor.putString(KEY_ATN_NAME,at_name);
                    session.createService(cus_id,invoice,tk_no,warranty,"",at_name,"","");
                    editor.commit();
                    //  editor.putString(KEY_ISSUE);
                    //  editor.putString(KEY_SERVICE_TYPE);
                    JSONArray array1 = obj.getJSONArray("invoice_details");
                    for (int j=0;j<array1.length();j++){
                        JSONObject obj1=array1.getJSONObject(j);
                        WarrantyListModel model=new WarrantyListModel();
                        model.setProduct_name(obj1.getString("product_name"));
                        model.setProduct_des(obj1.getString("product_description"));
                        model.setInv_not_select(adapter_id);
                        arrayList.add(model);
                        String pro=obj1.getString("product_name");
                        String des=obj1.getString("product_description");
                      //  project_des.setText(des);

                    }

                }
                LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                warranty_recycler.setLayoutManager(lrt);
                WarrantyListAdapter adapter=new WarrantyListAdapter(getActivity(), arrayList);
                warranty_recycler.setAdapter(adapter);
                Log.d("Success:","responce:::"+response);

                if (response.getString("status").equalsIgnoreCase("Success")) {
                    // JSONArray array = response.getJSONArray("data");
                }else {

                }
                return null;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_warranty, (ViewGroup) container, false);
    }



    @Override
    public void showProgress() {
        loader_img.setVisibility(View.VISIBLE);
        warranty_scroll.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        loader_img.setVisibility(View.GONE);
        warranty_scroll.setVisibility(View.VISIBLE);
    }

    public String updateIndex(int count){
        String value=null;
        if(count<=1000){
            fx="%03d";
        }else{
            fx="%04d";
        }
        count++;
        counter=String.format(fx,count);
        System.out.println("Updated value:"+(prefix+counter));
        warranty_ticket_id.setText(prefix+counter);
        value=prefix+counter;
        return value;
    }
    public void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Log.d("Success","keyboard_test_inside::");
    }
    private static class GetFilePathFromDevice {
        public static String getPath(final Context context, final Uri uri) {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }

        public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        public static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }
    }
}
