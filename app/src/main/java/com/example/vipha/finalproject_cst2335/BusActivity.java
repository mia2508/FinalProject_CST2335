package com.example.vipha.finalproject_cst2335;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Kim Ngan Dang
 * Student number: 040871976
 * @Description: This is the main page for OCTranspo Bus Route Activity
 **/
public class BusActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "OCTranspo Bus Route";

    /**
     * Variables:
     * ListView busListVIew : displays the information of the bus number that user select
     * Button searchBtn : uses to search what bus number that user want to find
     * ProgressBar ob :displays when the information is getting from the internet
     * TextView text: where user can type here
     **/
    private ListView busListVIew;


    private Toolbar toolbar;
    private String currentMessage = "Bus bus~~~~";
    private static final String BusURL =
            "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=";
    //private List<String> list;
    private BusAdapter busAdapter;
    private ContentValues cv;
    private EditText searchEdt;

    private Cursor results;

    //public BusRoute currentRoute;
    public BusDatabaseHelper dbOpener;
    public SQLiteDatabase db;
    public ArrayList<String> myStopNUmber;


    private Context ctx = this;
    private String user_input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        setTitle("OCTranspo Bus Route");
        Log.i(ACTIVITY_NAME, "In onCreate()");
        // myStopNUmber = new ArrayList<>();

        searchEdt = findViewById(R.id.searchEditText);
        busListVIew = findViewById(R.id.bus_listview);

        new RouteQuery().execute(user_input);


        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        cv = new ContentValues();
        myStopNUmber = new ArrayList<>();

        //open data
        dbOpener = new BusDatabaseHelper(this);//helper object
        db = dbOpener.getWritableDatabase();

        busAdapter = new BusAdapter(this);


        busListVIew.setAdapter(busAdapter);
        busAdapter.notifyDataSetChanged();

        /**
         * Create Query
         **/
        String[] columns = {BusDatabaseHelper.KEY_ID, BusDatabaseHelper.KEY_MESSAGE};
        results =db.query(BusDatabaseHelper.TABLE_NAME,columns,
                null,null,null,null,null);

        //each message that you retrieve from the cursor object
        while(results.moveToNext())

        {
            String newMessage = results.getString(results.getColumnIndex(BusDatabaseHelper.KEY_MESSAGE));
            myStopNUmber.add(newMessage);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + newMessage);
        }


        busListVIew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String content = searchEdt.getText().toString();
                cv.put("MessageCol", content);
                db.insert(BusDatabaseHelper.TABLE_NAME, "NullCol", cv);


                    Bundle info = new Bundle();
                    info.putString("StopNumber", content);
                    Intent newIntent = new Intent(BusActivity.this, BusStopActivity.class);
                    newIntent.putExtras(info);
                    Log.i(ACTIVITY_NAME, "The stop number passed: " + content);

                    startActivity(newIntent);
                    busListVIew.setAdapter(busAdapter);
                    searchEdt.setText("");

                    busAdapter.notifyDataSetChanged();
                }


        });

        /** Long click on one item in listview will display a dialog ask to delete**/
        busListVIew.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //when long click , then the message poped up
                AlertDialog.Builder builder = new AlertDialog.Builder(BusActivity.this);
                builder.setMessage("Confirm Delete??");
                builder.setTitle("Alarm!");


                builder.setPositiveButton("Yes,sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (myStopNUmber.remove(position) != null) {
                            System.out.println("success");

                        } else {
                            System.out.println("failed");
                        }
                        busListVIew.setAdapter(busAdapter);
                        busAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Delete this item", Toast.LENGTH_SHORT).show();
                    }
                });


                builder.setNegativeButton("NO!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return false;
            }
        });


        //print out the name of each column return by cursors
       /* for (int i = 0; i < results.getColumnCount(); i++) {
            String columnName = results.getColumnName(i);
            Log.i(ACTIVITY_NAME, "Column name:" + columnName);
        }*/


    }



    /**
     * get data from the link provided
     **/
    private class RouteQuery extends AsyncTask<String, Integer, String> {
        HttpURLConnection conn;
        public XmlPullParser parser;
        private String routeNum = "";


        @Override
        protected String doInBackground(String... args) {
            InputStream in;
            try {
                Log.i(ACTIVITY_NAME, "The user input is: " + user_input);
                URL url = new URL(BusURL + user_input);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                // in = conn.getInputStream();
                Log.i(ACTIVITY_NAME, "The connection was successful");

                parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), "UTF-8");
                //  parser.nextTag();
                Log.i(ACTIVITY_NAME, "Outside while loop");
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        String name = parser.getName();
                        Log.i(ACTIVITY_NAME, "The name is: " + name);


                        switch (name) {
                            case "StopNo":
                                //routeNum = parser.getAttributeValue(null, "RouteNo");
                                if (parser.next() == XmlPullParser.TEXT) {
                                    routeNum = parser.getText();
                                    Log.i(ACTIVITY_NAME, "Route NO: " + routeNum);
                                    myStopNUmber.add(routeNum);
                                    publishProgress(25);
                                }
                                break;

                        }
                    }

                }
            } catch (ProtocolException | MalformedURLException | XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {

            busAdapter.notifyDataSetChanged();

        }
    }


    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.bus_menu_toolbar, m);

        MenuItem searchItem = m.findItem(R.id.action_search);
        SearchView view = (SearchView) searchItem.getActionView();
        final TextView user_input = view.findViewById(R.id.searchEditText);
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //String[] columns = {BusDatabaseHelper.KEY_ID, BusDatabaseHelper.KEY_MESSAGE};

                //cv.put(BusDatabaseHelper.KEY_MESSAGE, user_input.getText().toString());
                //db.insert(BusDatabaseHelper.TABLE_NAME, "ReplacementValue", cv);
               // results = db.query(BusDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

                //myStopNUmber.add(user_input.getText().toString());


                //busAdapter.notifyDataSetChanged();

              // user_input.setText("");


                Intent intent = new Intent(ctx, BusStopActivity.class);
                intent.putExtra("User input", user_input.getText().toString());
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Search information", Toast.LENGTH_LONG).show();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        int id;
        id = mi.getItemId();

        switch (id) {
            case R.id.action_search:
                user_input = searchEdt.getText().toString();
                String[] columns = {BusDatabaseHelper.KEY_ID, BusDatabaseHelper.KEY_MESSAGE};

                cv.put(BusDatabaseHelper.KEY_MESSAGE, user_input);
                db.insert(BusDatabaseHelper.TABLE_NAME, "ReplacementValue", cv);
                results = db.query(BusDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);


                currentMessage = "You just search for: " +  user_input;

                myStopNUmber.add(user_input);



                busAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "You just add new bus number", Toast.LENGTH_LONG).show();


                break;
            case R.id.action_one:
                Snackbar.make(findViewById(R.id.action_one), currentMessage, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.about:
                AlertDialog alertDialog = new AlertDialog.Builder(BusActivity.this).create();
                alertDialog.setTitle("About");
                alertDialog.setMessage("Author: Kim Ngan Dang \n" +
                        "Activity version name: OcTranspo App \n" +
                        "Intruction: \n " +
                        "User click and search for a bus stop number. From there, you can see the list of bus number. Click on 1 of them, you can see the information of that bus");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            case R.id.action_two:
                Intent intent = new Intent(ctx, MovieActivity.class);
                startActivity(intent);
                break;
            case R.id.action_three:
                Intent intent1 = new Intent(ctx, CBCActivity.class);
                startActivity(intent1);
                break;
            case R.id.action_four:
                Intent intent2 = new Intent(ctx, FoodActivity.class);
                startActivity(intent2);
        }
        return true;


    }


    private class BusAdapter extends ArrayAdapter<String> {

        public BusAdapter(Context ctx) {
            super(ctx, 0);
        }


        public int getCount() {
            return myStopNUmber.size();
        }

        public String getItem(int position) {
            return myStopNUmber.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = BusActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.route_row, null);

            TextView stopnumber = result.findViewById(R.id.route_list);


            stopnumber.setText(getItem(position));


            return result;
        }


    }


}

