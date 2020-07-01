package com.example.alpha.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alpha.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class TransactionView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView transactionType, transactionDate, transactionTime, transactionStatus, transferredTo, transactionId, transactionAmount, transactionName;

    public TextView filterText;
    public RadioButton filterRadio;
    public ItemClickListener itemClickListener;
    public LinearLayout transactionLayout, filterLayout;
    public ImageView transactionImage, walletImage;
    public RelativeLayout walletAddImage;

    public TransactionView(@NonNull View itemView) {
        super(itemView);
        transactionDate = itemView.findViewById(R.id.transactionDate);
        transactionTime = itemView.findViewById(R.id.transactionTime);
        transactionName = itemView.findViewById(R.id.transactionName);
        transactionType = itemView.findViewById(R.id.transactionType);
        transactionStatus = itemView.findViewById(R.id.transactionStatus);


        filterLayout = itemView.findViewById(R.id.filterLayout);
        filterText = itemView.findViewById(R.id.filterText);
        filterRadio = itemView.findViewById(R.id.filterRadio);

        transactionAmount = itemView.findViewById(R.id.transactionAmount);
        transactionImage = itemView.findViewById(R.id.transactionImage);
        transactionLayout = itemView.findViewById(R.id.transactionLayout);
        walletAddImage = itemView.findViewById(R.id.walletAddImage);
        walletImage = itemView.findViewById(R.id.walletImage);



        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    public void setDetails(Context applicationContext, String subjectName) {
    }
}
