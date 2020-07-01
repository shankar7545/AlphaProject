package com.example.alpha.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.alpha.Course.CourseActivity;
import com.example.alpha.R;

import androidx.fragment.app.Fragment;


public class EarnFragment extends Fragment {

    LinearLayout androidLayout;
    View mView;
    public EarnFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_earn, container, false);


        onCLick();
        return mView;
    }


    private void onCLick() {
        androidLayout = mView.findViewById(R.id.androidcard);

        androidLayout.setOnClickListener(v -> {

            startActivity(new Intent(getContext(), CourseActivity.class));

        });
    }
}
