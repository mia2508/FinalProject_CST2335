package com.example.vipha.finalproject_cst2335;
/**
 * Author: Vi Pham
 * Class description: This class create database for movie, hold information of the movie
 * Class member: String DATABASE_NAME, INT VERSION_NUM, TABLE_NAME,KEY_ID,KEY_MESSAGE,KEY_TITLE,KEY_YEAR,KEY_RATING,KEY_RUNTIME,KEY_PLOT, KEY_URL_POSTER
 */

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Movie.db";
    public static int VERSION_NUM=3;
    public static final String TABLE_NAME="MovieInformation";
    public static final String KEY_ID="_ID";
    public static final String KEY_TITLE="_TITLE";
    public static final String KEY_YEAR="_YEAR";
    public static final String KEY_RATING="_RATING";
    public static final String KEY_RUNTIME="_RUNTIME";
    public static final String KEY_MAINACTORS="_MAINACTORS";
    public static final String KEY_PLOT="_PLOT";
    public static final String KEY_URL_POSTER="_URLPOSTER";
    public MovieDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    /*private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "( "
            + KEY_ID
            + " integer primary key autoincrement, "
            + KEY_TITLE + " STRING,"
            + KEY_YEAR + " STRING,"
            +KEY_RATING +" STRING,"
            +KEY_RUNTIME +" STRING,"
            +KEY_MAINACTORS +" STRING,"
            +KEY_PLOT + " STRING,"
            +KEY_URL_POSTER +" STRING"
            + ");";*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                        + TABLE_NAME
                        + "( "
                        + KEY_ID
                        + " integer PRIMARY KEY AUTOINCREMENT, "
                        + KEY_TITLE + " STRING,"
                        + KEY_YEAR + " STRING,"
                        +KEY_RATING +" STRING,"
                        +KEY_RUNTIME +" STRING,"
                        +KEY_MAINACTORS +" STRING,"
                        +KEY_PLOT + " STRING,"
                        +KEY_URL_POSTER +" STRING);");
        Log.i("MovieDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.i("MovieDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }
}
