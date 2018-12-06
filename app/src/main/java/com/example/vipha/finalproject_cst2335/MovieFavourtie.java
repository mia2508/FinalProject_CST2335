package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.vipha.finalproject_cst2335.MainActivity.ACTIVITY_NAME;
import static com.example.vipha.finalproject_cst2335.MovieDatabaseHelper.KEY_ID;
import static com.example.vipha.finalproject_cst2335.MovieDatabaseHelper.KEY_MAINACTORS;
import static com.example.vipha.finalproject_cst2335.MovieDatabaseHelper.KEY_PLOT;
import static com.example.vipha.finalproject_cst2335.MovieDatabaseHelper.KEY_RATING;
import static com.example.vipha.finalproject_cst2335.MovieDatabaseHelper.KEY_RUNTIME;
import static com.example.vipha.finalproject_cst2335.MovieDatabaseHelper.KEY_TITLE;
import static com.example.vipha.finalproject_cst2335.MovieDatabaseHelper.KEY_URL_POSTER;
import static com.example.vipha.finalproject_cst2335.MovieDatabaseHelper.KEY_YEAR;

/**
 * Author: Vi Pham
 * Class description: this class is showing the list of favourtie movie that user save
 * Class variable: Button deleteButton, ListView favouriteMovieListView, MovieAdapter movieAdapter, Movie
 */
public class MovieFavourtie extends Activity {
    protected static final String ACTIVITY_NAME = "MovieFavourite";
    Button deleteButton;
    ListView favouriteMovieListView;
    MovieAdapter movieAdapter;
    MovieDatabaseHelper movieDBHelper;
    Cursor cursor;
    SQLiteDatabase movieDB;

    ArrayList<String> movieList = new ArrayList<>();
    String movieYear="";
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movie);
        ctx = this;
        movieDBHelper=new MovieDatabaseHelper(ctx);
        movieDB=movieDBHelper.getWritableDatabase();

        Bundle infoToPass = getIntent().getExtras();
        movieList=infoToPass.getStringArrayList("movieArrayList");
        movieYear=infoToPass.getString("movieYear");
        //set adapter
        movieAdapter=new MovieAdapter(ctx,movieList);
        movieAdapter.notifyDataSetChanged();

        favouriteMovieListView=findViewById(R.id.favourite_movie_ListView);
        favouriteMovieListView.setAdapter(movieAdapter);
        favouriteMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle pass = new Bundle();
                Long idMovie = movieAdapter.getItemId(position+1);
                String idString = idMovie.toString();
                cursor=movieDB.query(MovieDatabaseHelper.TABLE_NAME, null, MovieDatabaseHelper.KEY_ID + "=?", new String[] {idString}, null,null,null
                ,null);
//                cursor=movieDB.rawQuery("SELECT * FROM "+MovieDatabaseHelper.TABLE_NAME+" where id =?",new String[]{idString});
                cursor.moveToFirst();
                if(!cursor.isAfterLast()){
                    pass.putLong("ID",idMovie);
                    String passTitle=cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE));
                    pass.putString("title",passTitle);

                    String passYear=cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_YEAR));
                    pass.putString("year",passYear);

                    String passRuntime=cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_RUNTIME));
                    pass.putString("run_time",passRuntime);

                    String passRating=cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_RATING));
                    pass.putString("rating",passRating);

                    String passActors=cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_MAINACTORS));
                    pass.putString("actors",passActors);

                    String passPlot=cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_PLOT));
                    pass.putString("plot",passPlot);
                }

                Intent phoneIntent = new Intent (MovieFavourtie.this,MovieDetail.class);
                phoneIntent.putExtras(pass);
                try {
                    startActivityForResult(phoneIntent,50);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        cursor= movieDB.query(movieDBHelper.TABLE_NAME,new String[]{KEY_ID,KEY_TITLE,KEY_YEAR,KEY_RATING,KEY_RUNTIME,KEY_MAINACTORS,KEY_PLOT,KEY_URL_POSTER},null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            String newMovie= cursor.getString(cursor.getColumnIndex(KEY_TITLE));
            movieList.add(newMovie);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            cursor.moveToNext();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==50 && resultCode == Activity.RESULT_OK) {
            Bundle infoToPass = data.getExtras();
            long ID = (long)infoToPass.get("ID");
            deleteMessage(ID);
        }
    }

    /**
     * method description: this method use to delete movie name from the list and from database
     * @param id
     */
    public void deleteMessage(long id){
        movieDB.delete(MovieDatabaseHelper.TABLE_NAME,KEY_ID+"="+id,null);
        //list.remove(position);
       movieList = new ArrayList<>();

        cursor= movieDB.query(movieDBHelper.TABLE_NAME,new String[]{KEY_ID,KEY_TITLE,KEY_YEAR,KEY_RATING,KEY_RUNTIME,KEY_MAINACTORS,KEY_PLOT,KEY_URL_POSTER},null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            String newMessage= cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE));
            movieList.add(newMessage);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_TITLE)));
            cursor.moveToNext();
        }
        movieAdapter.notifyDataSetChanged();
//      Intent intent= new Intent(this, ChatWindow.class);
//     this.startActivity(intent);
    }
}
