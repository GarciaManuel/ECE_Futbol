package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class Statistics extends AppCompatActivity implements TeamsFragment.OnFragmentInteractionListener {

    String currentMatch;
    private TeamsFragment teamsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        currentMatch = "0";

        teamsFragment = TeamsFragment.newInstance(currentMatch);
        FragmentTransaction fgt = getSupportFragmentManager().beginTransaction();
//        fgt.addToBackStack("new fragment");
        fgt.add(R.id.teamsFragment, teamsFragment).commit();

    }
}
