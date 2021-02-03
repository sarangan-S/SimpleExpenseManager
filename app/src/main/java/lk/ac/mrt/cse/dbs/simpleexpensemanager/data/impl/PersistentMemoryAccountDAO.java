package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.annotation.TargetApi;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentMemoryAccountDAO implements AccountDAO {


    SQLiteDatabase db;

   public PersistentMemoryAccountDAO(SQLiteDatabase db){
       this.db = db;
   }

    //add account
    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account ( accountNo,bankName,accountHolderName,balance) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);



        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());


        statement.executeInsert();
    }


    //get account numbers
    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();

        String accountNumbersSelectQuery = "SELECT accountNo FROM Account";


        Cursor rawQuery = db.rawQuery(accountNumbersSelectQuery, null);
        try {
            if (rawQuery.moveToFirst()) {
                do {

                    String test=rawQuery.getString(rawQuery
                            .getColumnIndex("accountNo"));

                    accountNumbers.add(test);

                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
        }

        return accountNumbers;
    }

    @Override

    //fetch all account details
    public List<Account> getAccountsList() {
        List<Account> accountdetail = new ArrayList<>();

        String accountDetailSelectQuery = "SELECT * FROM Account";


        Cursor rawQuery = db.rawQuery(accountDetailSelectQuery, null);

        try {
            if (rawQuery.moveToFirst()) {
                do {
                    Account acc=new Account(
                            rawQuery.getString(rawQuery.getColumnIndex("accountNo")),
                            rawQuery.getString(rawQuery.getColumnIndex("bankName")),
                            rawQuery.getString(rawQuery.getColumnIndex("accountHolderName")),
                            rawQuery.getDouble(rawQuery.getColumnIndex("balance")));




                    accountdetail.add(acc);

                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
        }

        return accountdetail;

    }

    //get account details for a particular account number
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String accountSelectQuery = "SELECT * FROM Account where accountNo = "+accountNo;

        Cursor rawQuery = db.rawQuery(accountSelectQuery, null);
        if (rawQuery != null)
            rawQuery.moveToFirst();

        Account td = new Account(
                rawQuery.getString(rawQuery.getColumnIndex("accountNo")),
                rawQuery.getString(rawQuery.getColumnIndex("bankName")),
                rawQuery.getString(rawQuery.getColumnIndex("accountHolderName")),
                rawQuery.getDouble(rawQuery.getColumnIndex("balance")));


        return td;

    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        String sqlquery = "DELETE FROM Account WHERE accountNo = ?";
        SQLiteStatement statement = db.compileStatement(sqlquery);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void updateBalance(String accountNo, ExpenseType expense_Type, double _amount) throws InvalidAccountException {

        String sqlquery = "UPDATE Account SET balance = balance + ?";
        SQLiteStatement statement = db.compileStatement(sqlquery);
        if(expense_Type == ExpenseType.EXPENSE){
            statement.bindDouble(1,-_amount);
        }else{
            statement.bindDouble(1,_amount);
        }

        statement.executeUpdateDelete();
    }
}

