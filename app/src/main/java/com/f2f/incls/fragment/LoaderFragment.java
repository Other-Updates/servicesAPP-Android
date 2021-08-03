package com.f2f.incls.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2f.incls.R;

public class LoaderFragment extends Fragment {
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_loader, container, false);
    }
    public void showloader(){
        view.findViewById(R.id.frame_container).setVisibility(View.GONE);
        view.findViewById(R.id.loader_ly).setVisibility(View.VISIBLE);
    }
    public void hideloader(){
        view.findViewById(R.id.frame_container).setVisibility(View.VISIBLE);
        view.findViewById(R.id.loader_ly).setVisibility(View.GONE);
    }
}
