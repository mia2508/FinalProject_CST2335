package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * This class is for the CBC News Reader activity, users can search for articles
 * and article titles will appear in a list view where they can be clicked and the article
 * will load.
 * @author Jonathan Dan
 */


public class CBCActivity extends Activity {

    /**
     * These variables represent the widgets/items in the activity page view which can be interacted with:
     * searchButton will search for articles once clicked, progressBar will fill as results for the search are returned,
     * searchText will be used to search for articles based on what the user enters in this field
     * newsList will list all the article titles as results
     * detailsButton will lead to a new activity showing the details of the page
     */

    protected Button searchButton;
    protected ProgressBar progressBar;
    protected EditText searchText;
    protected ListView newsList;
    protected Button detailsButton;

    /**
     * onCreate called when the activity is started, shows the view of the layout,
     * will set the title to CBC News Reader, and show a welcoming message via Toast
     * purpose of the method is to initialize the activity, and retrieve tools in the
     * activity that need to be interacted with
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
        duration=Toast.LENGTH_SHORT;
        welcomeToast = (Toast)Toast.makeText(CBCActivity.this,text,duration);
        welcomeToast.show();

        detailsButton = findViewById(R.id.detailsButton);
        progressBar = findViewById(R.id.progressBar);
        searchButton = findViewById(R.id.searchButton);
        newsList = findViewById(R.id.listView);
        searchText = findViewById(R.id.editText);
        searchButton.setOnClickListener((v)->{

            //Snackbar appears once the search button is clicked, showing a message that articles are being searched for
            Snackbar searchSnackBar = Snackbar.make(searchButton,
                    "Searching Articles for: "+searchText.getText(), Snackbar.LENGTH_LONG);
            searchSnackBar.show();
            progressBar.setProgress(100);  //showing the progressbar being filled, will adjust as other project requirements are implemented


        });

        detailsButton.setOnClickListener((v) -> {
            AlertDialog.Builder searchDialog = new AlertDialog.Builder(CBCActivity.this);
            searchDialog.setMessage("Proceed to view details about this item");
            searchDialog.setTitle("View Details?");
            searchDialog.setPositiveButton("Go to Details", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent detailsIntent = new Intent (CBCActivity.this, ArticleActivity.class);
                    startActivity(detailsIntent);
                }
            });

            searchDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });

            searchDialog.show();
        });

        //

    }
}
