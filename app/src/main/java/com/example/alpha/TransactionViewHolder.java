package com.example.alpha;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public TextView transactionAmount,transactiondate,transactiontime,transaction_status,txnid;



    private ItemClickListener itemClickListener;
    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        transactionAmount=(TextView)itemView.findViewById(R.id.trans_amount);
        transactiondate=(TextView)itemView.findViewById(R.id.trans_date);
        transactiontime=(TextView)itemView.findViewById(R.id.trans_time);
        transaction_status=(TextView)itemView.findViewById(R.id.trans_status);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
