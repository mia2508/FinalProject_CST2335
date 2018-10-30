package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class activity_main extends Activity {
    protected static final String ACTIVITY_NAME = "activity_main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button movieButton=findViewById(R.id.movieButton);
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movieMain= new Intent(activity_main.this, MovieMain.class);
                startActivity(movieMain);
            }
        });
    }

}
