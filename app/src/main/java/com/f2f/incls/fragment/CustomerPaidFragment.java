package com.f2f.incls.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION_CODES;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.PaidCustomerImgUploadAdapter;
import com.f2f.incls.model.PaidCustomerImgUploadModel;
import com.f2f.incls.model.PaidworkImgUploadModel;
import com.f2f.incls.utilitty.AndroidMultiPartEntity;
import com.f2f.incls.utilitty.Constants;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class CustomerPaidFragment extends Fragment implements LoadinInterface {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    Button customer_paid_cancel,customer_paid_submit;
    ImageView img_upload_icon,paid_image_upload;
    GifImageView loader_img;
    TextView paid_service_today_date,cus_paid_inv;
    EditText paid_service_issue;
    RecyclerView paid_upload_img_reg,paid_work_rec;
    String imagePath,store_path,id,click_pos;
    private int REQUEST_CAMERA = 100, SELECT_FILE = 200;
    String userChoosenTask,cus_id,today_date;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ArrayList<PaidCustomerImgUploadModel>cusuploadList=new ArrayList<>();
    ArrayList<PaidworkImgUploadModel>workuploadList=new ArrayList<>();
    ArrayList<String> uploadImage =new ArrayList<>();

    public static String counter = "0000000000";
    public static int count;
    public static String prefix= "TKNO-";
    public static String fx;
   // String request_ticket;
    LinearLayout paid_load_ly;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviewid(view);
        ((CustomerDashBoard)getActivity()).adsclose();
        ((CustomerDashBoard)getActivity()).adsimageget();
        //ticketmethod();
       // cus_paid_inv.setText(request_ticket);
     //   Log.d("Success","request_inv::"+request_ticket);
        img_upload_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Log.d("Success","upload_img::");
               click_pos="1";
                showPictureDialog();
            }
        });
        paid_image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_pos="2";
                showPictureDialog();
            }
        });

        customer_paid_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new CustomerServiceFragment();
                loadfragment(fragment);
            }
        });
        customer_paid_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paid_service_issue.getText().toString().isEmpty()/* ||imagePath==null*/){
                    if (paid_service_issue.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter about issue"
                                , Toast.LENGTH_SHORT).show();
                    }
                   /* if (imagePath==null){
                        Toast.makeText(getActivity(),"Please select image"
                                ,Toast.LENGTH_SHORT).show();
                    }*/
                }else {
                    new UploadFileToServer(uploadImage,imagePath).execute();
                }

            }
        });



        today_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Log.d("Success","date::"+today_date);
        paid_service_today_date.setText(today_date);

    }

    private boolean permission() {
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(VERSION_CODES.JELLY_BEAN)
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

/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Log.d(TAG, "camera2 selected");
        this.camera = new Camera2(getContext());
    } else {
        Log.d(TAG, "camera1 selected");
        this.camera = new Camera1(getContext());
    }*/
    /*private void ticketmethod() {
            SendinBackground(Constants.LAST_TICKET_NO_GET);
    }
    private void SendinBackground(String url) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url,null, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
               // hideProgress();
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                Log.d("Success","get_last_ticket_no::"+response);
                   String s_value=response.getString("data");
                    Log.d("Success","tk_no_test::"+s_value);

                    int int_value=Integer.parseInt(s_value);
                    Log.d("Success","tk_no_test_int::"+int_value);

                    if (!s_value.equals("0")){
                        request_ticket=updateIndex(int_value);
                        cus_paid_inv.setText(request_ticket);
                        Log.d("Success","request_inv_test::"+request_ticket);
                    }else {
                        request_ticket=updateIndex(0);
                        cus_paid_inv.setText(request_ticket);
                        Log.d("Success","request_inv_test_else::"+request_ticket);
                    }
                return s_value;
            }
        });*/
  //  }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;

        if(requestCode==REQUEST_CAMERA && resultCode==RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selectedImageUri = getImageUri(getActivity(),photo);
            Log.d("Success","camera_selected_images::"+selectedImageUri);
            imagePath=getRealPathFromURI(selectedImageUri);

            PaidCustomerImgUploadModel model=new PaidCustomerImgUploadModel();
            model.setImagepath(imagePath);
            cusuploadList.add(model);
            LinearLayoutManager lrt = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            paid_upload_img_reg.setLayoutManager(lrt);
            PaidCustomerImgUploadAdapter adapter = new PaidCustomerImgUploadAdapter(getActivity(), cusuploadList);
            paid_upload_img_reg.setAdapter(adapter);

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
                    uploadImage.add(imagePath);
                }

            } catch(Exception e){
                Log.e("success", "Exception:: " + e);

            }
        }else {

            if (data.getData() != null) {
                 imagePath =GetFilePathFromDevice.getPath(getActivity(), data.getData());
                Log.d("Success","selected_uri_gallery::"+imagePath);
                PaidCustomerImgUploadModel model = new PaidCustomerImgUploadModel();
                model.setImagepath(imagePath);
                cusuploadList.add(model);

                LinearLayoutManager lrt = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                paid_upload_img_reg.setLayoutManager(lrt);
                PaidCustomerImgUploadAdapter adapter = new PaidCustomerImgUploadAdapter(getActivity(), cusuploadList);
                paid_upload_img_reg.setAdapter(adapter);

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
                        uploadImage.add(imagePath);
                    }
                } catch (Exception e) {
                    Log.e("success", "Exception:: " + e);

                }
            }
        }

        if (requestCode==SELECT_FILE && resultCode==RESULT_OK && data!=null){


        }



    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void showProgress() {
        loader_img.setVisibility(View.GONE);
        paid_load_ly.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loader_img.setVisibility(View.GONE);
        paid_load_ly.setVisibility(View.VISIBLE);
    }

    public class UploadFileToServer extends AsyncTask<String, Integer, String> {
        long totalSize=0;
        ContentBody cbfile;
        boolean isAudio=false;
        String filePathurl;
        ArrayList<String> uploadImage;

        private UploadFileToServer(ArrayList<String> uploadImage,String imagePath) {
            this.uploadImage=uploadImage;
            this.filePathurl=imagePath;
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
            try {
                return uploadFile(uploadImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(ArrayList<String> uploadImage) throws IOException {
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

                HashMap<String,String>map=session.getcustomerdetails();
                 cus_id=map.get(KEY_USER_ID);
                 String service_type="warranty";
                String issue=paid_service_issue.getText().toString();


               // File sourceFile = new File(filePath);
                ContentBody e_id = new StringBody(cus_id);
              //  ContentBody t_id = new StringBody(request_ticket);
                ContentBody issue_id = new StringBody(issue);
                ContentBody service_id = new StringBody(service_type);
               // ContentBody path_id = new FileBody(sourceFile);

                Log.d("success","customer_id_paid::::"+ cus_id);
                Log.d("success","service_type_paid::::"+service_type);
                Log.d("success","issue_paid::::"+issue);
             //   Log.d("success","reques_ticket::::"+request_ticket);
               // Log.d("success","image_path_id_send::::"+path_id);

                if (uploadImage!=null) {
                    for (int i = 0; i < uploadImage.size(); i++) {
                        File file = new File(uploadImage.get(i));
                        cbfile = new FileBody(file);
                        entity.addPart("name[]", cbfile);
                    }
                }


                    entity.addPart("customer_id",e_id);
                   // entity.addPart("ticket_no",t_id);
                    entity.addPart("description",issue_id);
                    entity.addPart("service_type",service_id);
                   Log.d("success","serviceadd"+entity);



                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                HttpResponse response = httpClient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = String.valueOf(statusCode);
                    Log.w("Upload_Image","Response_here::::"+ EntityUtils.toString(r_entity));
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
          Fragment  fragment=new CustomerServiceFragment();
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

    private String getRealPathFromURI(Uri uri) {
        String path = "";

        if (Objects.requireNonNull(getActivity()).getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
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


    private void findviewid(View view) {
        img_upload_icon=view.findViewById(R.id.img_upload_icon);
        paid_upload_img_reg=view.findViewById(R.id.paid_upload_img_reg);
        paid_image_upload=view.findViewById(R.id.paid_image_upload);
        paid_work_rec=view.findViewById(R.id.paid_work_rec);
        paid_service_today_date=view.findViewById(R.id.paid_service_today_date);
        customer_paid_cancel=view.findViewById(R.id.customer_paid_cancel);
        customer_paid_submit=view.findViewById(R.id.customer_paid_submit);
        cus_paid_inv=view.findViewById(R.id.cus_paid_inv);
        paid_service_issue=view.findViewById(R.id.paid_service_issue);
        paid_load_ly=view.findViewById(R.id.paid_load_ly);
        loader_img=view.findViewById(R.id.loader_img);
       // request_ticket=view.findViewById(R.id.ticket_id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomerDashBoard)getActivity()).paidserviceMethod();
        session=new SessionManager(getActivity());
        sharpre=this.getActivity().getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor=sharpre.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paid, container, false);
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
      //  warranty_ticket_id.setText(prefix+counter);
        value=prefix+counter;
        return value;
    }
    public void loadfragment(Fragment fragment){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static class GetFilePathFromDevice {
        public static String getPath(final Context context, final Uri uri) {
            final boolean isKitKat = Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
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
