package com.example.vipha.finalproject_cst2335;
/**
 * This activity displays the Route Number, Direction, and Heading direction which get the information from the URL
 **/


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


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


public class BusStopActivity extends AppCompatActivity {


    private static final String ACTIVITY_NAME = "BusStopActivity";
    private ListView busListVIew;


    private static final String BusURL =
            "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=";
    private List<BusRoute> list;
    private MyBusAdapter mybusAdapter;
    private ContentValues cv;


    BusDatabaseHelper dbOpener;
    SQLiteDatabase db;


    public ProgressBar progressBar;


    private String user_input;

    private boolean isTablet;
    private String routeNum;
    public ArrayList<String> dirArr;
    public ArrayList<String> headingdirArr;
    public FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        setTitle("OCTranspo Bus Route");
        Log.i(ACTIVITY_NAME, "In onCreate()");

        dirArr = new ArrayList<>();
        headingdirArr = new ArrayList<>();
        busListVIew = findViewById(R.id.stopNo);

        progressBar = findViewById(R.id.progressBar);


        cv = new ContentValues();

        list = new ArrayList<>();
        //open data
        dbOpener = new BusDatabaseHelper(this);//helper object
        db = dbOpener.getWritableDatabase();

        mybusAdapter = new MyBusAdapter(this);
        busListVIew.setAdapter(mybusAdapter);
        mybusAdapter.notifyDataSetChanged();

        frameLayout = findViewById(R.id.fragment_location);

        user_input = getIntent().getExtras().getString("StopNumber");

        Log.i(ACTIVITY_NAME, "The user input" + user_input);
        new RouteQuery().execute();


        busListVIew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle infotoPass = new Bundle();
                infotoPass.putString("stopNumber", user_input);
                infotoPass.putString("routeNum", routeNum);
                infotoPass.putString("direction", dirArr.get(i));
                infotoPass.putString("headingDir", headingdirArr.get(i));
                infotoPass.putBoolean("isTablet", isTablet);
                InfoFragmentActivity newFragment = new InfoFragmentActivity();
                newFragment.iAmTablet = true;


                if (isTablet) {


                    newFragment.setArguments(infotoPass); //give information to bundle

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ftrans = fm.beginTransaction();
                    ftrans.replace(R.id.fragment_location, newFragment); //load a fragment into the framelayout
                    ftrans.addToBackStack(""); //changes the back button behaviour
                    ftrans.commit(); //actually load it

                } else //on a phone
                {

                    //go to new window:
                    Intent nextPage = new Intent(BusStopActivity.this, BusDetailActivity.class);
                    nextPage.putExtras(infotoPass); //send info
                    startActivityForResult(nextPage, 67);
                }
            }
        });


    }

    private class RouteQuery extends AsyncTask<String, Integer, String> {
        HttpURLConnection conn;
        public XmlPullParser parser;
        private String routeNum = "";
        private String directionID = "";
        private String direction = "";
        private String routeHeading = "";

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
                            case "RouteNo":
                                //routeNum = parser.getAttributeValue(null, "RouteNo");
                                if (parser.next() == XmlPullParser.TEXT) {
                                    routeNum = parser.getText();
                                    Log.i(ACTIVITY_NAME, "Route NO: " + routeNum);
                                    publishProgress(25);
                                }
                                break;
                            case "DirectionID":
                                //directionID = parser.getAttributeValue(null, "DirectionID");
                                if (parser.next() == XmlPullParser.TEXT) {
                                    directionID = parser.getText();
                                    Log.i(ACTIVITY_NAME, "Direction ID: " + directionID);
                                }
                                publishProgress(50);
                                break;
                            case "Direction":
                                // direction = parser.getAttributeValue(null, "Direction");
                                if (parser.next() == XmlPullParser.TEXT) {
                                    direction = parser.getText();
                                    Log.i(ACTIVITY_NAME, "Direction: " + direction);
                                }
                                publishProgress(75);
                                break;
                            case "RouteHeading":
                                //routeHeading = parser.getAttributeValue(null, "RouteHeading");
                                if (parser.next() == XmlPullParser.TEXT) {
                                    routeHeading = parser.getText();
                                    Log.i(ACTIVITY_NAME, "Route heading: " + routeHeading);
                                }
                                publishProgress(100);
                                break;
                        }
                    }
                    if (parser.getEventType() == XmlPullParser.END_TAG) {
                        String endtag = parser.getName();
                        if (endtag.equals("Route")) {
                            Log.i(ACTIVITY_NAME, "Record added success");
                            list.add(new BusRoute(routeNum, directionID, direction, routeHeading));
                            dirArr.add(direction);
                            headingdirArr.add(routeHeading);
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
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            mybusAdapter.notifyDataSetChanged();

        }
    }


    private class MyBusAdapter extends ArrayAdapter<BusRoute> {

        public MyBusAdapter(Context ctx) {
            super(ctx, 0);
        }


        public int getCount() {
            return list.size();
        }

        public BusRoute getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = BusStopActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.bus_stop_row, null);


            TextView routeNumber = result.findViewById(R.id.routeNumber);


            TextView dir = result.findViewById(R.id.direction);


            TextView dirHeading = result.findViewById(R.id.dirHeading);

            Log.i(ACTIVITY_NAME, "I am in the getView ");

            routeNumber.setText("Route Number: " + getItem(position).getRouteNum());

            dir.setText("Direction: " + getItem(position).getDirection());
            dirHeading.setText("Route Heading: " + getItem(position).getdirHeading());

            return result;
        }


    }
}
