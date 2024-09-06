package com.example.campusexpensemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.AddExpenseActivity;
import com.example.campusexpensemanager.DatabaseSQLite.DatabaseHelper;
import com.example.campusexpensemanager.Models.Categories;
import com.example.campusexpensemanager.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Categories> expenseList;
    private DatabaseHelper dbHelper;
    private Context context;

    // Constructor to pass the expense list
    public ExpenseAdapter(List<Categories> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);

    }
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each expense item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        // Bind the data to each item view
        Categories expense = expenseList.get(position);
        holder.tvName.setText(expense.getCategory_name());
        holder.tvBudget.setText(String.format("Budget: %.2f", expense.getCategory_budget()));
        holder.tvRemaining.setText(String.format("Remaining: %.2f", expense.getRemaining_budget()));
        holder.btnDelete1Expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteExpense(expense.getCategory_id());

                expenseList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, expenseList.size());

                // Notify AddExpenseActivity to refresh budget displays
                if (context instanceof AddExpenseActivity) {
                    ((AddExpenseActivity) context).refreshBudgetDisplay();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ViewHolder class to hold the views for each item
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBudget, tvRemaining;
        ImageButton btnDelete1Expense;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvBudget = itemView.findViewById(R.id.tvBudget);
            tvRemaining = itemView.findViewById(R.id.tvRemaining);
            btnDelete1Expense = itemView.findViewById(R.id.btnDelete1Expense);
        }
    }

}
