package com.example.vipha.finalproject_cst2335;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Description: create the toolbars which go to bus, cbc, movie activity and help toolbar to show the author information, version and instruction
 */
public class FoodToolbar extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_toolbar);
        setTitle(R.string.favorite_page_tittle);

        Toolbar foodToolbar = findViewById(R.id.food_toolbar);
        setSupportActionBar(foodToolbar);


    }


    /**
     * @param menu
     * @return true
     * The purpose of this function is to create the toolbar by inflating it from food_toolbar_menu.xml file
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_toolbar_menu, menu);
        return true;
    }

    /**
     *
     * @param menuItem
     * @return true
     * this function give the options of the toolbar which user can choose
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id){
            case R.id.bus:
                Intent foodPage = new Intent(FoodToolbar.this, BusActivity.class);
                startActivity(foodPage);
                break;
            case R.id.news:
                Intent newsPage = new Intent(FoodToolbar.this, CBCActivity.class);
                startActivity(newsPage);
                break;
            case R.id.movie:
                Intent moviePage = new Intent(FoodToolbar.this, MovieActivity.class);
                startActivity(moviePage);
                break;
            case R.id.help_action:
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodToolbar.this);

                builder.setIcon(R.drawable.cherry)
                        .setTitle(R.string.help_menu)
                        .setMessage(R.string.instruction)
                        .setPositiveButton(R.string.close_button_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;


        }

        return true;
    }
    }
