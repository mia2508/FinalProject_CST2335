package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * this class shows the favorite list of all the food which user selected
 */
public class FoodFavorities extends Activity {

    public ListView favoriteListView;
    public static final String ACTIVITY_NAME = "FoodFavorities";
    Button startBtn, home;
    //public ArrayList<FoodInfo> myFoodNameList;
    public ArrayList<String> myCalList;
    public ArrayList<String> myFavFoods;
    public NewFoodQuery foodQuery;
    Context ctx=this;
    public String calories;
    public String name;
    public String userInput;
    public FoodDatabaseHelper foodDatabaseHelper;
    public SQLiteDatabase db;
    public Cursor result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_favorities);
        myCalList = new ArrayList<>();
        //myFoodNameList=new ArrayList<>();
        myFavFoods = new ArrayList<>();
        foodQuery = new NewFoodQuery();
        foodDatabaseHelper=new FoodDatabaseHelper(ctx);
        db=foodDatabaseHelper.getWritableDatabase();

        Bundle infoGetFromFood = getIntent().getExtras();
        myFavFoods= infoGetFromFood.getStringArrayList("FavFoods");


      //  myFavFoods = getIntent().getExtras().getStringArrayList("FavFoods");
        runNewQuery();
        int size = myFavFoods.size();
        Log.i(ACTIVITY_NAME, "The size of list: "+size);

        for(int i=0; i<size; i++){
            userInput=myFavFoods.get(i);
           // foodQuery.execute();
            myCalList.add(calories);
            // question

        }

        setTitle(R.string.favorite_page_tittle);

        favoriteListView = findViewById(R.id.foodfavorite_listview);
        startBtn = findViewById(R.id.stat_btn);
        home = findViewById(R.id.back_btn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle info =new Bundle();
                info.putStringArrayList("CalInfo", myCalList);
                Intent intent = new Intent(FoodFavorities.this, FinalDisplay.class);
                intent.putExtras(info);
                startActivity(intent);

            }
        });
    }
    public void runNewQuery(){
        result = db.query(FoodDatabaseHelper.TABLE_NAME_FAVORITE, new String[]{FoodDatabaseHelper.KEY_ID, FoodDatabaseHelper.KEY_FOOD_NAME},null,null,null,null,null,null);

        if(result != null){
            Log.i(ACTIVITY_NAME, "Cursor's column count ="+result.getColumnCount());

            while (result.moveToNext()){
                String message = result.getString(result.getColumnIndex(FoodDatabaseHelper.KEY_FOOD_NAME));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" +message);

                myFavFoods.add(message);
            }
            for(int i = 0; i < result.getColumnCount(); i++){
                Log.i(ACTIVITY_NAME, "Column Name: " + result.getColumnName(i));
            }
        }
    }

    class NewFoodQuery extends AsyncTask<String, Integer, String> {
        public String doInBackground(String... args) {
            try {

                // Connect to Server
                URL url = new URL("https://api.edamam.com/api/food-database/parser?app_id=e5bc806d&app_key=5f7521ffeefe491b936cea6271e13d3d&mode=XML&ingr="+userInput);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
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


                publishProgress(100);

            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }
            return "";
        }

        public void onProgressUpdate(Integer ... value) //update your GUI
        {
            /*progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);*/
        }

        public void onPostExecute(String result)  // doInBackground has finished
        {
            /*nameFood.setText("Name item:" + name);
            caloriesFood.setText("Calories:"+calories);
            fatFood.setText("Fat:" +fat);
            progressBar.setVisibility(View.INVISIBLE);*/
        }
    }


/*    private class FavFoodAdapter extends ArrayAdapter<FoodInfo> {

        public FavFoodAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount(){
            return myFoodNameList.size();
        }


        public FoodInfo getItem(int position){
            return  myFoodNameList.get(position);
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = FoodFavorities.this.getLayoutInflater();

            View result = null;

            result = inflater.inflate(R.layout.inflate_my_favourite, null);

            CheckBox chkbox = result.findViewById(R.id.new_checkbox);

            chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        String myCalories = ""+myFoodNameList.get(position).getCals();
                        myCalList.add(myCalories);


                    }
                }
            });
            TextView foodName= result.findViewById(R.id.fav_food_name);
            TextView foodCal= result.findViewById(R.id.fav_food_cal);
            foodName.setText("Food name: "+getItem(position).getFood());
            foodCal.setText("Calorie content: "+getItem(position).getCals());

            return result;
        }

        public long getItemId(int position) {
            //results.moveToPosition(position);
            // long messageID =  results.getLong(results.getColumnIndex(ChatDatabaseHelper.KEY_ID));
            return position;
        }
    }*/
}
