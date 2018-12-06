package com.example.vipha.finalproject_cst2335;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.transition.FragmentTransitionSupport;
import android.support.v7.app.AppCompatActivity;

/**
 * This class is used to transition to the fragment of NewsFragment class after the user clicks
 * on a new item in the newsListVIew from CBCActivity
 */

public class ArticleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbc_details);

        Bundle bundle = getIntent().getBundleExtra("News Item");
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.articleFrame, newsFragment).commit();


    }


}
