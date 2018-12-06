package com.example.vipha.finalproject_cst2335;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class creates the database
 * Class member: TABLE_NAME, DAATABASE_NAME, TABLE_NAME_FAVORITE, VERSION_NUMBER, KEY_ID, KEY_NAME, KEY_FOOD_CALS
 */
public class FoodDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME ="Food_Database";
    public static final String TABLE_NAME_FAVORITE ="Food_Database_Favorite";
    public static final String DATABASE_NAME ="MyDatabase";
    public static final int VERSION_NUMBER =4;
    public static final String KEY_ID ="_id";
    public static final String KEY_FOOD_NAME ="_name";
    public static final String KEY_FOOD_CALS = "calories";

    public FoodDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME, null, VERSION_NUMBER);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_FOOD_NAME+" Text, " + KEY_FOOD_CALS + ", double); ");
       // db.execSQL("CREATE TABLE " + TABLE_NAME_FAVORITE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_FOOD_NAME+" Text, " + KEY_FOOD_CALS + ", double); ");
        // Log.i(ChatDatabaseHelper, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITE);
        onCreate(db);
        // Log.i(ChatDatabaseHelper, "Calling onUpgrade, oldVersion=" + oldVer + "newVersion" + newVer);
    }
}
