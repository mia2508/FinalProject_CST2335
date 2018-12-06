package com.example.vipha.finalproject_cst2335;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * This class will be used to hold the article information in a fragment
 * Will show the Title, Author, Date, Category, Link, Description of the article
 * Will provide a link where the user can click and open the default Android Browser.
 */

public class NewsFragment extends Fragment {

    /*
    Widget variables used to display the attributes of the article in the fragment
     */

    private TextView titleView;
    private TextView linkView;
    private TextView authorView;
    private TextView categoryView;
    private TextView descriptionView;
    private TextView dateView;
    private Button saveArticleButton;

    //private Button deleteArticleButton;

    private Bundle bundle;
    private boolean isTablet;

    //database variables
    private SQLiteDatabase db;
    private Cursor cursor;
    Context ctx;
    NewsDatabaseHelper newsDatabaseHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //sets the tablet to true and behaviour will change
    public void setTablet(boolean b) {
        this.isTablet = b;
    }


    /**
     * This method will create the view of the fragment -  will retrieve information for the ArrayLists from either
     * the ArrayLists in CBCActivity or the SQLite Database
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View gui = inflater.inflate(R.layout.activity_cbc_fragment, container, false);
        titleView = (TextView) gui.findViewById(R.id.titleView);
        linkView = (TextView) gui.findViewById(R.id.linkView);
        authorView = (TextView) gui.findViewById(R.id.authorView);
        descriptionView = (TextView) gui.findViewById(R.id.descView);
        categoryView = (TextView) gui.findViewById(R.id.categoryView);
        dateView = (TextView) gui.findViewById(R.id.dateView);

        saveArticleButton = (Button) gui.findViewById(R.id.saveButton);
        //deleteArticleButton =(Button) gui.findViewById(R.id.deleteButton);

        bundle = getArguments();

        ctx = container.getContext();

        newsDatabaseHelper = new NewsDatabaseHelper(ctx);
        db = newsDatabaseHelper.getWritableDatabase();

        //gets it from the previous activity
        String title = bundle.getString("Title");
        String link = bundle.getString("Link");
        String author = bundle.getString("Author");
        String description = bundle.getString("Description");
        String category = bundle.getString("Category");
        String date = bundle.getString("Date");

        titleView.setText(title);
        authorView.setText(author);
        descriptionView.setText(description);
        categoryView.setText(category);
        dateView.setText(date);


        linkView.setLinksClickable(true);
        linkView.setMovementMethod(LinkMovementMethod.getInstance());
        linkView.setText(link);
        //once clicked will open the default android browser to the webpage of the article
        linkView.setOnClickListener((v) -> {

                    //citation
                    //https://stackoverflow.com/questions/2734270/how-do-i-make-links-in-a-textview-clickable
                    Intent browser = new Intent(Intent.ACTION_VIEW);
                    browser.setData(Uri.parse(link));
                    startActivity(browser);
                    Log.i("CLICKED ON", link);

                }
        );

        //saves the article to the Saved Articles List
        saveArticleButton.setOnClickListener((v) -> {

                    ContentValues cv = new ContentValues();
                    cv.put(NewsDatabaseHelper.KEY_TITLE, title);
                    cv.put(NewsDatabaseHelper.KEY_LINK, link);
                    cv.put(NewsDatabaseHelper.KEY_AUTHOR, author);
                    //cv.put(NewsDatabaseHelper.KEY_DESCRIPTION, description);
                    cv.put(NewsDatabaseHelper.KEY_CATEGORY, category);
                    cv.put(NewsDatabaseHelper.KEY_DATE, date);

                    db.insert(NewsDatabaseHelper.TABLE_NAME, null, cv);
                    cursor = db.rawQuery("SELECT * FROM " + NewsDatabaseHelper.TABLE_NAME + ";", null);
                    cursor.moveToFirst();

                    Toast savedToast;
                    CharSequence savedToastText;
                    int duration;
                    savedToastText = "Saved " + title;
                    duration = Toast.LENGTH_SHORT;
                    savedToast = (Toast) Toast.makeText(ctx, savedToastText, duration);
                    savedToast.show();

                }
        );

        return gui;
    }

}