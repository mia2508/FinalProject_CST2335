package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * @author Nhu Ngoc Dang
 * @version 1
 * description: the purpose of this class is create the Food page to let user search food information
 * */
public class FoodActivity extends Activity {

    /**
     * The variables connect the other components in the layout file
     * searchButton lets user click when searching food information
     * favoriteButton lets user go to favourite Page
     * foodListView shows all the list which user searched
     * searchingEditText lets user give the input
     * foodProgressBar load when waiting the food result
     * */
    protected static final String ACTIVITY_NAME = "FoodActivity";
    private Button searchButton;
    private EditText searchingEditText;
    private ProgressBar foodProgressBar;
    public Button otherActivityButton;
    public ListView foodListView;
    public Cursor result;


    public ListView favoriteListView; // list view in the favorite page

    public SQLiteDatabase db;
    FoodDatabaseHelper foodDatabaseHelper;
    static ArrayList<String> foodSavedList; // chua string
    public LayoutInflater inflater;
    public static FoodAdapter foodAdapter; // chua id
    String foodName;
    Context ctx=this;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        setTitle(R.string.food_activity_tittle);

        // save the name of food in the List View on the FoodActivity
        foodDatabaseHelper = new FoodDatabaseHelper(ctx);
        db = foodDatabaseHelper.getWritableDatabase();




        searchButton = findViewById(R.id.search_button);
        searchingEditText = findViewById(R.id.search_info_edit_text);
        foodProgressBar = findViewById(R.id.progress_bar);
        otherActivityButton = findViewById(R.id.change_activity_button);
        foodListView = findViewById(R.id.food_information_listview);
        favoriteListView=findViewById(R.id.foodfavorite_listview);



        // contain the string
        foodSavedList = new ArrayList<>();

        result = db.query(FoodDatabaseHelper.TABLE_NAME, new String[]{FoodDatabaseHelper.KEY_ID, FoodDatabaseHelper.KEY_FOOD_NAME},null,null,null,null,null,null);

            if(result != null){
                Log.i(ACTIVITY_NAME, "Cursor's column count ="+result.getColumnCount());

                while (result.moveToNext()){
                    String message = result.getString(result.getColumnIndex(FoodDatabaseHelper.KEY_FOOD_NAME));
                    Log.i(ACTIVITY_NAME, "SQL MESSAGE:" +message);

                    foodSavedList.add(message);
                }
                for(int i = 0; i < result.getColumnCount(); i++){
                    Log.i(ACTIVITY_NAME, "Column Name: " + result.getColumnName(i));
                }
            }

            foodAdapter = new FoodAdapter(this);
            foodListView.setAdapter(foodAdapter);
        /**
         * rootView for putting the SnackBar to the activity_food layout
         * */
        final RelativeLayout rootView=findViewById(R.id.root_view);
        final Context ctx = this;

        /**
         * this shows the snack bar when user go to the activity_food layout
         * */
        Snackbar snackbar = Snackbar.make(rootView, R.string.welcome_mess, Snackbar.LENGTH_LONG);
        snackbar.show();
/**
 * Search button lets user search information of the food which user type in the edit text
 */
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // this shows the toast "Loading data" when user clicks on search button
                foodName = searchingEditText.getText().toString();
              //  String input = searchingEditText.getText().toString();
                Toast.makeText(FoodActivity.this, "Loading data", Toast.LENGTH_LONG).show();

                // this goes to FoodInformation class


                // this saves the data which user search
               // foodName = searchingEditText.getText().toString();
                ContentValues newRow = new ContentValues();
                newRow.put(FoodDatabaseHelper.KEY_FOOD_NAME, foodName); // all columns have a value

                //ready to insert into database
                db.insert(FoodDatabaseHelper.TABLE_NAME, null, newRow);
                result = db.query(FoodDatabaseHelper.TABLE_NAME, new String[]{FoodDatabaseHelper.KEY_ID, FoodDatabaseHelper.KEY_FOOD_NAME}, null, null, null, null, null, null);
                foodSavedList.add(foodName);
                foodAdapter.notifyDataSetChanged(); // data has changed

                searchingEditText.setText("");

                Intent foodInformationIntent = new Intent(ctx, FoodInformation.class);
                foodInformationIntent.putExtra("inputFromUser", foodName);
                startActivity(foodInformationIntent);
            }
        });


        /**
         *  change to other activities: bus, cbc news, movie
          */

        otherActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toolbarPage = new Intent(FoodActivity.this, FoodToolbar.class);
                startActivity(toolbarPage);
            }
        });

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private class FoodAdapter extends ArrayAdapter<String>{

        public FoodAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount(){
            return foodSavedList.size();
        }


       public String getItem(int position){
           return  foodSavedList.get(position);
       }
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = FoodActivity.this.getLayoutInflater();

            View result = null;

                result = inflater.inflate(R.layout.check_box_list_view, null);
                TextView foodName= result.findViewById(R.id.food_textview_listview);
                foodName.setText(getItem(position));
                return result;
        }

        public long getItemId(int position) {
            //results.moveToPosition(position);
           // long messageID =  results.getLong(results.getColumnIndex(ChatDatabaseHelper.KEY_ID));
            return position;
        }
    }

}

