package com.example.vipha.finalproject_cst2335;
/**Author: Vi Pham
*Student number: 040886894
*Class Description: this class extend from Activity, hold detail information about movie,
*class member: String movieURL,TextView movie_Title_textView,movie_Year_textView, movie_rating_textView,movie_runtime_textView,movie_mainActors_textView, movie_plot_textView;
* ImageView posterImageView, ProgressBar progressBar; String user_input, MovieAdapter movieAdapter, MovieDatabaseHelper movieDBHelper,Cursor cursor, SQLDatabase movieDB,String moviePosterURL
 *class method:
 **/
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.vipha.finalproject_cst2335.MainActivity.ACTIVITY_NAME;

public class MovieInformation extends Activity {
    /**
     * method member: addButton : Button, favouriteButton:Button
     * Description : create dialog when user click addButton, show snackbar when user click yes in dialog
     * @param savedInstanceState
     */

    protected final static String movieURL ="http://www.omdbapi.com/?apikey=6c9862c2&r=xml&t=";
    protected TextView movie_Title_textView,movie_Year_textView, movie_rating_textView,movie_runtime_textView,movie_mainActors_textView, movie_plot_textView;
    protected ImageView posterImageView;
    protected ProgressBar progressBar;

    public String movieTitle = "";
    public String movieYear = "";
    public String movieRating = "";
    public String movieRuntime;
    public String movieActors ="";
    public String moviePlot ="";

    public String user_input ="";
    MovieAdapter movieAdapter;
    MovieDatabaseHelper movieDBHelper;
    Cursor cursor;
    SQLiteDatabase movieDB;
//movie poster URL;
    public String moviePosterURL="";
    //HashMap<String,String> movieList= new HashMap<>();
    ArrayList<String> movie_title_ArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context ctx = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);

        movieAdapter = new MovieAdapter(ctx,movie_title_ArrayList);
/**
 * define all the variables
 */
        movie_Title_textView=findViewById(R.id.movieTitle_textView);
        movie_Year_textView=findViewById(R.id.movieYear_textView);
        movie_rating_textView=findViewById(R.id.movieRating_textView);
        movie_runtime_textView=findViewById(R.id.movieRuntime_textView);
        movie_mainActors_textView=findViewById(R.id.movieMainActors_textView);
        movie_plot_textView=findViewById(R.id.plot_textView);
        posterImageView=findViewById(R.id.movieInfoImage);

        progressBar=findViewById(R.id.progressBar_movie_information);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        //get string from user from searchEditText
        Bundle extras = getIntent().getExtras();
        user_input = extras.getString("User_input");
/**
 * Defind database
 */
        movieDBHelper = new MovieDatabaseHelper(this);
        movieDB=movieDBHelper.getWritableDatabase();

        final Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieInformation.this);
// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Do you want to add this movie to your favourite") //Add a dialog message to strings.xml

                        .setTitle("Confirmation")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            //user clock ok button
                            public void onClick(DialogInterface dialog, int id) {
                                //add movie name to arrayList
//                                movieArrayList.add(movie_Title_textView.getText().toString());
//                                movieAdapter.notifyDataSetChanged(); //this restart the process of getCount() & getView();

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MovieDatabaseHelper.KEY_TITLE, movieTitle);
                                contentValues.put(MovieDatabaseHelper.KEY_YEAR, movieYear);
                                contentValues.put(MovieDatabaseHelper.KEY_RATING, movieRating);
                                contentValues.put(MovieDatabaseHelper.KEY_RUNTIME, movieRuntime);
                                contentValues.put(MovieDatabaseHelper.KEY_MAINACTORS, movieActors);
                                contentValues.put(MovieDatabaseHelper.KEY_PLOT, moviePlot);
//                                contentValues.put(MovieDatabaseHelper.KEY_PLOT, "Hello World");
                                contentValues.put(MovieDatabaseHelper.KEY_URL_POSTER, moviePosterURL);

                                //insert to database
                                long resultId = movieDB.insert(MovieDatabaseHelper.TABLE_NAME,null,contentValues);

                                Snackbar.make( addButton, "Successful add to your list", Snackbar.LENGTH_LONG).show();

                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        })
                        .show();
            }
        });

        Button favouriteButton = findViewById(R.id.favouriteMovie);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createQuery();
                Bundle send = new Bundle();
                send.putStringArrayList("movieArrayList",movie_title_ArrayList);
                send.putString("movieYear",movieYear);

                Intent intent = new Intent(ctx, MovieFavourtie.class);
                intent.putExtras(send);
                startActivity(intent);

            }
        });
    //call forecastQuery
        new ForeCastQuery().execute();
    }
//    public void createQuery() {
//        cursor = movieDB.query(movieDBHelper.TABLE_NAME, new String[]{MovieDatabaseHelper.KEY_ID, MovieDatabaseHelper.KEY_TITLE, MovieDatabaseHelper.KEY_YEAR, MovieDatabaseHelper.KEY_RATING, MovieDatabaseHelper.KEY_RUNTIME, MovieDatabaseHelper.KEY_MAINACTORS, MovieDatabaseHelper.KEY_PLOT, MovieDatabaseHelper.KEY_URL_POSTER}, null, null, null, null, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String newMovieName = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE));
//            movie_title_ArrayList.add(newMovieName);
//            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE)));
//            cursor.moveToNext();
//        }
//    }

    /**
     * method description: this is use to connect to internet to take data back
     */
    //Connect to internet and take data
    private class ForeCastQuery extends AsyncTask<String,Integer,String> {



        private Bitmap posterMovieBitmap;
        private HttpURLConnection conn;
        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream=null;
            try {
                URL url = new URL(movieURL+ URLEncoder.encode(user_input,"UTF-8"));
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                inputStream= conn.getInputStream();

                //create pullParser
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser= factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream,"UTF-8");

                /*XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);*/
                int eventType = parser.getEventType();
                boolean set = false;
                while (eventType!= XmlPullParser.END_DOCUMENT){
                    if(eventType== XmlPullParser.START_TAG){
                        String temp = parser.getName();
                        if(parser.getName().equalsIgnoreCase("root")){
                            set=true;
                        }
                        else if(temp.equalsIgnoreCase("movie") && set){
                            movieRating =parser.getAttributeValue(null,"rated");
                            movieTitle =parser.getAttributeValue(null,"title");
                            movieYear=parser.getAttributeValue(null,"year");
                            moviePlot =parser.getAttributeValue(null,"plot");
                            movieRuntime=parser.getAttributeValue(null,"runtime");
                            movieActors= parser.getAttributeValue(null,"actors");
                            moviePosterURL=parser.getAttributeValue(null,"poster");
                            publishProgress(75);
                           // File file = getBaseContext().getFileStreamPath(moviePosterURL);
                            int lastSlash = moviePosterURL.lastIndexOf("/");
                            String fileName = moviePosterURL.substring(lastSlash + 1);
                            if(!fileExistance(fileName)) {
                               posterMovieBitmap = getImage(moviePosterURL);

                                FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                posterMovieBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                            }
                            FileInputStream fis=null;

                            try {
                                fis = openFileInput(fileName);

                            } catch (FileNotFoundException e) {
                                Log.i(ACTIVITY_NAME, movieActors + "is not found, need to download");
                            }
                            posterMovieBitmap = BitmapFactory.decodeStream(fis);


                            publishProgress(100);
                        }

                    }eventType=parser.next();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setProgress(values[0]);
//        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            movie_Title_textView.setText("Title: " + movieTitle );
            movie_Year_textView.setText("Year: "+ movieYear );
            movie_rating_textView.setText("Rated: "+ movieRating);
            movie_runtime_textView.setText("Runtime: "+ movieRuntime);
            movie_mainActors_textView.setText("Main Actors: "+movieActors);
            movie_plot_textView.setText("Plot: "+moviePlot);

            posterImageView.setImageBitmap(posterMovieBitmap);
            progressBar.setVisibility(View.INVISIBLE);
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        public Bitmap getImage(String fname) {
            try {
                URL url = new URL(fname);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }
}

}

