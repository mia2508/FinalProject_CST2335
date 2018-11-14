package com.example.vipha.finalproject_cst2335;
/**Author: Vi Pham
*Student number: 040886894
*Class Description: this class extend from Activity, hold detail information about movie,
*class member:
*class method: override Oncreate(Bundle) : void
 **/
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

public class MovieInformation extends Activity {
    /**
     * method member: addButton : Button, favouriteButton:Button
     * Description : create dialog when user click addButton, show snackbar when user click yes in dialog
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context ctx = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);

        final Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieInformation.this);
// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Do you want to add this movie to your favourite") //Add a dialog message to strings.xml

                        .setTitle("Confirmation")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Snackbar.make( addButton, "Successful add to your list", Snackbar.LENGTH_LONG).show();
                                // User clicked OK button
                                  /*  finish();
                                    Intent intent = new Intent(ListItemsActivity.this, StartActivity.class);
                                    startActivity(intent);*/
                                /*Intent resultIntent = new Intent(  );
                                resultIntent.putExtra("Response", "Here is my response");
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();*/
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        })
                        .show();
            }
        });

        Button favouriteButton = findViewById(R.id.favouriteMovie);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, FavouriteMovie.class);
                startActivity(intent);

            }
        });

    }
}
