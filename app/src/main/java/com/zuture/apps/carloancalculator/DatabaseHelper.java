package com.zuture.apps.carloancalculator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DB_NAME="CalculatorDb";
    public static final int DB_VERSION=1;


    public DatabaseHelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String query=("CREATE TABLE LOANDETAILS(_id INTEGER PRIMARY KEY AUTOINCREMENT ,"+"NAME TEXT,"+"PRINCIPAL,"+"MONTHLYPAYMENT NUMERIC,"+"INTERESTRATE NUMERIC,"+"ANNUALINTEREST NUMERIC,"+"MONTHS NUMERIC);");
        sqLiteDatabase.execSQL(query);

        String SQL_CREATE_ALARM_TABLE =  "CREATE TABLE " + AlarmReminderContract.AlarmReminderEntry.TABLE_NAME + " ("
                + AlarmReminderContract.AlarmReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AlarmReminderContract.AlarmReminderEntry.KEY_TITLE + " TEXT, "
                + AlarmReminderContract.AlarmReminderEntry.KEY_DATE + " TEXT, "
                + AlarmReminderContract.AlarmReminderEntry.KEY_TIME + " TEXT , "
                + AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT + " TEXT , "
                + AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO + " TEXT , "
                + AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE + " TEXT , "
                + AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE + " TEXT NOT NULL , "
                + AlarmReminderContract.AlarmReminderEntry.KEY_SENT_ID + " INTEGER " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_ALARM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor readAllLoanDetails(){
        String query= "SELECT * FROM LOANDETAILS";
        SQLiteDatabase sqLiteDatabase= this.getReadableDatabase();

        Cursor cursor= null;
        if (sqLiteDatabase!= null){
            cursor=  sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }
    public Cursor readAllReminderetails(){
        String query= "SELECT * FROM  vehicles ";
        SQLiteDatabase sqLiteDatabase= this.getReadableDatabase();

        Cursor cursor= null;
        if (sqLiteDatabase!= null){
            cursor=  sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }

}
