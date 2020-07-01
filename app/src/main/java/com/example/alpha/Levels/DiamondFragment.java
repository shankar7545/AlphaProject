package com.example.alpha.Levels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alpha.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiamondFragment extends Fragment {

    public DiamondFragment() {
        // Required empty public constructor
    }

    private View mView;

    public static Fragment newInstance() {
        Bundle args = new Bundle();

        DiamondFragment fragment = new DiamondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_diamond, container, false);


        return mView;

    }
}
