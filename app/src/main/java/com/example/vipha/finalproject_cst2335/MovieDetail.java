package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**Author: Vi Pham
 *Student number: 040886894
 *Class Description: MovieDetail is a fragment manager use to load fragment
 *class member:
 *class method:
 */
public class MovieDetail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Load fragment to phone:
        Bundle infoToPass = getIntent().getExtras();
        MovieDetailFragment messageFragment = new MovieDetailFragment();
        messageFragment.setArguments(infoToPass);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.phoneFrameLayout,messageFragment);
        fragmentTransaction.commit();
    }
}
