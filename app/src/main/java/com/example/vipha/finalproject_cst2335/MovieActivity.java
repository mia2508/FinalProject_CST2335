package com.example.vipha.finalproject_cst2335;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;



/**Author: Vi Pham
 *Student number: 040886894
 *Class Description: this class extend from Activity, simply just a main page for movie information, user can use editText to search the movie they want
 *class member: Toolbar movieToolbar, Button searchButton, EditText searchEditText,
 *class method:
 */
public class MovieActivity extends AppCompatActivity {

    private Toolbar movieToolbar;
    private Button searchButton;
    private EditText searchEditText;
    private Button favouriteButton;

    /**
     * Method member: Button searchButton
     * Description: open MovieInformation class when searchButton is clicked, and show a toast to notify user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context ctx = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
       // progressBar=findViewById(R.id.progressBar);
        searchEditText=findViewById(R.id.searchEditText);
       // movieListView = findViewById(R.id.movieListView);
        movieToolbar=findViewById(R.id.movie_toolbar);
        setSupportActionBar(movieToolbar);
//        favouriteButton=findViewById(R.id.favouriteMovie);
//        favouriteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ctx,MovieFavourtie.class);
//                startActivity(intent);
//            }
//        });
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchEditText.length()!=0) {
                    Intent intent = new Intent(ctx, MovieInformation.class);
                    //get data from user and pass it through intent
                    intent.putExtra("User_input", searchEditText.getText().toString());
                    startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(), "Searching information", Toast.LENGTH_LONG); //this is the ListActivity
                    toast.show(); //display your message box
                    searchEditText.setText("");
                }else {
                    Toast.makeText(MovieActivity.this,getString(R.string.errorBlank),Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    /**
     *
     * use to get inflate from movie_toolbar
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.movie_toolbar,m);
        return true;
    }

    /**
     *
     * @param mi set all the function for toolbar
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();

        switch(id){
            case R.id.food_item:
                Log.d("Toolbar", "Option 1 selected");
                startActivity(new Intent(this, FoodActivity.class));

                //Snackbar.make(findViewById(R.id.food_item),currentMessage, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.bus_item:
                startActivity(new Intent(this, BusActivity.class));
                break;
            case R.id.news_item:
                startActivity(new Intent(this, CBCActivity.class));
                break;
            case R.id.help_item:
                AlertDialog.Builder builder = new AlertDialog.Builder((MovieActivity.this));
                builder.setMessage(getString(R.string.Vi) + "\n" + getString(R.string.food_version) + "\n"+ getString(R.string.food_help)+getString(R.string.food_help2)).setTitle(R.string.help).setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();
                break;
        }
        return true;

    }

}
