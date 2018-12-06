package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class is for the CBC News Reader activity, users can search for articles
 * and article titles will appear in a list view where they can be clicked and the article details will load
 * in a fragment, users can click the link to go to the full article in a browser and can also save the article.
 * Saved articles are in a separate list view and can be deleted.
 *
 * @author Jonathan Dan
 * @Version 11/14/2018
 */


public class CBCActivity extends AppCompatActivity {

    /**
     * These variables represent the widgets/items in the activity page view which can be interacted with:
     * searchButton will search for articles once clicked, progressBar will fill as results for the search are returned,
     * searchEdiText will be used to search for articles based on what the user enters in this field
     * newsListView will list all the article titles as results
     * cbcToolbar will have the Toolbar to be used at the top with a graphical icon that can be used to go to the saved articles list
     * newsFrame is used for the fragment when a user selects an article title from the list
     */

    private Button searchButton;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private ListView newsListView;
    private Toolbar cbcToolbar;
    private FrameLayout newsFrame;

    private static final String ACTIVITY_NAME = "CBCActivity";

    /**
     * These ArrayLists are used to keep track of the index number of the particular detail
     */

    ArrayList<String> articleList = new ArrayList<>();
    ArrayList<String> articleLinksList = new ArrayList<>();
    ArrayList<String> articleCatList = new ArrayList<>();
    ArrayList<String> articleAuthorsList = new ArrayList<>();
    ArrayList<String> articleDescList = new ArrayList<>();
    ArrayList<String> articleDateList = new ArrayList<>();
    NewsListAdapter newsListAdapter;

    protected SQLiteDatabase db;
    protected Cursor cursor;
    protected Cursor cursorSearch;

    protected String userSearch;
    protected int articleNumber =-1;

    /**
     * onCreate called when the activity is started, shows the view of the layout,
     * will set the title to CBC News Reader, and show a welcoming message via Toast
     * purpose of the method is to initialize the activity, and retrieve tools in the
     * activity that need to be interacted with
     *
     * @param savedInstanceState reference object of Bundle passed to the method
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbc);
        setTitle("CBC News Reader");
        Toast welcomeToast;
        CharSequence text;
        int duration;
        text = "Welcome to the CBC News Reader";
        duration = Toast.LENGTH_SHORT;
        welcomeToast = (Toast) Toast.makeText(CBCActivity.this, text, duration);
        welcomeToast.show();

        //detailsButton = findViewById(R.id.detailsButton);
        progressBar = findViewById(R.id.progressBar);
        searchButton = findViewById(R.id.searchButton);
        newsListView = findViewById(R.id.newsListView);
        searchEditText = findViewById(R.id.searchEditText);
        cbcToolbar = (Toolbar) findViewById(R.id.cbcToolbar);
        cbcToolbar.setBackgroundColor(Color.parseColor("#CF1616"));
        setSupportActionBar(cbcToolbar);

        newsListAdapter = new NewsListAdapter(this);
        newsListView.setAdapter(newsListAdapter);

        newsFrame = findViewById(R.id.articleFrame);

        NewsDatabaseHelper newsDatabaseHelper = new NewsDatabaseHelper(this);
        db = newsDatabaseHelper.getWritableDatabase();

        CBCQuery cbcQuery = new CBCQuery();
        cbcQuery.execute();

        cursor = db.rawQuery("SELECT * FROM "+NewsDatabaseHelper.TABLE_NAME+";", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast() ) {
            //articleList.add(cursor.getString( cursor.getColumnIndex(NewsDatabaseHelper.KEY_TITLE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(NewsDatabaseHelper.KEY_TITLE)));
            cursor.moveToNext();
            Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount() );
            for(int k =0; k<cursor.getColumnCount(); k++){
                Log.i(ACTIVITY_NAME, "Column name" + cursor.getColumnName(k));
            }
        }

        searchButton.setOnClickListener((v) -> {

            //Snackbar appears once the search button is clicked, showing a message that articles are being searched for
            Snackbar searchSnackBar = Snackbar.make(searchButton,
                    "Searching Articles for: " + searchEditText.getText(), Snackbar.LENGTH_LONG);
            searchSnackBar.show();
            progressBar.setProgress(100);
//            for (int i=0; i<articleList.size(); i++) {
////                if (articleList.get(i).contains(searchEditText.getText().toString())) {
////                    return;
////                } else {
////                    articleList.remove(i);
////                }
////            }

            //articleList.clear();

//            Log.i(ACTIVITY_NAME, "FOR ARTICLES");
//            ContentValues cv = new ContentValues();
//            userSearch = searchEditText.getText().toString();
//
//            cursorSearch = db.rawQuery("SELECT * FROM "+NewsDatabaseHelper.TABLE_NAME+ " WHERE " + NewsDatabaseHelper.KEY_TITLE +
//                    " LIKE " + "'%"+userSearch+"%';",null);
//            cursorSearch.moveToFirst(); //
        });

        /**
         * When the user clicks an item in the newsListView, this is used to pass the information from the
         * ArrayLists to the next fragment
         */

        newsListView.setOnItemClickListener((adapterView, view, position, id) ->{
            String newsItem =newsListAdapter.getItem(position);
            long ID = id;
            NewsFragment newsFragment = new NewsFragment();
            Bundle passInfo = new Bundle();
            passInfo.putString("Title", newsItem );
            passInfo.putString("Link", articleLinksList.get((int)ID).toString());
            passInfo.putString("Author", articleAuthorsList.get((int)ID).toString());
            passInfo.putString("Description", articleDescList.get((int)ID).toString());
            passInfo.putString("Category", articleCatList.get((int)ID).toString());
            passInfo.putString("Date", articleDateList.get((int)ID).toString());


            if (newsFrame!=null){ //checks for tablet

                newsFragment.setArguments(passInfo);
                newsFragment.setTablet(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.articleFrame, newsFragment).commit();

            } else { //checks for phone

                newsFragment.setTablet(false);
                Intent intent = new Intent(CBCActivity.this, ArticleActivity.class);
                intent.putExtra("News Item",passInfo);
                startActivity(intent);
            }

        });

    }


    /**
     * Creates the Toolbar menu
     * @param m menu variable
     * @return
     */
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.cbc_toolbar_menu, m);
        return true;
    }

    /**
     * Actions for when the user interacts with a Toolbar item
     * @param mi used for the switch case between the two options
     * @return true when the option has been selected
     */
    public boolean onOptionsItemSelected(MenuItem mi) {

        switch (mi.getItemId()) {

            case R.id.itemOne:
                AlertDialog.Builder builder = new AlertDialog.Builder(CBCActivity.this);
                builder.setTitle("Do you want to go to your saved articles?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //goes to the SavedArticlesActivity
                        Intent goToSaved = new Intent(CBCActivity.this, SavedArticlesActivity.class);
                        startActivity(goToSaved);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;


            case R.id.itemTwo:

                AlertDialog.Builder aboutDialog = new AlertDialog.Builder(CBCActivity.this);
                aboutDialog.setTitle("About the CBC News Reader: ");

                CharSequence aboutText = "Version 1.0, by Jonathan Dan \n" +
                        "Activity Version 1.0 (December 3rd)\n" +
                        "Click the articles in the list and click the Save Button to save them - use the link to\n read the full article"
                        +"Click the icon in the Toolbar to go to your saved articles. Simply click the article in the saved list " +
                        "to get more details and the link, to delete an article from the saved list, click and hold";

                aboutDialog.setMessage(aboutText);
                AlertDialog aboutAlert = aboutDialog.create();
                aboutAlert.show();

                break;

        }
        return true;
    }

    /**
     * Inner class that uses AsyncTask to retrieve information from the http server using XML parsing
     */
    public class CBCQuery extends AsyncTask<String, Integer, String> {

        private final String ACTIVITY = "CBCQuery";

        @Override
        protected String doInBackground(String... args) {

            try {

                URL url = new URL("https://www.cbc.ca/cmlink/rss-world");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //Timeout values are 10x higher than usual to account for slow internet
                conn.setReadTimeout(100000 /* milliseconds */);
                conn.setConnectTimeout(150000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                //Starts the query
                conn.connect();


                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(conn.getInputStream(), "UTF-8");
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                // parser.nextTag();

                String articleTitle;
                String articleLink;
                String articleAuthor;
                String articleCategory;
                String articleDescription;
                String articleDate;

                boolean isNewsItem =false;

                while (parser.next() != XmlPullParser.END_DOCUMENT) {

                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "START DOCUMENT");
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("item")) {
                            isNewsItem=true;
                            Log.i(ACTIVITY_NAME, "ITEM");
                        } else if (name.equalsIgnoreCase("title")) {
                            if(isNewsItem){
                                articleTitle = parser.nextText();
                                Log.i(ACTIVITY_NAME, "TITLE");
                                articleNumber++;
                                articleList.add(articleTitle);
                            }

//                            if (articleTitle.contains(searchEditText.getText())) {
//                                articleList.add(articleTitle);
//                                articleNumber++;
//                                //Log.i(articleList.get(articleNumber).toString(), "Test");
//                                Log.i("Contains", "Search");
//                            }
                        } else if (name.equalsIgnoreCase("link")) {
                            if (isNewsItem) {
                                Log.i(ACTIVITY_NAME, "LINK");
                                articleLink = parser.nextText();

                                articleLinksList.add(articleLink);
                                Log.i("Link", articleList.get(articleNumber).toString() + "  " + articleLinksList.get(articleNumber).toString());
                            }
                        } else if (name.equalsIgnoreCase("pubDate")){
                            if (isNewsItem) {
                                Log.i(ACTIVITY_NAME, "DATE");
                                articleDate = parser.nextText();
                                articleDateList.add(articleDate);
                            }
                        }else if (name.equalsIgnoreCase("author")) {
                            Log.i(ACTIVITY_NAME, "AUTHOR");
                            articleAuthor = parser.nextText();

                            articleAuthorsList.add(articleAuthor);
                        }else if (name.equalsIgnoreCase("category")) {
                            if(isNewsItem) {
                                Log.i(ACTIVITY_NAME, "CATEGORY");
                                articleCategory = parser.nextText();

                                articleCatList.add(articleCategory);
                            }
                        }  else if (name.equalsIgnoreCase("description")){
                            if(isNewsItem) {
                                Log.i(ACTIVITY_NAME, "DESCRIPTION");
                                articleDescription = parser.nextText();
                                articleDescList.add(articleDescription);
                            }
                        }

                    }
                } Log.i(ACTIVITY_NAME,"Looped "+articleNumber);
            } catch (IOException e) {
                e.printStackTrace();

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            //newsListAdapter.notifyDataSetChanged();
            return "";
        }
    }

    /**
     * Inner class used for the NewsList
     */
    public class NewsListAdapter extends ArrayAdapter<String> {
        NewsListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return articleList.size();
        }

        public String getItem(int position) {
            return articleList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = CBCActivity.this.getLayoutInflater();
            View result = null;

            result = inflater.inflate(R.layout.news_list, null);

            TextView articleItem = result.findViewById(R.id.articleItem);
            articleItem.setText(getItem(position));
            return result;
        }

        public long getId(int position){
            return position;
        }

    }
}

