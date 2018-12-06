package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Author : ViPham
 * Class description: this is a fragment to show movie information after user click on the favourite list
 * Class variable:   protected TextView movie_Title_textView,movie_Year_textView, movie_rating_textView,movie_runtime_textView,movie_mainActors_textView, movie_plot_textView;
 protected Button deleteButton; String movieTitle="";String movieYear="";String movieRating="";String movieRuntime="";String movieActor="";String moviePlot="";
 */
public class MovieDetailFragment extends Fragment {
    protected TextView movie_Title_textView,movie_Year_textView, movie_rating_textView,movie_runtime_textView,movie_mainActors_textView, movie_plot_textView;
    protected Button deleteButton;
    Long id;

    private String movieTitle="";
    private String movieYear="";
    private String movieRating="";
    private String movieRuntime="";
    private String movieActor="";
    private String moviePlot="";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle infoToPass = getArguments();
        id=infoToPass.getLong("ID");
        movieTitle = (String)infoToPass.getString("title");
        movieYear=infoToPass.getString("year");
        movieRating=infoToPass.getString("rating");
        movieRuntime=infoToPass.getString("run_time");
        moviePlot=infoToPass.getString("plot");
        movieActor=infoToPass.getString("actors");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View screen = inflater.inflate(R.layout.movie_layout,null);

        movie_Title_textView = screen.findViewById(R.id.movie_fav_Title_textView);
        movie_Title_textView.setText("Title :" +movieTitle);

        movie_Year_textView=screen.findViewById(R.id.movie_fav_Year_textView);
        movie_Year_textView.setText("Year :"+movieYear);

        movie_rating_textView = screen.findViewById(R.id.movie_fav_Rating_textView);
        movie_rating_textView.setText("Title :" +movieRating);

        movie_runtime_textView=screen.findViewById(R.id.movie_fav_Runtime_textView);
        movie_runtime_textView.setText("Year :"+movieRuntime);

        movie_mainActors_textView = screen.findViewById(R.id.movie_fav_MainActors_textView);
        movie_mainActors_textView.setText("Title :" +movieActor);

        movie_plot_textView=screen.findViewById(R.id.movie_fav_plot_textView);
        movie_plot_textView.setText("Year :"+moviePlot);

        deleteButton=screen.findViewById(R.id.delete_Button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent delIntent = new Intent();
                delIntent.putExtra("ID", id);
                getActivity().setResult(Activity.RESULT_OK, delIntent);
                getActivity().finish();
            }
        });
        return screen;
    }
}
