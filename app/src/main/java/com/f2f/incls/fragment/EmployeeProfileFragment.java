package com.f2f.incls.fragment;

import android.Manifest;
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
import pl.droidsonroids.gif.GifImageView;

import android.os.Environment;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.activity.EmployeeDashboard;
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
import org.apache.http.entity.mime.content.FileBody;
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
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;
import static com.f2f.incls.fragment.CustomerProfileFragment.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.f2f.incls.utilitty.SessionManager.KEY_EMAIL;
import static com.f2f.incls.utilitty.SessionManager.KEY_MOBILE;
import static com.f2f.incls.utilitty.SessionManager.KEY_PASSWORD;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_NAME;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_PROFILE;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class EmployeeProfileFragment extends Fragment implements LoadinInterface {
    ImageView camera_logo,emp_profile;
    LinearLayout emp_profile_ly;
    Button emp_profile_save;
    EditText emp_name,emp_email,emp_mobile,emp_pass;
    LinearLayout details_ly;
    GifImageView loader_img;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 100, SELECT_FILE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 0;
    String imagePath,store_path;
     String id;

    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
     Fragment fragment;
    private Object container;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity());
        sharpre = this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharpre.edit();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviewid(view);
        ((EmployeeDashboard)getActivity()).adsimageget();
      /*  emp_profile_ly.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public void onClick(View v) {
                ((EmployeeDashboard)getActivity()).hideKeyboard(v);
            }
        });*/

       HashMap<String, String> user = session.getempdetails();
       String name = user.get(KEY_USER_NAME);
        id = user.get(KEY_USER_ID);
       String email = user.get(KEY_EMAIL);
       String mobile = user.get(KEY_MOBILE);
       String image = user.get(KEY_USER_PROFILE);
       String pass = user.get(KEY_PASSWORD);
        Log.d("Success","name_test::"+name);
        Log.d("Success","profile_mobile_test::"+mobile);
        Log.d("Success","profile_test_pass::"+pass);
      // showProgress();
            if (image.isEmpty()){
                emp_profile.setImageResource(R.drawable.ic_user_def);
            }else {
                Glide.with(getActivity())
                        .load(image)
                        .into(emp_profile);
                hideProgress();
            }

        emp_name.setText(name);
        emp_email.setText(email);
        emp_mobile.setText(mobile);
        emp_pass.setText(pass);

        emp_profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=emp_name.getText().toString();
                String email=emp_email.getText().toString();
                String mobile=emp_mobile.getText().toString();
                String pass=emp_pass.getText().toString();

                if (name.isEmpty() || email.isEmpty() ||!(isValidMail(email)) || mobile.isEmpty() || pass.isEmpty()) {
                    if (name.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter name",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (email.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter email",
                                Toast.LENGTH_SHORT).show();
                    }else if (!(isValidMail(email))){
                        Toast.makeText(getActivity(), "Please enter valid email",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (mobile.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter mobile number",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (pass.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter password",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("emp_id", id);
                        object.put("username", name);
                        object.put("email_id", email);
                        object.put("mobile_number", mobile);
                        object.put("password", pass);
                        Log.d("Success", "Request_object::" + object);
                        sendbackground(Constants.EMP_PROFILE_UPDATE, object);
                        showProgress();
                        String ids = "1";
                     //   profile(id, imagePath);
                        Log.d("Success", "Test_image_path::" + imagePath);

                    } catch (Exception e) {

                    }
                }
            }
        });

        camera_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });
    }

    private boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_STRING).matcher(email).matches();
    }

    private void sendbackground(String url, JSONObject object) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, object, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {

            }

            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                Log.d("Suucess","Emp_profile_update_responce::"+response);
                if (response.getString("status").equalsIgnoreCase("success")){
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        String id=obj.getString("id");
                        String name=obj.getString("username");
                        String email=obj.getString("email_id");
                        String mobile=obj.getString("mobile_no");
                        String pass = emp_pass.getText().toString();

                        Log.d("Success", "mobile::" + mobile);
                        Log.d("Success", "username::" + name);
                        editor.remove(KEY_USER_ID);
                        editor.remove(KEY_USER_NAME);
                        editor.remove(KEY_EMAIL);
                        editor.remove(KEY_MOBILE);
                        editor.remove(KEY_PASSWORD);
                        editor.putString(KEY_USER_ID,id);
                        editor.putString(KEY_USER_NAME,name);
                        editor.putString(KEY_EMAIL,email);
                        editor.putString(KEY_MOBILE,mobile);
                        editor.putString(KEY_PASSWORD,pass);
                      /*  session.createempLoginSession(id,name,"",pass,mobile
                                ,email,"","","","","");*/
                        editor.commit();

                        fragment=new EmployeeProfileFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container_emp, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
                return null;
            }
        });
    }

    private void selectimage() {
            final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

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
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            emp_profile.setImageBitmap(bitmap);

            sharpre.edit();
            editor.remove(KEY_USER_PROFILE);
            editor.putString("user_profile",imagePath);
            editor.commit();

            try {
                File sdcard = Environment.getExternalStorageDirectory();
                String targetPath = sdcard.getAbsolutePath() + File.separator + "INS" + File.separator + "Images" + File.separator;
                File folder = new File(targetPath);
                folder.mkdirs();

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                String imageFileName = id + "_" + randomUUIDString + ".jpg";
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
                    new UploadFileToServer(imagePath).execute();
                }
            } catch(Exception e){
                Log.e("success", "Exception:: " + e);
            }
        }

        if (requestCode==SELECT_FILE && resultCode==RESULT_OK) {
            imagePath = CustomerPaidFragment.GetFilePathFromDevice.getPath(getActivity(), data.getData());
            Log.d("Success", "selected_uri_gallery::" + imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            emp_profile.setImageBitmap(bitmap);
            store_path=imagePath;

            sharpre.edit();
            editor.remove(KEY_USER_PROFILE);
            editor.putString("user_profile",store_path);
            editor.commit();

            try {
                File sdcard = Environment.getExternalStorageDirectory();
                String targetPath = sdcard.getAbsolutePath() + File.separator + "INS" + File.separator + "Images" + File.separator;
                File folder = new File(targetPath);
                folder.mkdirs();

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                String imageFileName = id + "_" + randomUUIDString + ".jpg";
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
                    imagePath= destination_file_name;
                    new UploadFileToServer(imagePath).execute();
                }
            } catch(Exception e){
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

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    cameraIntent();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void findviewid(View view) {
        camera_logo=view.findViewById(R.id.camera_logo);
        emp_profile=view.findViewById(R.id.emp_profile);
        emp_name=view.findViewById(R.id.emp_name);
        emp_email=view.findViewById(R.id.emp_email);
        emp_mobile=view.findViewById(R.id.emp_mobile);
        emp_pass=view.findViewById(R.id.emp_pass);
        emp_profile_save=view.findViewById(R.id.emp_profile_save);
        details_ly=view.findViewById(R.id.details_ly);
        loader_img=view.findViewById(R.id.loader_img);
       // emp_profile_ly=view.findViewById(R.id.emp_profile_ly);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container , Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employee_profile, (ViewGroup) container, false);
    }

    @Override
    public void showProgress() {
        loader_img.setVisibility(View.VISIBLE);
        details_ly.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        loader_img.setVisibility(View.GONE);
        details_ly.setVisibility(View.VISIBLE);
    }


    public class UploadFileToServer extends AsyncTask<String, Integer, String> {
        long totalSize=0;
        boolean isAudio=false;
        String filePathurl;
        ProgressDialog progressDialog;

        private UploadFileToServer(String filePath) {
            this.filePathurl=filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Bundle bundle=getArguments();
            String path= null;
            if (bundle != null) {
                path = bundle.getString("user_profile");
                editor.putString("user_profile",path);
                editor.commit();
            }
            Log.d("Success","path_test_post::"+path);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Log.d("succes","updatng::: "+String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String... params) {
            return uploadFile(filePathurl);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String filePath) {
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
            String url = Constants.EMP_PROFILE_PIC;
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

                File sourceFile = new File(filePath);

                entity.addPart("name", new FileBody(sourceFile));

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
         //   dialog.dismiss();
            Log.e("seuccess", "Response from server: " + result);
            super.onPostExecute(result);
        }
    }

}


