package com.example.vipha.finalproject_cst2335;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class InfoFragmentActivity extends Fragment {
    public boolean iAmTablet;
    private String routeNumPassed;
    private String direcPassed;
    private String headingDirPassed;
    private String stopNumber;
    private static final String ACTIVITY_NAME = "InfoFragmentActivity";

    ListView listView;
    private List<Trip> list;
    private InfoBusAdapter infoBusAdapter;
    public Context ctx;

    public BusStopActivity parent;

    public InfoFragmentActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View screen = inflater.inflate(R.layout.activity_stop, container, false);
        infoBusAdapter = new InfoBusAdapter(getContext());
        list = new ArrayList<>();
        listView = screen.findViewById(R.id.stopNo);

        Bundle infoToPass = getArguments();
        stopNumber = infoToPass.getString("stopNumber");
        routeNumPassed = infoToPass.getString("routeNum");
        direcPassed = infoToPass.getString("direction");
        headingDirPassed = infoToPass.getString("headingDir");
        iAmTablet = infoToPass.getBoolean("isTablet");
        listView.setAdapter(infoBusAdapter);
        infoBusAdapter.notifyDataSetChanged();
        new InfoBusQuery().execute();

        return screen;

    }


    private class InfoBusQuery extends AsyncTask<String, Integer, String> {

        private XmlPullParser parser;

        @Override
        protected String doInBackground(String... strings) {

            URL url;

            HttpURLConnection conn;
            InputStream resultStream = null;

            String address = "https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=" + stopNumber + "&routeNo=" + routeNumPassed + "";

            try {
                url = new URL(address);
                Log.i("Fragment", "URL: host is " + url.getHost() + "Stop number is :" + stopNumber + " Route Number is " + routeNumPassed);

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                //Start the connection with webserver
                conn.connect();
                //get the response code
                //https://developer.android.com/training/basics/network-ops/connecting.html
                int responseCode = conn.getResponseCode();

                //handle the connection error
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                //get the output stream
                resultStream = conn.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(resultStream, "UTF-8");

                String destination = "", startTime = "", latitude = "", longitude = "", gpsSpeed = "", adjustedTime = "";
                //start read the stream
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {

                        String tag = parser.getName();
                        Log.i(ACTIVITY_NAME, "The tag name is: " + tag);
                        if (tag.equalsIgnoreCase("tripdestination")) {
                            if (parser.next() == XmlPullParser.TEXT) {
                                destination = parser.getText();
                                Log.i(ACTIVITY_NAME, "Tripdes: " + destination);
                            }
                        }
                        if (tag.equalsIgnoreCase("tripstarttime")) {
                            if (parser.next() == XmlPullParser.TEXT) {
                                startTime = parser.getText();
                            }
                        }
                        if (tag.equalsIgnoreCase("adjustedscheduletime")) {
                            if (parser.next() == XmlPullParser.TEXT) {
                                adjustedTime = parser.getText();
                            }
                        }
                        if (tag.equalsIgnoreCase("latitude")) {
                            if (parser.next() == XmlPullParser.TEXT) {
                                latitude = parser.getText();
                            }
                        }
                        if (tag.equalsIgnoreCase("longitude")) {
                            if (parser.next() == XmlPullParser.TEXT) {
                                longitude = parser.getText();
                            }
                        }
                        if (tag.equalsIgnoreCase("gpsspeed")) {
                            if (parser.next() == XmlPullParser.TEXT) {
                                gpsSpeed = parser.getText();
                            }
                        }

                    }
                    if (parser.getEventType() == XmlPullParser.END_TAG) {
                        String endtag = parser.getText();

                        if (endtag.equals("Trip")) {
                            Log.i(ACTIVITY_NAME, "Recordadded !!");
                            list.add(new Trip(destination, startTime, latitude, longitude, gpsSpeed, adjustedTime));

                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                //Log.i(ACTIVITY_NAME,"STREAM reader exception");
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        protected void onPostExecute(String result) {

            infoBusAdapter.notifyDataSetChanged();
            Log.i("activity", "I am in post execute");
        }
    }

    private class InfoBusAdapter extends ArrayAdapter<Trip> {

        public InfoBusAdapter(Context ctx) {

            super(ctx, 0);
        }


        public int getCount() {
            return list.size();
        }

        public Trip getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View result = inflater.inflate(R.layout.fragment_blank, null);

            TextView tripDestv = result.findViewById(R.id.TripDes);
            TextView latitudeTv = result.findViewById(R.id.latitude);
            TextView longtitudeTv = result.findViewById(R.id.longtitude);
            TextView gpsTv = result.findViewById(R.id.gps);
            TextView starTimeTv = result.findViewById(R.id.startTime);
            TextView adjustedTimeTv = result.findViewById(R.id.adjustedTime);

            Log.i("Hello", "I am in the getView ");

            tripDestv.setText("Trip Destination: " + getItem(position).getDestination());
            latitudeTv.setText("Latitude : " + getItem(position).getLatitude());
            longtitudeTv.setText("Longtitude: " + getItem(position).getLongitude());
            gpsTv.setText("GPS : " + getItem(position).getGpsSpeed());
            starTimeTv.setText("Start time: " + getItem(position).getStartTime());
            adjustedTimeTv.setText("Adjusted scheduled time: " + getItem(position).getAdjustedScheduleTime());


            return result;
        }


    }
}