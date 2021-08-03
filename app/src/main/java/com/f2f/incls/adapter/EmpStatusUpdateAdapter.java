package com.f2f.incls.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.f2f.incls.R;
import com.f2f.incls.model.EmpSpinnerModel;
import com.f2f.incls.model.EmpStatusUpdateModel;
import com.f2f.incls.model.EmpUploadImgModel;
import com.f2f.incls.utilitty.CustomerUploadModel;
import com.f2f.incls.utilitty.VolleyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class EmpStatusUpdateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<EmpStatusUpdateModel> statusList;
    String[] testList;
    String[] task_status = {"Pending", "Inprogress", "Complete"};
    String userChoosenTask;
    String imagePath,store_path,id,click_pos;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 0;
    private static final int PERMISSION_REQUEST_CODE = 200;


    public EmpStatusUpdateAdapter(Context context, List<EmpStatusUpdateModel> statusList, String[] testList) {
        Log.d("Success","constructore_test::"+testList.length);
        this.context=context;
        this.statusList=statusList;
        this.testList=testList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyVIewHolder vIewHolder;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_status_update_view,parent,false);
        vIewHolder=new MyVIewHolder(view);
        return vIewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EmpStatusUpdateModel model=statusList.get(position);
        MyVIewHolder vIewHolder=(MyVIewHolder)holder;

        String date=model.getDate();
        Log.d("Success","Employee_update_status::"+date);
        StringBuilder builder=new StringBuilder();
        StringTokenizer tk = new StringTokenizer(date);
        String da = tk.nextToken();  // <---  yyyy-mm-dd
        String time = tk.nextToken();

        String[] split=da.split("-");
        String y=split[0];
        String m=split[1];
        String d=split[2];
        Log.d("Success","date_test_year::"+y);
        Log.d("Success","date_test_month::"+m);
        Log.d("Success","date_test_day::"+d);

        builder.append(d).append("/").append(m).append("/").append(y).toString();
        String date1=new String(builder);


        vIewHolder.attender_id.setText(model.getAttenter_name());
        vIewHolder.task_date.setText(date1);
        vIewHolder.status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pos = task_status[position];
                Log.d("Success","recyclerview_pos::"+pos);
                if (pos.equalsIgnoreCase("pending")) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#fd0606"));
                }
                if (pos.equalsIgnoreCase("Inprogress")) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#F3DC02"));
                }
                if (pos.equalsIgnoreCase("Complete")) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#06801a"));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /* vIewHolder.photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });*/
    }
    @Override
    public int getItemCount() {
        return statusList.size();
    }

    private class MyVIewHolder extends RecyclerView.ViewHolder {
        TextView attender_id,task_date;
        ImageView photo_upload;
        Spinner status_spinner;

        public MyVIewHolder(View view) {
            super(view);
            attender_id=view.findViewById(R.id.attender_id);
            task_date=view.findViewById(R.id.task_date);
            status_spinner=view.findViewById(R.id.status_spinner);
            photo_upload=view.findViewById(R.id.photo_upload);
            Log.d("Success","recyclerview_pos_outside::");
            ArrayAdapter adapter=new ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,testList);
            status_spinner.setAdapter(adapter);

            photo_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPictureDialog();
                }
            });

        }
    }

    private void showPictureDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";

                        galleryIntent();

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
        if (checkPermission()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Log.d("Success","camera_test::");
            startActivityForResult(intent, REQUEST_CAMERA);
        } else {
            requestPermission();
        }
    }


    private void startActivityForResult(Intent data, int request) {
        Log.d("Success", "picture_test_1::" + data);
        Log.d("Success", "picture_test_2::" + request);
        Uri selectedImageUri = null;

        if (request == 0) {

            selectedImageUri = data.getData();
            Log.d("Success", "camera_image_uri::" + selectedImageUri);
            imagePath = getRealPathFromURI(selectedImageUri);
            Log.d("Success", "gallery_image_path::" + imagePath);
        }else {
            Log.d("Success", "camera_image_uri::" + data.getData());
            selectedImageUri = data.getData();
            Log.d("Success", "gallery_image_uri::" + selectedImageUri);
            imagePath = getRealPathFromURI(selectedImageUri);
            Log.d("Success", "gallery_image_path::" + imagePath);
        }
        }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        Log.d("Success","request_code_main::"+requestCode);
        switch (requestCode) {

            case PICK_IMAGE:
                Log.d("Success","request_code_test_gallery::"+requestCode);
                if (resultCode == RESULT_OK) {
                    selectedImageUri = data.getData();
                    imagePath = getRealPathFromURI(selectedImageUri);

                    if (click_pos.equalsIgnoreCase("1")) {
                        CustomerUploadModel model=new CustomerUploadModel();
                        model.setImage_path(imagePath);
                        Log.d("Success","gallery_image_path::"+imagePath);
                      /*  uploadArrayList.add(model);
                        Log.d("Success","upload_Array_size::"+uploadArrayList.size());

                        Log.d("Success","click_pos::"+click_pos);
                        LinearLayoutManager lrt = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        upload_img_reg.setLayoutManager(lrt);
                        CustomerUploadImgAdapter adapter = new CustomerUploadImgAdapter(getActivity(), uploadArrayList);
                        upload_img_reg.setAdapter(adapter);*/
                    }else {
                        EmpUploadImgModel model1=new EmpUploadImgModel();
                        model1.setImage_path(imagePath);
                       /* empuploadList.add(model1);
                        Log.d("Success","Emp_upload_Array_size::"+empuploadList.size());

                        LinearLayoutManager lrt=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                        work_per_rec.setLayoutManager(lrt);
                        EmpUploadImgAdapter adapter=new EmpUploadImgAdapter(getActivity(),empuploadList);
                        work_per_rec.setAdapter(adapter);*/
                    }

                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    // image_test.setImageBitmap(bitmap);
                    store_path=imagePath;

                   /* sharpre.edit();
                    editor.remove(KEY_USER_PROFILE);
                    editor.putString("user_profile",store_path);
                    editor.commit();*/

                    Log.d("Success","Store_path::"+store_path);
                   /* editor.putString(KEY_USER_PROFILE,store_path);
                    session.customer_profil(store_path);
                    editor.commit();*/
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
//                            new CustomerProfileFragment.UploadFileToServer(imagePath).execute();
                        }
                    } catch(Exception e){
                        Log.e("success", "Exception:: " + e);

                    }

                }
                break;
            case PICK_Camera_IMAGE:
                Log.d("Success","request_code_test_camera::"+requestCode);
                if (resultCode == RESULT_OK) {
                    //use imageUri here to access the image
                    //    String path= String.valueOf(data.getData());
                    selectedImageUri=data.getData();
                    imagePath = getRealPathFromURI(selectedImageUri);
                    Log.d("Success", "cemera_pick::" + imagePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    // image_test.setImageBitmap(bitmap);
                    store_path=imagePath;


                    if (click_pos.equalsIgnoreCase("1")) {
                        Log.d("Success","click_pos::"+click_pos);
                        CustomerUploadModel model=new CustomerUploadModel();
                        model.setImage_path(imagePath);
                       /* uploadArrayList.add(model);
                        Log.d("Success","upload_Array_size::"+uploadArrayList.size());

                        LinearLayoutManager lrt = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        upload_img_reg.setLayoutManager(lrt);
                        CustomerUploadImgAdapter adapter = new CustomerUploadImgAdapter(getActivity(), uploadArrayList);
                        upload_img_reg.setAdapter(adapter);*/
                    }else {
                        EmpUploadImgModel model1=new EmpUploadImgModel();
                        model1.setImage_path(imagePath);
                        /*empuploadList.add(model1);

                        LinearLayoutManager lrt=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                        work_per_rec.setLayoutManager(lrt);
                        EmpUploadImgAdapter adapter=new EmpUploadImgAdapter(getActivity(),empuploadList);
                        work_per_rec.setAdapter(adapter);*/
                    }
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
//                            new CustomerProfileFragment.UploadFileToServer(imagePath).execute();
                        }
                    } catch(Exception e){
                        Log.e("success", "Exception:: " + e);

                    }

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(context, "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
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

        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
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

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {

        requestPermissions((Activity) context,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);


    }
}
