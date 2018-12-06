package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class FinalDisplay extends Activity {
    TextView totalCals, avgCals, maxCals, minCals;
    public ArrayList<String> myCalInfo;
    public static final String ACTIVITY_NAME = "FinalDisplay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_display);
        myCalInfo=new ArrayList<>();

        myCalInfo=getIntent().getExtras().getStringArrayList("CalInfo");
        Log.i(ACTIVITY_NAME, "The size of list is: "+myCalInfo.size());

        totalCals=findViewById(R.id.total_cals);
        avgCals=findViewById(R.id.avg_cals);
        maxCals=findViewById(R.id.max_cals);
        minCals=findViewById(R.id.min_cals);



    }
}
