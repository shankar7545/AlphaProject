package com.example.alpha.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alpha.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class TransactionView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView transactionType,transactionDate ,transactionTime ,transactionStatus ,transferredTo ,transactionId ,transactionAmount ,transactionName;

    public ItemClickListener itemClickListener;
    public LinearLayout transactionLayout;
    public ImageView transactionImage;

    public TransactionView(@NonNull View itemView) {
        super(itemView);
        transactionDate=(TextView)itemView.findViewById(R.id.transactionDate);
        transactionTime = (TextView)itemView.findViewById(R.id.transactionTime);
        transactionName = (TextView)itemView.findViewById(R.id.transactionName);
        transactionType = (TextView)itemView.findViewById(R.id.transactionType);
        transactionStatus = (TextView)itemView.findViewById(R.id.transactionStatus);


        transactionAmount = (TextView)itemView.findViewById(R.id.transactionAmount);
        transactionImage = (ImageView) itemView.findViewById(R.id.transactionImage);
        transactionLayout=(LinearLayout)itemView.findViewById(R.id.transactionLayout);


        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    public void setDetails(Context applicationContext, String subjectName) {
    }
}
