package com.example.alpha.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alpha.R;

import androidx.fragment.app.Fragment;


public class EarnFragment extends Fragment {


    public EarnFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_earn, container, false);
    }

}
