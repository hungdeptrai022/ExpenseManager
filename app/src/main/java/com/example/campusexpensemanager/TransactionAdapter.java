package com.example.campusexpensemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Models.Transaction;
import com.example.campusexpensemanager.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactionList;

    // Constructor to pass the list of transactions
    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each transaction item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        // Bind the data to each item view
        Transaction transaction = transactionList.get(position);
        holder.tvExpenseName.setText(transaction.getExpenseName());
        holder.tvTransactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.tvTransactionDetail.setText(transaction.getDetail());
        holder.tvTransactionDate.setText(transaction.getDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    // ViewHolder class to hold the views for each transaction item
    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvExpenseName, tvTransactionAmount, tvTransactionDetail, tvTransactionDate;


        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpenseName = itemView.findViewById(R.id.tvExpenseName);
            tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvTransactionDetail = itemView.findViewById(R.id.tvTransactionDetail);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
        }
    }
}
