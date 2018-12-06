package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Description: Get input from user and connect to URL to get data.
 * Class member: nameFood, caloriesFood, fatFood, progressBar, backButton, myCaloriesList, ACTIVITY_NAME, addButtom, favoriteButton,
 */

public class FoodInformation extends Activity {
    public TextView nameFood;
    public TextView caloriesFood;
    public TextView fatFood;
    public ProgressBar progressBar;
    public Button backButton;
    public ArrayList<Double> myCalorieList;
    protected static final String ACTIVITY_NAME = "FoodInformation";
    public Button addButton, favoriteButton;
    public ArrayList<String> myFavoriteList;
    static ArrayList<String> foodSavedList;
    FoodAdapter foodAdapter;



    String name ="";
    //double calories;
    String calories;
    String fat="";
    String user_input="";
    public SQLiteDatabase db;
    Context ctx=this;


    /**
     * Method member: myFavList, nameFood, caloriesFood, fatFood, progressBar, backButton, addButton
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        setTitle(R.string.food_information_tittle);
        myCalorieList=new ArrayList<>();
        myFavoriteList = new ArrayList<>();

        nameFood = findViewById(R.id.food_name_textview);
        caloriesFood = findViewById(R.id.calories_textview);
        fatFood = findViewById(R.id.fat_textview);
        progressBar = findViewById(R.id.food_progressbar);
        backButton = findViewById(R.id.take_me_back);
        addButton = findViewById(R.id.add_favorite_button);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null)
        user_input=extras.getString("inputFromUser");



        favoriteButton = findViewById(R.id.favorite_button);

        /**
         * Favourite button to come to the favourite item into a list.
         */
       favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInformation.this);
                builder.setMessage(R.string.dialog_mess);
                builder.setTitle(R.string.dialog_tittle);
                builder.setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent foodFavoriteIntent=new Intent(FoodInformation.this, FoodFavorities.class);
                        foodFavoriteIntent.putExtra("selected food", user_input);
                        startActivity(foodFavoriteIntent);
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

       /**
        * Back button to come back the previous activity
        * */

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Bundle info = new Bundle();
                info.putString("Calories", calories);
                Intent intent = new Intent();
                intent.putExtras(info);
                setResult(Activity.RESULT_OK,intent);
                finish();*/
               Intent backPage = new Intent(FoodInformation.this, FoodActivity.class);
               startActivity(backPage);
            }
        });
        // bo bundle nay
                /*Bundle info = new Bundle();
                info.putStringArrayList("FavFoods", myFavList);
                Intent intent = new Intent(FoodInformation.this, FoodFavorities.class);
                intent.putExtras(info);
                startActivity(intent);*/


        /**
         *   the Add button let the user add the selected food to the favorite
         */

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int size = myFavList.size();
                for(int i=0; i<size; i++) {*/

                    ContentValues newRow = new ContentValues();
                    newRow.put(FoodDatabaseHelper.KEY_FOOD_NAME, user_input); //all columns have a value

                    // ready to insert into database
                db.insert(FoodDatabaseHelper.TABLE_NAME, null, newRow);
                Cursor result =db.query(FoodDatabaseHelper.TABLE_NAME, new String[]{FoodDatabaseHelper.KEY_ID, FoodDatabaseHelper.KEY_FOOD_NAME}, null, null, null, null, null, null);
                myFavoriteList.add(user_input);
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("MyFavoriteList",myFavoriteList);
                foodAdapter.notifyDataSetChanged();

                nameFood.setText("");




                Toast.makeText(ctx, "R.string.toast_message_add_button", Toast.LENGTH_LONG).show();
            }
        });



        //start query
        FoodQuery query = new FoodQuery();
        query.execute();

    }

    /**
     * the inner FoodQurery class
     */
    public class FoodQuery extends AsyncTask<String, Integer, String> {
        public String doInBackground(String... args) {
            try {

                // Connect to Server
                //URL url = new URL("https://api.edamam.com/api/food-database/parser?app_id=9ae3222e&app_key=e7c25ac506b14c7050aa727a24d1a393&ingr=" + user_input);
                // I am using the link from: Chi Mien Huynh
                URL url = new URL("https://api.edamam.com/api/food-database/parser?app_id=6a50376a&app_key=244ae4242068125a61412a255d4b6a26&ingr=" + user_input);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 9);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject root = new JSONObject(result); // jsonObject is the root object

                // take the name of food
                name = root.getString("text");
                publishProgress(30);
                Log.i("Name", name);

                // take the content (fat and calories) of food
                JSONArray parsedArray = root.getJSONArray("parsed");
                JSONObject parsed = parsedArray.getJSONObject(0);
                JSONObject food = parsed.getJSONObject("food");
                JSONObject nutrients = food.getJSONObject("nutrients");
                calories = nutrients.getString("ENERC_KCAL");
                Log.i(ACTIVITY_NAME, "The calories: "+calories);
                publishProgress(70);

                fat = nutrients.getString("FAT");
                Log.i(ACTIVITY_NAME, "The fat: "+fat);
                publishProgress(100);

            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }
            return "";
        }

        public void onProgressUpdate(Integer ... value) //update your GUI
        {
            super.onProgressUpdate(value);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        public void onPostExecute(String result)  // doInBackground has finished
        {
            super.onPostExecute(result);
            nameFood.setText("Name of food: " + name);
            caloriesFood.setText("Calories: "+calories);
            fatFood.setText("Fat:" +fat);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private class FoodAdapter extends ArrayAdapter<String> {

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
           LayoutInflater inflater = FoodInformation.this.getLayoutInflater();

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


