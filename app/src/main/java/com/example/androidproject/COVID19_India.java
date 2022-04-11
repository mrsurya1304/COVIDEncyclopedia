package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class COVID19_India extends AppCompatActivity implements View.OnClickListener {

    public CardView c11, c12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_india);

        c11 = (CardView) findViewById(R.id.card11);
        c12 = (CardView) findViewById(R.id.card12);

        c11.setOnClickListener(this);
        c12.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
            case R.id.card11:
                i = new Intent(this, COVID_IndiaTracking.class);
                startActivity(i);
                break;
            case R.id.card12:
                i = new Intent(this, Hotspots.class);
                startActivity(i);
                break;
        }
    }
}