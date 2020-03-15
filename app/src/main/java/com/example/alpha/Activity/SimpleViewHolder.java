package com.example.alpha.Activity;

import android.view.View;
import android.widget.TextView;

import com.example.alpha.R;

import androidx.recyclerview.widget.RecyclerView;

public class SimpleViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);
    }
}
