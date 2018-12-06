package com.example.vipha.finalproject_cst2335;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class is used to create the database table that stores the attributes for the
 * Saved Articles
 */

public class NewsDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Articles.db";
    public static final int VERSION_NUMBER =2;
    public static final String TABLE_NAME ="Articles";
    public static final String KEY_ID ="ID";
    public static final String KEY_TITLE ="Title";
    public static final String KEY_LINK ="Link";
    public static final String KEY_AUTHOR ="Author";
    public static final String KEY_CATEGORY ="Category";
    public static final String KEY_DESCRIPTION ="Description";
    public static final String KEY_DATE= "Date";


    public NewsDatabaseHelper(Context ctx){
    super(ctx, DATABASE_NAME, null, VERSION_NUMBER);
    }

    public void onCreate(SQLiteDatabase db)
    {
        Log.i("NewsDatabaseHelper", "Calling onCreate");
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TITLE + " text, "
                +KEY_LINK + " text, "
                +KEY_AUTHOR+" text, "
                +KEY_CATEGORY+" text, "
                +KEY_DESCRIPTION+" text, "
                +KEY_DATE+" text "+
                ");");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
    {
        Log.i("NewsDatabaseHelper", "Calling onUpgrade, oldVersion= " + oldVer + ", newVersion= " + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME ); //Drops the table
        onCreate(db);  //creates new database
    }

    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer)
    {
        Log.i("NewsDatabaseHelper", "Calling onDowngrade, oldVersion= " + oldVer + ", newVersion= " + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME ); //Drops the table
        onCreate(db);  //creates new database
    }
}
