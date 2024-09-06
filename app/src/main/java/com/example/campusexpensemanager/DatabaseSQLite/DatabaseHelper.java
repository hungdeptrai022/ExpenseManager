package com.example.campusexpensemanager.DatabaseSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expenseManager.db";
    private static final int DATABASE_VERSION = 4;

    // Table names
    private static final String TABLE_EXPENSE = "expenses";
    private static final String TABLE_TRANSACTION = "transactions";

    // Expense table columns
    private static final String COLUMN_EXPENSE_ID = "id";
    private static final String COLUMN_EXPENSE_NAME = "name";
    private static final String COLUMN_EXPENSE_BUDGET = "budget";
    private static final String COLUMN_EXPENSE_REMAINING = "remaining";

    // Transaction table columns
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_TRANSACTION_EXPENSE_ID = "expense_id";
    private static final String COLUMN_TRANSACTION_EXPENSE_NAME = "expense_name";
    private static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_TRANSACTION_DETAIL = "detail";
    private static final String COLUMN_TRANSACTION_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createExpenseTable = "CREATE TABLE " + TABLE_EXPENSE + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXPENSE_NAME + " TEXT, " +
                COLUMN_EXPENSE_BUDGET + " REAL, " +
                COLUMN_EXPENSE_REMAINING + " REAL" + ")";

        String createTransactionTable = "CREATE TABLE " + TABLE_TRANSACTION + " (" +
                COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRANSACTION_EXPENSE_ID + " INTEGER, " +
                COLUMN_TRANSACTION_EXPENSE_NAME + " TEXT, " +
                COLUMN_TRANSACTION_AMOUNT + " REAL, " +
                COLUMN_TRANSACTION_DETAIL + " TEXT, " +
                COLUMN_TRANSACTION_DATE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_TRANSACTION_EXPENSE_ID + ") REFERENCES " +
                TABLE_EXPENSE + "(" + COLUMN_EXPENSE_ID + "))";

        db.execSQL(createExpenseTable);
        db.execSQL(createTransactionTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(db);
    }

    // Method to add expense
    public boolean addExpense(String name, double budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_NAME, name);
        values.put(COLUMN_EXPENSE_BUDGET, budget);
        values.put(COLUMN_EXPENSE_REMAINING, budget); // Initially, remaining equals budget

        long result = db.insert(TABLE_EXPENSE, null, values);
        return result != -1;
    }

    // Method to add transaction and update budget
    public boolean addTransaction(int expenseId,String transaction_name, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_EXPENSE_ID, expenseId);
        values.put(COLUMN_TRANSACTION_EXPENSE_NAME,transaction_name );
        values.put(COLUMN_TRANSACTION_AMOUNT, amount);
        values.put(COLUMN_TRANSACTION_DATE, new Date().toString());

        long result = db.insert(TABLE_TRANSACTION, null, values);

        if (result != -1) {
            // Update the remaining budget for the related expense
            updateExpenseRemaining(expenseId, amount);
        }

        return result != -1;
    }

    private void updateExpenseRemaining(int expenseId, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_EXPENSE + " SET " +
                COLUMN_EXPENSE_REMAINING + " = " + COLUMN_EXPENSE_REMAINING + " - " + amount +
                " WHERE " + COLUMN_EXPENSE_ID + " = " + expenseId;
        db.execSQL(query);
    }

    // Fetch all expenses
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSE, null);
    }

    // Fetch all transactions for an expense
    public Cursor getTransactionsForExpense(int expenseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_TRANSACTION_EXPENSE_ID + " = " + expenseId, null);
    }

    public List<String> getAllExpenseNames(){
        List<String> expenseNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM expenses", null);

        if (cursor.moveToFirst()) {
            do {
                expenseNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseNames;
    }
    public void addTransaction (String expenseName, double amount, String detail, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM expenses WHERE name = ?", new String[]{expenseName});
        if (cursor.moveToFirst()) {
            int expenseId = cursor.getInt(0);

            ContentValues values = new ContentValues();
            values.put("expense_id", expenseId);
            values.put("amount", amount);
            values.put("detail", detail);
            values.put("date", date);

            db.insert("transactions", null, values);
        }
        cursor.close();
    }
    public void updateExpenseRemainingBudget(String expenseName, double amountSpent) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE expenses SET remaining = remaining - ? WHERE name = ?",
                new String[]{String.valueOf(amountSpent), expenseName});
    }
    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT expenses.name, transactions.amount, transactions.detail, transactions.date " +
                "FROM transactions " +
                "INNER JOIN expenses ON transactions.expense_id = expenses.id";
        return db.rawQuery(query, null);
    }
    // Method to edit an existing expense
    public int editExpense(String expenseName, double newBudget) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Fetch the current amount spent for the given expense
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_EXPENSE_BUDGET + " - " + COLUMN_EXPENSE_REMAINING + " FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_EXPENSE_NAME + " = ?", new String[]{expenseName});
        double amountSpent = 0;
        if (cursor.moveToFirst()) {
            amountSpent = cursor.getDouble(0);
        }
        cursor.close();

        // Calculate the new remaining budget
        double newRemaining = newBudget - amountSpent;

        // Update the budget and remaining values
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_BUDGET, newBudget);
        values.put(COLUMN_EXPENSE_REMAINING, newRemaining);

        // Update the expense in the database
        return db.update(TABLE_EXPENSE, values, COLUMN_EXPENSE_NAME + " = ?", new String[]{expenseName});
    }

    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses", "id = ?", new String[]{String.valueOf(expenseId)});
        db.delete("transactions", "expense_id = ?", new String[]{String.valueOf(expenseId)});
        db.close();
    }

    public double getTotalBudget() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(budget) FROM expenses", null);
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        cursor.close();
        return 0;
    }

    public double getTotalRemaining() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(remaining) FROM expenses", null);
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        cursor.close();
        return 0;
    }
    public double getRemainingBudget(String expenseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        double remainingBudget = 0.0;

        // Truy vấn để lấy số tiền còn lại của expense dựa trên tên
        String query = "SELECT remaining FROM expenses WHERE name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{expenseName});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                remainingBudget = cursor.getDouble(cursor.getColumnIndex("remaining"));
            }
            cursor.close();
        }

        return remainingBudget;
    }


}