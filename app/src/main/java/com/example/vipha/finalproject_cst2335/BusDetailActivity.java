package com.example.vipha.finalproject_cst2335;
/**
 * This activity is used to display the information of which bus that user clicked on phone
 **/

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class BusDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);


        Bundle info = getIntent().getExtras();


        InfoFragmentActivity newFragment = new InfoFragmentActivity();
        newFragment.setArguments(info);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_location, newFragment).commit();


    }
}




