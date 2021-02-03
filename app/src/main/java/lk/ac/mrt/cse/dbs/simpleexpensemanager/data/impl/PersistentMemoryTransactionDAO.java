package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;




public class PersistentMemoryTransactionDAO implements TransactionDAO {

    //transaction table



    SQLiteDatabase db;

    public PersistentMemoryTransactionDAO(SQLiteDatabase db){

      this.db=db;
    }


    @Override
    //insert values into transaction table
    public void logTransaction(Date date_, String accountNo, ExpenseType expenseType_, double amount_){



            String insertQuery = "INSERT INTO Account_Transaction (accountNo,expenseType,amount,date) VALUES (?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(insertQuery);

            statement.bindString(1,accountNo);
            statement.bindLong(2,(expenseType_ == ExpenseType.EXPENSE) ? 0 : 1);
            statement.bindDouble(3,amount_);
            statement.bindLong(4,date_.getTime());

            statement.executeInsert();



    }

    @Override
    //get all transactions
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();

        String transactionDetailSelectQuery = "SELECT * FROM Account_Transaction";
        Cursor rawQuery = db.rawQuery(transactionDetailSelectQuery, null);

        try {
            if (rawQuery.moveToFirst()) {
                do {
                    Transaction trans=new Transaction(
                            new Date(rawQuery.getLong(rawQuery.getColumnIndex("date"))),
                            rawQuery.getString(rawQuery.getColumnIndex("accountNo")),
                            (rawQuery.getInt(rawQuery.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                   rawQuery.getDouble(rawQuery.getColumnIndex("amount")));




                    transactions.add(trans);

                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
        }


        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {


        List<Transaction> transdetail = new ArrayList<>();

        String transDetailSelectQuery = "SELECT * FROM Account_Transaction LIMIT"+limit;


        Cursor rawQuery = db.rawQuery(transDetailSelectQuery, null);


            if (rawQuery.moveToFirst()) {
                do {
                    Transaction trans=new Transaction(
                            new Date(rawQuery.getLong(rawQuery.getColumnIndex("date"))),
                            rawQuery.getString(rawQuery.getColumnIndex("accountNo")),
                            (rawQuery.getInt(rawQuery.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                            rawQuery.getDouble(rawQuery.getColumnIndex("amount")));


                    transdetail.add(trans);

                } while (rawQuery.moveToNext());
            }

        return  transdetail;
    }





}

