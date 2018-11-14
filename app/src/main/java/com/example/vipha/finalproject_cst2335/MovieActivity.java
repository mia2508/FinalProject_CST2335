package com.example.vipha.finalproject_cst2335;
/**Author: Vi Pham
 *Student number: 040886894
 *Class Description: this class extend from Activity, simply just a main page for movie information, show all the movie in the list
 *class member:
 *class method: override Oncreate(Bundle) : void
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MovieActivity extends Activity {
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

        Button button = findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MovieInformation.class);
                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(),"Searching information", Toast.LENGTH_LONG); //this is the ListActivity
                toast.show(); //display your message box
            }
        });
    }
}
