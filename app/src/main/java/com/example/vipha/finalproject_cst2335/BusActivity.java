package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * @author Kim Ngan Dang
 * Student number: 040871976
 * @Description: This is the main page for OCTranspo Bus Route Activity
 **/
public class BusActivity extends Activity {
    protected static final String ACTIVITY_NAME = "OCTranspo Bus Route";

    /**
     * Variables:
     *      ListView busView : displays the information of the bus number that user select
     *      Button searchBt : uses to search what bus number that user want to find
     *      ProgressBar ob :displays when the information is getting from the internet
     *      TextView text: where user can type here**/
    private ListView busView;
    private Button searchBt;
    private ProgressBar pb;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        setTitle("OCTranspo Bus Route");
        Log.i(ACTIVITY_NAME, "In onCreate()");

        final Context ctx = this;
        text = findViewById(R.id.text);
        searchBt = findViewById(R.id.button);
        busView = findViewById(R.id.listview);
        pb = findViewById(R.id.pbLoading);

        /**progressBar displays visible**/
        pb.setVisibility(View.VISIBLE);

        /**
         * When user clicked on search button, they will get a message
         * **/
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(ctx, InfoActivity.class);
                    startActivityForResult(intent, 67);
               // }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy");
    }
}
