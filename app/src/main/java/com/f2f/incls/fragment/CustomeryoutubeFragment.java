package com.f2f.incls.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.f2f.incls.adapter.YoutubeAdapter;
import com.f2f.incls.model.YoutubeModel;
import com.f2f.incls.utilitty.Constants;
import com.f2f.incls.utilitty.LoadinInterface;
import com.f2f.incls.utilitty.SessionManager;
import com.f2f.incls.utilitty.VolleyCallback;
import com.f2f.incls.utilitty.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

import static com.f2f.incls.utilitty.SessionManager.KEY_DESCRIPTION;
import static com.f2f.incls.utilitty.SessionManager.KEY_LINKDATA;
import static com.f2f.incls.utilitty.SessionManager.KEY_USER_ID;
import static com.f2f.incls.utilitty.SessionManager.KEY_YOUTUBE_ID;
import static com.f2f.incls.utilitty.SessionManager.PREF_NAME;

public class CustomeryoutubeFragment extends Fragment implements LoadinInterface {
    SessionManager session;
    SharedPreferences sharpre;
    SharedPreferences.Editor editor;
    GifImageView loader_img;
    /*  ImageView thumbnail_img;
      TextView youtube_title;*/
    RecyclerView youtube_rec;
    String link,description;
    String cus_id=null;
    ArrayList<YoutubeModel>youtubeList=new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findview(view);
        ((CustomerDashBoard) Objects.requireNonNull(getActivity())).adsimageget();
        HashMap<String,String> user=session.getcustomerdetails();
        cus_id =user.get(KEY_USER_ID);


        //Test line

     /*   YoutubeModel model=new YoutubeModel();
        model.setLink("https://www.youtube.com/watch?v=NtMvVz9EfmA");
        youtubeList.add(model);*/

        LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
        youtube_rec.setLayoutManager(lrt);
        YoutubeAdapter adapter=new YoutubeAdapter(getActivity(),youtubeList);
        youtube_rec.setAdapter(adapter);


         linkget();
    }
    private void linkget() {
        JSONObject object=new JSONObject();
        showProgress();
        run(Constants.YOUTUBE);
        Log.d("Success","youtube_link_request::"+object);
    }
    private void run(String url) {
        VolleyUtils.makeJsonObjectRequest(getActivity(), url, null, new VolleyCallback() {
            @Override
            public void onError(String message, VolleyError error) {
            }
            @Override
            public String onResponse(JSONObject response) throws JSONException {
                hideProgress();
                Log.d("Success", "youtube_link_response::" + response);
                if (response.getString("status").equals("success")) {
                    JSONArray array = response.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        JSONArray array1 = obj.getJSONArray("link_datas");
                        for (int j=0;j<array1.length();j++){
                            JSONObject obj1=array1.getJSONObject(j);
                            YoutubeModel model=new YoutubeModel();
                            model.setLink(obj1.getString("link_data"));
                            link=obj1.getString("link_data");
                            model.setDescription(obj1.getString("description"));
                            description=obj1.getString("description");




                            youtubeList.add(model);

                        }
                    }
                    LinearLayoutManager lrt=new LinearLayoutManager(getActivity());
                    youtube_rec.setLayoutManager(lrt);
                    YoutubeAdapter adapter=new YoutubeAdapter(getActivity(),youtubeList);
                    youtube_rec.setAdapter(adapter);
                }
                return link;
            }
        });
    }

    private void findview(View view) {
        loader_img=view.findViewById(R.id.loader_img);
        youtube_rec=view.findViewById(R.id.youtube_rec);

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session=new SessionManager(Objects.requireNonNull(getActivity()));
        sharpre=this.getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor=sharpre.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_customer_apps, container, false);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //     mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}


