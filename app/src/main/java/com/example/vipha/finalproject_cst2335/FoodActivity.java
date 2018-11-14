package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
/**
 * @author Nhu Ngoc Dang
 * @version 1
 * description: the purpose of this class is create the Food page to let user search food information
 * */
public class FoodActivity extends Activity {

    /**
     * searchBt displays  the Search button
     * */

    private Button searchBt;
    /**
     * favoriteBt displays the Favorite List button
     * */
    private Button favoriteBt;
    private ListView foodList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        setTitle("Nutrition Database");

        /**
         * rootView for putting the SnackBar to the activity_food layout
         * */
        final RelativeLayout rootView = findViewById(R.id.root_view);

        /**
         * this shows the snack bar when user go to the activity_food layout
         * */
        Snackbar snackbar = Snackbar.make(rootView, R.string.welcome_mess, Snackbar.LENGTH_LONG);
        snackbar.show();

        /**
         * {@value #ctx} ctx object
         * */
        final Context ctx = this;

        /**
         * find the Search button via id
         *
         * */
        searchBt = findViewById(R.id.search_bt);
        searchBt.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that lets user click on the button
             * then the Toast is shown
             * @param v
             */
            @Override
            public void onClick(View v) {

                //Snackbar.make(searchBt, R.string.search_mess,Snackbar.LENGTH_LONG).show();
                Toast.makeText(FoodActivity.this, "Loading data", Toast.LENGTH_LONG).show();

            }
        });

        /**
         * define the Favorite Button via id
         *
         * */
        favoriteBt = findViewById(R.id.favorite_button);
        favoriteBt.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that lets user clcik on the favorite button
             * then the Dialog is shown to confirm user want to the Favorite List or nor
             * @param v
             */
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
                builder.setMessage(R.string.dialog_mess);
                builder.setTitle(R.string.dialog_tittle);
                builder.setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ctx, FoodFavorities.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();
            }
        });
    }
}
