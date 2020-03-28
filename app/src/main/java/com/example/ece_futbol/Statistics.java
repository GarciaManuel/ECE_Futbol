package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class Statistics extends AppCompatActivity implements TeamsFragment.OnFragmentInteractionListener, PlayersFragment.OnFragmentInteractionListener {
    private  static final String STATE_CHOSEN = "CHOSEN";

    String currentMatch;
    boolean localStorage;
    boolean chosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        currentMatch = "0";
        chosen = false;

        if (savedInstanceState != null) {
            chosen = savedInstanceState.getBoolean(STATE_CHOSEN, true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentMatch = Long.toString(extras.getLong("currentMatch"));
            localStorage = extras.getBoolean("localStorage");
        }

        if (!chosen) {
            TeamsFragment teamsFragment = TeamsFragment.newInstance(currentMatch, localStorage);
            FragmentTransaction fgt = getSupportFragmentManager().beginTransaction();
            fgt.add(R.id.statsFragment, teamsFragment).commit();
        }

    }

    public void onFragmentChoose(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            return;
        }
        FragmentTransaction fgt = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.teamsRadio:
                chosen = true;
                TeamsFragment teamsFragment = TeamsFragment.newInstance(currentMatch, localStorage);
                fgt.replace(R.id.statsFragment, teamsFragment).commit();
                break;
            case R.id.playersRadio:
                chosen = true;
                PlayersFragment playersFragment = PlayersFragment.newInstance(currentMatch, localStorage);
                fgt.replace(R.id.statsFragment, playersFragment).commit();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_CHOSEN, chosen);
    }
}
