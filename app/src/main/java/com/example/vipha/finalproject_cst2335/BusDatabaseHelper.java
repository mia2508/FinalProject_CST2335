package com.example.vipha.finalproject_cst2335;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class BusDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDatabase";
    public static final int VERSION_NUM = 8;
    public static final String TABLE_NAME = "Bus_Number";
    public static final String KEY_ID = "ID"; //column name, primary key
    public static final String KEY_MESSAGE = "MessageCol";


    public BusDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT);";
        db.execSQL(CREATE_TABLE);
        Log.i("BusDatabaseHelper","Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
        Log.i("BusDatabaseHelper", "Calling onUpgrade,oldVersion " + oldVersion + " newVersion =" + newVersion);
    }


}

