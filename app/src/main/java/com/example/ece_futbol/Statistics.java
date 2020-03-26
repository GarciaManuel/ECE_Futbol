package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class Statistics extends AppCompatActivity implements TeamsFragment.OnFragmentInteractionListener, PlayersFragment.OnFragmentInteractionListener {

    String currentMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        currentMatch = "0";

        TeamsFragment teamsFragment = TeamsFragment.newInstance(currentMatch);
        FragmentTransaction fgt = getSupportFragmentManager().beginTransaction();
        fgt.add(R.id.statsFragment, teamsFragment).commit();
    }

    public void onFragmentChoose(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            return;
        }
        FragmentTransaction fgt = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.teamsRadio:
                TeamsFragment teamsFragment = TeamsFragment.newInstance(currentMatch);
                fgt.replace(R.id.statsFragment, teamsFragment).commit();
                break;
            case R.id.playersRadio:
                PlayersFragment playersFragment = PlayersFragment.newInstance(currentMatch);
                fgt.replace(R.id.statsFragment, playersFragment).commit();
                break;
        }

    }

}
