package com.f2f.incls.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.EmployeeDashboard;
import com.f2f.incls.adapter.CustomerUploadImgAdapter;
import com.f2f.incls.adapter.EmpUploadImgAdapter;
import com.f2f.incls.adapter.WarrantyListAdapter;
import com.f2f.incls.adapter.WorkperformedAdapter;
import com.f2f.incls.model.EmpSpinnerModel;
import com.f2f.incls.model.EmpStatusUpdateModel;
import com.f2f.incls.model.EmpUploadImgModel;
import com.f2f.incls.model.WarrantyListModel;
import com.f2f.incls.model.WorkPerformedModel;
import com.f2f.incls.utilitty.AndroidMultiPartEntity;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.CustomerUploadModel;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;
import com.google.gson.Gson;

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
import java.util.StringTokenizer;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

@SuppressLint("ValidFragment")
public class EmpEditServiceFragment extends Fragment implements AdapterView.OnItemSelectedListener,LoadinInterface {
    private static final int STORAGE_PERMISSION_CODE = 1;
    Spinner status_spinner;
    Button emp_edit_service_cancel,emp_edit_service_submit;
    GifImageView loader_img;
    ImageView photo_upload;
    LinearLayout emp_edit_ly;
    TextView emp_edit_about_issue,warranty_tv,current_status,edit_invoice,edit_ticket,edit_date,
            edit_invoice_amt,attender_id,task_date;
    EditText emp_work_id;
    LinearLayout emp_layout;
    String[] task_status = {"Select Status","Inprogress", "Complete","pending"};
    RecyclerView emp_edit_service_rec,emp_status_update_rec,upload_img_show_reg,emp_img_upload_rec,workperformed_rec;
    ArrayList<EmpStatusUpdateModel>statusList=new ArrayList<>();
    ArrayList<EmpSpinnerModel> testList=new ArrayList();
    ArrayList<CustomerUploadModel>uploadArrayList=new ArrayList<>();
    ArrayList<EmpUploadImgModel>empuploadArrayList=new ArrayList<>();
    ArrayList<String>imageselectList=new ArrayList<>();

    String imagePath,store_path,id,click_pos,date1,spinner_value;
    private int REQUEST_CAMERA = 100, SELECT_FILE = 200;
    private String userChoosenTask,emp_id;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 0;
    private static final int PERMISSION_REQUEST_CODE = 200;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;

    public static String b_warranty,b_status,b_inv_no,b_ticket,b_date,b_pro_name,
            b_pro_des,b_inv_amt,b_issue,b_image,b_at_name,
            b_service_details,b_service_id,image_type;
    ArrayList<WarrantyListModel> arrayList;
    ArrayList<CustomerUploadModel> ImageList;

    public EmpEditServiceFragment(ArrayList<WarrantyListModel> arrayList,
                                  ArrayList<CustomerUploadModel> ImageList) {
        this.arrayList=arrayList;
        this.ImageList=ImageList;
        Log.d("Success","Construct_test::"+arrayList.size());
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((EmployeeDashboard) Objects.requireNonNull(getActivity())).empservicelist();
        findviewid(view);
        ((EmployeeDashboard)getActivity()).adsimageget();
        ((EmployeeDashboard)getActivity()).hideads();
        HashMap<String, String> user = session.getempdetails();
        final String customer_id = user.get(SessionManager.KEY_USER_ID);
        emp_id = user.get(KEY_USER_ID);
        String cutomer_type = "service";
        String services_id =getArguments().getString("service_id");

       /* emp_edit_ly.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((EmployeeDashboard)getActivity()).hideKeyboard(v);
                return false;
            }
        });

        emp_edit_service_rec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((EmployeeDashboard)getActivity()).hideKeyboard(v);
                return false;
            }
        });*/
        try {
            workperformedhistory(customer_id,cutomer_type,services_id,emp_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        emp_edit_service_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new EmpServiceFragment();
                FragmentTransaction tr=getFragmentManager().beginTransaction();
                tr.replace(R.id.frame_container_emp,fragment);
                tr.addToBackStack(null);
                tr.commit();
            }
        });
        photo_upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPictureDialog();

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, task_status);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        status_spinner.setAdapter(adapter);

        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_value=status_spinner.getSelectedItem().toString();
                Log.d("Success","Spinner_select_test::"+spinner_value);
                String pos = task_status[position];
                Log.d("Success","recyclerview_pos::"+pos);
                if (pos.equalsIgnoreCase("pending")) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#fd0606"));
                }
                if (pos.equalsIgnoreCase("Inprogress")) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#ff7e00"));
                }
                if (pos.equalsIgnoreCase("Complete")) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#06801a"));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Success","Spinner_test_not_selected::");
            }
        });
        emp_edit_service_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emp_work_id.getText().toString().isEmpty() /*|| imagePath==null*/
                        ||spinner_value.equals("Select Status")){
                    if (emp_work_id.getText().toString().isEmpty()){
                        Toast.makeText(getActivity(),"Please enter work performed"
                                ,Toast.LENGTH_SHORT).show();
                    }/*if (imagePath==null){
                        Toast.makeText(getActivity(),"Please select image"
                                ,Toast.LENGTH_SHORT).show();
                    }*/if (spinner_value.equals("Select Status")){
                        Toast.makeText(getActivity(),"Please select status"
                                ,Toast.LENGTH_SHORT).show();
                    }

                }else {
                    new UploadFileToServer(imageselectList).execute();
                }

                //    if (issue.isEmpty() || imagePath==null ||adapter_id.equals("Select Invoice")){
            }
        });
        Bundle bundle=getArguments();
        String edit=bundle.getString("edit_service");
        Log.d("Success","edit_service_test::"+edit);
        if(edit.equals("Edit")){
            Service_to_edit(bundle);
        }else {
            Today_task_edit(bundle);
            Log.d("Success","today_task_else_test::");
        }
    }
    private void workperformedhistory(String id, String cutomer_type,String services_id,String emp_id) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("customer_id",id);
        object.put("service_id",services_id);
        object.put("service_type",cutomer_type);
        object.put("emp_id",emp_id);
        //  object.put()
        //   object.put("inv_no", adapter_id);
        Log.d("Success", "Work_performed_request::" + object);
        Log.d("success","ser89t"+emp_id);
        Log.d("success","custo89"+cutomer_type);
        Log.d("success","typoo89"+id);
        Log.d("success","service89"+services_id);
        runback(Constants.SERVICE_HISTORY, object);
    }

    private void runback(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success", "Work_performed_responce25::" + response);
                ArrayList<WorkPerformedModel> workList = new ArrayList<>();
                if (response.getString("status").equalsIgnoreCase("success")) {
                    JSONArray array = response.getJSONArray("data");
                    Log.d("Success", "Work_performed_array::" + array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj1 = array.getJSONObject(i);
                        WorkPerformedModel model = new WorkPerformedModel();
                        model.setAttendant_name(obj1.getString("name"));
                        model.setService_date(obj1.getString("created_date"));
                        model.setAttendantmobile_number(obj1.getString("mobile_no"));
                        model.setStatus(obj1.getString("work_status"));
                        model.setEmp_upload_image(obj1.getString("emp_image"));
                        model.setservice_id(obj1.getString("service_id"));
                        model.setWork_performed(obj1.getString("work_performed"));
                        //    String ser_id = (obj1.getString("service_id"));
                        //    model.setInv_select(adapter_id);


                    /*    JSONArray array1 = obj.getJSONArray("employee_image_upload");
                        ArrayList<String> images = new ArrayList<>();
                        for (int j = 0; j < array1.length(); j++) {
                            JSONObject obj1 = array1.getJSONObject(j);
                            String path = obj1.getString("img_path");
                            images.add(path);
                        }*/
                        //  model.setWarrantyList(images);
                        Log.d("Success:","history123"+ new Gson().toJson(model));
                        workList.add(model);
                    }

                    LinearLayoutManager lrs = new LinearLayoutManager(getActivity());
                    workperformed_rec.setLayoutManager(lrs);
                    WorkperformedAdapter adapter1 = new WorkperformedAdapter(getActivity(), workList);
                    workperformed_rec.setAdapter(adapter1);

                   /* for (WorkPerformedModel model:workList) {
                        for (String s : model.getWarrantyList()) {
                            Log.w("Images:", s);
                        }*/



                } /*else {
                    if (response.getString("status").equalsIgnoreCase("false")) {
                        String no_data = response.getString("message");
                        Log.d("Success", "no_data::" + no_data);
                      ;  emp_work_id.setVisibility(View.GONE);
                        no_service_data.setVisibility(View.VISIBLE);
                        no_service_data.setText(no_data)
                    }*/

                //   }
                return null;
            }
        });
    }


    private void Today_task_edit(Bundle bundle) {
        b_warranty = bundle.getString("warranty");
        b_status = bundle.getString("status");
        b_inv_no = bundle.getString("invoice_no");
        b_ticket = bundle.getString("ticket");
        b_date = bundle.getString("date");
        b_pro_name = bundle.getString("pro_name");
        b_pro_des = bundle.getString("pro_des");
        b_inv_amt = bundle.getString("inv_amt");
        b_issue = bundle.getString("issue");
        b_image = bundle.getString("image");
        b_at_name = bundle.getString("at_name");
        b_service_details=bundle.getString("service_details_id");
        b_service_id=bundle.getString("service_id");
        Log.d("Success","today_task_service_id::"+b_service_id);

        for (int i=0;i<ImageList.size();i++){
            CustomerUploadModel model=ImageList.get(i);
       //     if (id.equals(b_service_id)){
            if(id != null && id.equals("b_service_id")){
            String id=model.getService_id();
            Log.d("Success","model_id::"+id);
                String path=model.getImage_path();
                CustomerUploadModel model1=new CustomerUploadModel();
                model1.setImage_path(path);
                uploadArrayList.add(model1);
            }
        }
        Log.d("Success","service_id_test::"+b_service_id);

        StringBuilder builder=new StringBuilder();
        StringTokenizer tk = new StringTokenizer(b_date);
        String date = tk.nextToken();  // <---  yyyy-mm-dd
      //  String time = tk.nextToken();

        String[] split=date.split("/");
        String y=split[2];
        String m=split[1];
        String d=split[0];

        builder.append(d).append("/").append(m).append("/").append(y).toString();
        date1=new String(builder);

        if (b_warranty.equals("Available") || b_warranty.equals("available")){
            String tv="Available";
            warranty_tv.setText(tv);
            warranty_tv.setTextColor(Color.parseColor("#06801a"));
        }else {
            String tv="Not Available";
            warranty_tv.setText(tv);
            warranty_tv.setTextColor(Color.parseColor("#ff0000"));
        }

        warranty_tv.setText(b_warranty);
        if (b_status.equals("1")){
            String tv="Complete";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#06801a"));
        }else if(b_status.equals("2")) {
            String tv="Pending";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#fd0606"));
        }else {
            String tv="Inprogress";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#ff7e00"));
        }
        edit_invoice.setText(b_inv_no);
        edit_ticket.setText(b_ticket);
        edit_date.setText(date1);
        edit_invoice_amt.setText(b_inv_amt);
        emp_edit_about_issue.setText(b_issue);

        //Recyclerview product name,product description
        LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
        emp_edit_service_rec.setLayoutManager(lrt);
        WarrantyListAdapter adapter=new WarrantyListAdapter(getActivity(), arrayList);
        emp_edit_service_rec.setAdapter(adapter);

        //Recyclerview image show
       /* CustomerUploadModel c_model=new CustomerUploadModel();
        c_model.setImage_path(b_image);
        uploadArrayList.add(c_model);*/

        LinearLayoutManager lrt1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        upload_img_show_reg.setLayoutManager(lrt1);
        CustomerUploadImgAdapter adapter1 = new CustomerUploadImgAdapter(getActivity(), uploadArrayList);
        upload_img_show_reg.setAdapter(adapter1);

        //Employee work performed

        attender_id.setText(b_ticket);
        task_date.setText(date1);

    }

    private void Service_to_edit(Bundle bundle) {

        if (bundle!=null) {
            b_warranty = bundle.getString("warranty");
            b_status = bundle.getString("status");
            b_inv_no = bundle.getString("invoice_no");
            b_ticket = bundle.getString("ticket");
            b_date = bundle.getString("date");
            b_pro_name = bundle.getString("pro_name");
            b_pro_des = bundle.getString("pro_des");
            b_inv_amt = bundle.getString("inv_amt");
            b_issue = bundle.getString("issue");
            b_image = bundle.getString("img_path");
            b_at_name = bundle.getString("ticket");
            b_service_details=bundle.getString("service_details_id");
            b_service_id=bundle.getString("service_id");
            image_type=bundle.getString("image_type");

        }
        for (int i=0;i<ImageList.size();i++){
            CustomerUploadModel model=ImageList.get(i);
            String id=model.getService_id();

            if (id.equals(b_service_id)){
                Log.d("Success","service_type::"+image_type);
                CustomerUploadModel model1=new CustomerUploadModel();
                String path=model.getImage_path();
                model1.setImage_path(path);
                uploadArrayList.add(model1);
            }
        }

        StringBuilder builder=new StringBuilder();
        StringTokenizer tk = new StringTokenizer(b_date);
        String date = tk.nextToken();  // <---  yyyy-mm-dd
       // String time = tk.nextToken();

        String[] split=date.split("/");
        String y=split[2];
        String m=split[1];
        String d=split[0];

        builder.append(d).append("/").append(m).append("/").append(y).toString();
        date1=new String(builder);

        if (b_warranty.equals("Available") || b_warranty.equals("available")){
            String tv="Available";
            warranty_tv.setText(tv);
            warranty_tv.setTextColor(Color.parseColor("#06801a"));
        }else {
            String tv="Not Available";
            warranty_tv.setText(tv);
            warranty_tv.setTextColor(Color.parseColor("#ff0000"));
        }

        warranty_tv.setText(b_warranty);
        if (b_status.equals("1")){
            String tv="Complete";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#06801a"));
        }else if(b_status.equals("2")) {
            String tv="Pending";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#fd0606"));
        }else {
            String tv="Inprogress";
            current_status.setText(tv);
            current_status.setTextColor(Color.parseColor("#ff7e00"));
        }
        edit_invoice.setText(b_inv_no);
        edit_ticket.setText(b_ticket);
        edit_date.setText(b_date);
        edit_invoice_amt.setText(b_inv_amt);
        emp_edit_about_issue.setText(b_issue);

        //Recyclerview product name,product description
        LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
        emp_edit_service_rec.setLayoutManager(lrt);
        WarrantyListAdapter adapter=new WarrantyListAdapter(getActivity(), arrayList);
        emp_edit_service_rec.setAdapter(adapter);

        //image show recyclerview


        LinearLayoutManager lrt1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        upload_img_show_reg.setLayoutManager(lrt1);
        CustomerUploadImgAdapter adapter1 = new CustomerUploadImgAdapter(getActivity(), uploadArrayList);
        upload_img_show_reg.setAdapter(adapter1);

        //Employee work performed

        //    workperformed(b_inv_no,emp_id);

        attender_id.setText(b_at_name);
        task_date.setText(date1);
}

    private void workperformed(String b_inv_no, String emp_id) {
        JSONObject object=new JSONObject();
        //   object.put("")
    }

    private void showPictureDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
            //    boolean result = VolleyUtils.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (VolleyUtils.permission(getActivity())) {
                        cameraIntent();
                    }

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (VolleyUtils.permission(getActivity())) {
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
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Log.d("Success","camera_test::");
            startActivityForResult(intent, REQUEST_CAMERA);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;


        if(requestCode==REQUEST_CAMERA && resultCode==RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selectedImageUri = getImageUri(getActivity(), photo);
            imagePath = getRealPathFromURI(selectedImageUri);

            EmpUploadImgModel model=new EmpUploadImgModel();
            model.setImage_path(imagePath);
            empuploadArrayList.add(model);

            LinearLayoutManager lrt = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            emp_img_upload_rec.setLayoutManager(lrt);
            EmpUploadImgAdapter adapter = new EmpUploadImgAdapter(getActivity(), empuploadArrayList);
            emp_img_upload_rec.setAdapter(adapter);
            try {
                File sdcard = Environment.getExternalStorageDirectory();
                String targetPath = sdcard.getAbsolutePath() + File.separator + "INS" + File.separator + "Images" + File.separator;
                File folder = new File(targetPath);
                folder.mkdirs();

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                String imageFileName =randomUUIDString + ".jpg";
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
                    imageselectList.add(imagePath);
                }
            } catch(Exception e){
                Log.e("success", "Exception:: " + e);

            }
        }
        if (requestCode==SELECT_FILE && resultCode==RESULT_OK) {
            imagePath = CustomerPaidFragment.GetFilePathFromDevice.getPath(getActivity(), data.getData());
            Log.d("Success", "selected_uri_gallery::" + imagePath);
            EmpUploadImgModel model = new EmpUploadImgModel();
            model.setImage_path(imagePath);
            Log.d("Success", "gallery_image_path::" + imagePath);
            empuploadArrayList.add(model);
            Log.d("Success", "upload_Array_size::" + empuploadArrayList.size());

            Log.d("Success", "click_pos::" + click_pos);
            LinearLayoutManager lrt = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            emp_img_upload_rec.setLayoutManager(lrt);
            EmpUploadImgAdapter adapter = new EmpUploadImgAdapter(getActivity(), empuploadArrayList);
            emp_img_upload_rec.setAdapter(adapter);

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            store_path = imagePath;
            Bundle bundle = new Bundle();
            bundle.putString("user_profile", store_path);
            setArguments(bundle);
            Log.d("Success", "Store_path::" + store_path);
            try {
                File sdcard = Environment.getExternalStorageDirectory();
                String targetPath = sdcard.getAbsolutePath() + File.separator + "INS" + File.separator + "Images" + File.separator;
                File folder = new File(targetPath);
                folder.mkdirs();

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                String imageFileName = randomUUIDString + ".jpg";
                String destination_file_name = targetPath + imageFileName;
                Log.d("success", "imageFilePath:::" + imagePath);
                File source_fiel = new File(imagePath);

                File destination_file = new File(destination_file_name);
                Log.d("success", "destination_file:: " + destination_file_name);

                if (!destination_file.exists()) {
                    destination_file.createNewFile();
                    Log.d("success", "created:: ");
                }

                if (copyFile(source_fiel, destination_file)) {
                    imagePath = destination_file_name;
                    imageselectList.add(imagePath);
                }
            } catch (Exception e) {
                Log.e("success", "Exception:: " + e);

            }
            //  Toast.makeText(getActivity(), selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    private void findviewid(View view) {
        status_spinner = view.findViewById(R.id.emp_status_spinner);
        emp_edit_service_rec = view.findViewById(R.id.emp_edit_service_rec);
        loader_img = view.findViewById(R.id.loader_img);
        emp_layout=view.findViewById(R.id.emp_layout);
        //    emp_status_update_rec=view.findViewById(R.id.emp_status_update_rec);
        emp_edit_about_issue=view.findViewById(R.id.emp_edit_about_issue);
        emp_edit_ly=view.findViewById(R.id.emp_edit_ly);
        warranty_tv=view.findViewById(R.id.warranty_tv);
        current_status=view.findViewById(R.id.current_status);
        edit_invoice=view.findViewById(R.id.edit_invoice);
        edit_ticket=view.findViewById(R.id.edit_ticket);
        edit_date=view.findViewById(R.id.edit_date);
        edit_invoice_amt=view.findViewById(R.id.edit_invoice_amt);
        upload_img_show_reg=view.findViewById(R.id.upload_img_show_reg);
        attender_id=view.findViewById(R.id.attender_id);
        task_date=view.findViewById(R.id.task_date);
        emp_edit_service_submit=view.findViewById(R.id.emp_edit_service_submit);
        emp_edit_service_cancel=view.findViewById(R.id.emp_edit_service_cancel);
        photo_upload=view.findViewById(R.id.photo_upload);
        emp_img_upload_rec=view.findViewById(R.id.emp_img_upload_rec);
        emp_work_id=view.findViewById(R.id.emp_work_id);
        workperformed_rec=view.findViewById(R.id.workperformed_rec);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getActivity());
        sharpre = this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
        if(checkPermission()){
            return;
        }else {
            requestPermission();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services_fragment_em, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), task_status[position], Toast.LENGTH_LONG).show();
        String pos = task_status[position];
        if (pos.equalsIgnoreCase("pending")) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#fd0606"));
        }
        if (pos.equalsIgnoreCase("Inprogress")) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#ff7e00"));
        }
        if (pos.equalsIgnoreCase("Complete")) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#06801a"));
        }
        Log.d("Success", "spinner_position::" + pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class UploadFileToServer extends AsyncTask<String, Integer, String> {
        long totalSize=0;
        boolean isAudio=false;
        //  String filePathurl;
        ProgressDialog progressDialog;
        ArrayList<String> filePath;

        private UploadFileToServer(ArrayList<String> filePath) {
            this.filePath=filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Log.d("succes","updatng::: "+String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String... params) {
            return uploadFile(filePath);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(ArrayList<String> filePath) {
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
            String url = Constants.EMP_EDIT_SERVICE;

            HttpPost httppost = new HttpPost(url);

            httppost.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials("admin", "1234"), "UTF-8", false));

            Log.d("success","image_url::::"+url);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                String emp_work=emp_work_id.getText().toString();
                String status=spinner_value;
                String service_details=b_service_id;
                Log.d("Success","work_performed::"+emp_work);
                Log.d("Success","status12::"+status);
                Log.d("Success","service_detail::"+service_details);
                Log.d("Success","image_path::"+imagePath);
                String status_1=null;
                if (status.equals("Inprogress")){
                    status_1="0";
                }else if(status.equals("pending")){
                    status_1="2";
                }else {
                    status_1="1";
                }
                Log.d("Success","status12::"+status_1);

                // File sourceFile = new File(filePath);
                ContentBody work=new StringBody(emp_work);
                ContentBody service_id=new StringBody(service_details);
                ContentBody select_status=new StringBody(status_1);

                if (filePath!=null) {
                    for (int i = 0; i < filePath.size(); i++) {
                        File file = new File(filePath.get(i));
                        ContentBody cbfile = new FileBody(file);
                        entity.addPart("name[]", cbfile);
                    }
                }
                    // entity.addPart("emp_image_upload", new FileBody(sourceFile));
                    entity.addPart("work_performed", work);
                    entity.addPart("status", select_status);
                    entity.addPart("service_id", service_id);



                //  Log.d("Success","edit_service_list_inside::"+sourceFile);
                Log.d("Success","edit_service_list_inside::"+emp_work);
                Log.d("Success","edit_service_list_inside::"+service_details);
                Log.d("Success","edit_service_list_inside::"+status_1);
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
            Log.e("seuccess", "Response from server::"+result);
            super.onPostExecute(result);
            hideProgress();
            Fragment fragment=new EmpServiceFragment();
            FragmentTransaction transaction=getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container_emp,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    @Override
    public void showProgress() {
        loader_img.setVisibility(View.GONE);
        emp_layout.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        loader_img.setVisibility(View.GONE);
        emp_layout.setVisibility(View.VISIBLE);
    }

}
