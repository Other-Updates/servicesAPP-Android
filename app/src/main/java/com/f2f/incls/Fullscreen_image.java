package com.f2f.incls;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.f2f.incls.adapter.WorkperformedAdapter;

public class Fullscreen_image extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);
/*
        Bundle b = getIntent().getExtras();

        ImageView imageview=(ImageView) findViewById(R.id.full_image);
        int receivingdata = b.getInt("image");
        imageview.setImageResource(receivingdata);
        Intent intent = getIntent();*/
       // String message = intent.getStringExtra(WorkperformedAdapter."image");
      //  int position = Integer.parseInt(message);
      //  ImageView imageView = (ImageView) viewItefindViewById(R.id.imageView);
      //  imageview.setImageResource(image_ids[position]);


    }

    /*private void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.customer_workperformed);
        dialog.setTitle("Dialog Box");
        TextView text = (TextView) dialog.findViewById(R.id.text);
       text.setText("Android custom dialog example!");
        Button button = (Button) dialog.findViewById(R.id.workpicture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();*/
    }

