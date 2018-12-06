package com.example.vipha.finalproject_cst2335;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;

/**
 * This class is used to list all of the user's Saved Articles in a List View.
 * Users can simply click to go to a new fragment similar to before or click and hold
 * and a dialog will appear asking if the user wants to delete the article from the list.
 */

public class SavedArticlesActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "SavedArticlesActivity";

    /**
     * These are used for the widgets in the activity
     * savedList is the ListView of all the saved articles
     * the ArrayList used to keep track of all the savedArticles in the ListView
     */

    private ListView savedList;
    private ArrayList<String> savedArticlesList;
    private SavedListAdapter savedListAdapter;
    private Button saveButton;
    private NewsFragment newsFragment;

    /**
     * These are used to query the database to retrieve/delete
     */
    private SQLiteDatabase db;
    private SQLiteDatabase db2;
    private Cursor cursor;
    private Cursor savedCursor;
    private NewsDatabaseHelper newsDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles);
        setTitle("CBC News Reader Saved Articles");
        savedList = findViewById(R.id.savedList);
        savedArticlesList = new ArrayList<>();
        savedListAdapter = new SavedListAdapter(this);

        savedList.setAdapter(savedListAdapter);

        newsDatabaseHelper = new NewsDatabaseHelper(this);

        db = newsDatabaseHelper.getWritableDatabase();
        db2 = newsDatabaseHelper.getWritableDatabase();

        newsFragment = new NewsFragment();
        //saveButton.setVisibility(View.GONE);   // hide save button when reading saved item

        cursor = db.rawQuery("SELECT * FROM " + NewsDatabaseHelper.TABLE_NAME + ";", null);
        cursor.moveToFirst();


        //loop to retrieve article names from the database and add them to the ListView
        while (!cursor.isAfterLast()) {
            savedArticlesList.add(cursor.getString(cursor.getColumnIndex(NewsDatabaseHelper.KEY_TITLE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(NewsDatabaseHelper.KEY_TITLE)));
            cursor.moveToNext();

            Log.i(ACTIVITY_NAME, "Cursor  column count =" + cursor.getColumnCount());

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.i(ACTIVITY_NAME, "Column name" + cursor.getColumnName(i));
            }
        }


        //when user clicks on an item in the ListView will retrieve all necessary data to pass to the NewsFragment
        savedList.setOnItemClickListener((adapterView, view, position, id) -> {
            String newsItem = savedListAdapter.getItem(position);

            Long idInDB = savedListAdapter.getId(position + 1);
            String stringID = idInDB.toString();
            NewsFragment newsFragment = new NewsFragment();

            Bundle passInfo = new Bundle();
            passInfo.putString("Title", newsItem);

            savedCursor = db.rawQuery("SELECT * FROM " + NewsDatabaseHelper.TABLE_NAME + " where id =?", new String[]{stringID});
            savedCursor.moveToFirst();

            if (!savedCursor.isAfterLast()) {
                String passLink = savedCursor.getString(savedCursor.getColumnIndex(NewsDatabaseHelper.KEY_LINK));
                passInfo.putString("Link", passLink);

                String passAuthor = savedCursor.getString(savedCursor.getColumnIndex(NewsDatabaseHelper.KEY_AUTHOR));
                passInfo.putString("Author", passAuthor);

                String passCategory = savedCursor.getString(savedCursor.getColumnIndex(NewsDatabaseHelper.KEY_CATEGORY));
                passInfo.putString("Category", passCategory);

                String passDate = savedCursor.getString(savedCursor.getColumnIndex(NewsDatabaseHelper.KEY_DATE));
                passInfo.putString("Date", passDate);
            }


            Intent intent = new Intent(SavedArticlesActivity.this, ArticleActivity.class);
            intent.putExtra("News Item", passInfo);
            startActivity(intent);

            savedListAdapter.notifyDataSetChanged();
        });

        //ciation for the longclick on listview items:
        //https://stackoverflow.com/questions/13470404/setonclicklistener-and-setonlongclicklistener-call-on-single-button-issue
        savedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //String newsItem = savedListAdapter.getItem(position);
                //Long idInList = savedListAdapter.getId(position);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SavedArticlesActivity.this);
                builder.setTitle("Do you want to delete this article?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //deletes the selected item

                        db2.delete(NewsDatabaseHelper.TABLE_NAME, NewsDatabaseHelper.KEY_ID + " = ?", new String[]{Long.toString(id)});
                        cursor = db2.rawQuery("SELECT * FROM " + NewsDatabaseHelper.TABLE_NAME + ";", null);
                        cursor.moveToFirst();
                        savedArticlesList.remove(position);

                        savedList.setAdapter(savedListAdapter);
                        savedListAdapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog
                    }
                });

                android.app.AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


    }


    /**
     * adapter for the savedList
     */
    public class SavedListAdapter extends ArrayAdapter<String> {

        SavedListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return savedArticlesList.size();
        }

        public String getItem(int position) {
            return savedArticlesList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = SavedArticlesActivity.this.getLayoutInflater();
            View result = null;

            result = inflater.inflate(R.layout.news_list, null);

            TextView articleItem = result.findViewById(R.id.articleItem);
            articleItem.setText(getItem(position));
            return result;
        }

        public long getId(int position) {
            return position;
        }

    }


}