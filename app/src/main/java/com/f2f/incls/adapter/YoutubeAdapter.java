package com.f2f.incls.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.model.YoutubeModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class YoutubeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<YoutubeModel> youtubeList;
    public YoutubeAdapter(Context context, ArrayList<YoutubeModel> youtubeList) {
        this.youtubeList = youtubeList;
        this.context=context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      myviewHolder viewholder;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_layout,parent,false);
     viewholder= new myviewHolder(view);
        return viewholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        myviewHolder viewholder=(myviewHolder)holder;
        YoutubeModel model= youtubeList.get(position);
        final String link=model.getLink();
        final String description=model.getDescription();
       // viewholder.youtube_title.setText(link);

        try
        {
           String videoId=extractYoutubeId(link);
            String img_url="http://img.youtube.com/vi/"+videoId+"/0.jpg";
            Log.d("Success","thumbnail::"+img_url);
            Glide.with(context)
                    .load(img_url)
                    .fitCenter()
                    .into(viewholder.thumbnail_img);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        viewholder.thumbnail_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Success","youtube_link_click::");
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(link));
                context.startActivity(intent);

            }
        });
      /*  viewholder.youtube_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Success","youtube_link_click::");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                context.startActivity(intent);

            }
        });*/

        viewholder.description.setText(description);

    }

    public String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }


    @Override
    public int getItemCount()
    {
        Log.d("Success","youtube_size::"+youtubeList.size());
        return youtubeList.size();
    }

    private static class myviewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail_img;
        TextView youtube_title,description;
        public myviewHolder(View view) {
            super(view);
            thumbnail_img=view.findViewById(R.id.thumnail_img);
        //    youtube_title=view.findViewById(R.id.youtube_title);
            description=view.findViewById(R.id.youtube_description);

        }
    }


}
