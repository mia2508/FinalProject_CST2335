package com.example.vipha.finalproject_cst2335;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**Author: Vi Pham
 *Student number: 040886894
 *Class Description: MovieAdapter is extend from BaseAdapter to keep track of the movieArrayList
 *class member: Context context, ArrayList<String> movieArrayLis
 *class method:
 */
public  class MovieAdapter extends BaseAdapter {
         private Context context; //context
    private ArrayList<String> movieArrayList; //data source of the list adapter

    //public constructor
    public MovieAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.movieArrayList = items;
    }

    @Override
    public int getCount() {
        return movieArrayList.size(); //returns total of items in the list
    }


    @Override
    public Object getItem(int position) {
        return movieArrayList.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.movie_list_view, parent, false);
        }

        // get current item to be displayed
        String currentMovie = (String) getItem(position);

        // get the TextView for item name and item description
        TextView textViewMovieName = (TextView)
                convertView.findViewById(R.id.movie_title_fragment_textView);

        //sets the text for item name and item description from the current item object
        textViewMovieName.setText(currentMovie);

        // returns the view for the current row
        return convertView;
    }
/**
 https://stackoverflow.com/questions/5234576/what-adapter-shall-i-use-to-use-hashmap-in-a-listview
 */
//private final ArrayList mData;
//
//    public MovieAdapter(Map<String, String> map) {
//        mData = new ArrayList();
//        mData.addAll(map.entrySet());
//    }
//
//    @Override
//    public int getCount() {
//        return mData.size();
//    }
//
//    @Override
//    public Map.Entry<String, String> getItem(int position) {
//        return (Map.Entry) mData.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final View result;
//
//        if (convertView == null) {
//            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_view, parent, false);
//        } else {
//            result = convertView;
//        }
//
//        Map.Entry<String, String> item = getItem(position);
////         get the TextView for item name and item description
//        TextView textViewMovieName = (TextView) result.findViewById(R.id.movie_title_fragment_textView);
//
//        //sets the text for item name and item description from the current item object
//        textViewMovieName.setText(mData.get(position));
//        // TODO replace findViewById by ViewHolder
//        ((TextView) result.findViewById(R.id.movie_title_fragment_textView)).setText(item.get("movieTitle");
//
//        return result;
//    }
//}
}