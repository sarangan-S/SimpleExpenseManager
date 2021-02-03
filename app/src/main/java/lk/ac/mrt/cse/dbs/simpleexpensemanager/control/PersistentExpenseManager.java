package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;





public class PersistentExpenseManager extends ExpenseManager{


    Context context;
    public PersistentExpenseManager(Context ctx)  {
        context=ctx;
        try {

            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }
    @Override

    public void setup() throws ExpenseManagerException {

        SQLiteDatabase db = context.openOrCreateDatabase("180568V", context.MODE_PRIVATE, null);

        // create the databases.
        db.execSQL("CREATE TABLE IF NOT EXISTS Account( accountNo VARCHAR PRIMARY KEY,bankName VARCHAR,accountHolderName VARCHAR,balance REAL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Account_Transaction(Transaction_id INTEGER PRIMARY KEY,accountNo VARCHAR,expenseType INT,amount REAL,date DATE,FOREIGN KEY (accountNo) REFERENCES Account(accountNo));");
        PersistentMemoryAccountDAO accountDAO = new PersistentMemoryAccountDAO(db);
        PersistentMemoryTransactionDAO transactionDAO = new PersistentMemoryTransactionDAO(db);
        setAccountsDAO(accountDAO);

        setTransactionsDAO(transactionDAO);

        /*** End ***/
    }
}
