package com.example.alpha.Activity;

import android.view.View;
import android.widget.TextView;

import com.example.alpha.R;

import de.blox.graphview.ViewHolder;

public class SimpleViewHolder extends ViewHolder {
    public TextView textView;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);
    }
}
