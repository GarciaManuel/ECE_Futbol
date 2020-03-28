package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    private  static final String STATE_SELECTED_MATCH = "selectedMatch";
    private  static final String STATE_MATCH_POSITION = "matchPosition";
    private  static final String STATE_LOCAL_STORAGE = "localStorage";

    private DatabaseHelper db;
    private MatchServer matchServer;

    private long matchGamesId[];
    private int selectedMatch;
    private long matchPosition;
    private boolean localStorage;

    private List<String> matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        if (savedInstanceState != null) {
            selectedMatch = savedInstanceState.getInt(STATE_SELECTED_MATCH, 0);
            matchPosition = savedInstanceState.getLong(STATE_MATCH_POSITION, 0);
            localStorage = savedInstanceState.getBoolean(STATE_LOCAL_STORAGE, false);
        }

        Button maps = findViewById(R.id.matchMap);
        maps.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mapsActivity = new Intent(MainActivity.this, Maps.class);
                        mapsActivity.putStringArrayListExtra("matches", (ArrayList<String>) matches);
                        startActivity(mapsActivity);
                    }
                });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent registerActivity = new Intent(MainActivity.this, Register.class);
                        registerActivity.putExtra("currentMatch", matchPosition);
                        registerActivity.putExtra("localStorage", localStorage);
                        startActivity(registerActivity);
                    }
                });

        Button stats = findViewById(R.id.stats);
        stats.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent statsActivity = new Intent(MainActivity.this, Statistics.class);
                        statsActivity.putExtra("currentMatch", matchPosition);
                        statsActivity.putExtra("localStorage", localStorage);
                        startActivity(statsActivity);
                    }
                });

        initDatabase();
        initMatchServer("", "", "", "allMatches", Boolean.toString(false));

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, matches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedMatch);
        spinner.setOnItemSelectedListener(this);
    }

    private void initMatchServer(String...inputString) {
            matchServer = new MatchServer();
            matchServer.execute(inputString);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_MATCH, selectedMatch);
        outState.putLong(STATE_MATCH_POSITION, matchPosition);
        outState.putBoolean(STATE_LOCAL_STORAGE, localStorage);

        db.closeDB();
    }
    // <----------- LocalStorage functions ----------->
    private void initDatabase() {
        db.resetDatabase();

        matchGamesId = new long [5];

        String schools[] = new String[]{"TEC", "Harvard", "CUHK", "LaSalle", "Brown", "Stanford", "ECE", "UCLA", "UDEM", "Yale"};
        String persons[] = new String[]{"Andrea", "Rebeca","Jenna", "James","Lucy", "Javier", "Laura", "Barbara",
                "Paulina", "Carolina", "Atheena", "James","Renato", "Bruno", "Shai", "Eli","Simone", "Cristy",
                "Sofia", "Lazaro"};

        for (int i = 0, j = 5, z = 0; i < 10; i += 2, j++, z++) {
            MatchGame mg = new MatchGame(j, schools[i], schools[i+1]);
            matchGamesId[z] = db.createMatchGame(mg);
        }

        for (int i = 0, j = 0; j < 10; i +=2, j++) {
            Team t = new Team(schools[j], persons[i], persons[i+1]);
            db.createTeam(t);
        }

        for (String per : persons) {
            Player p = new Player(per);
            db.createPlayer(p);
        }

        matches = db.getAllMatches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedMatch = position;
        if (selectedMatch > 4) {
            matchPosition = selectedMatch - 5;
            localStorage = false;
        } else {
            matchPosition = matchGamesId[selectedMatch];
            localStorage = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateMatches(String[] values) {
        matches.addAll(Arrays.asList(values));
    }

    // <----------- ExternalStorage Server functions ----------->
    private  class MatchServer extends AsyncTask<String, Void, Void> {
        private String[] values;
        @Override
        protected Void doInBackground(String...inputStrings) {
            getAttribute('a', 0, inputStrings[3], inputStrings);
            return null;
        }

        private void getAttribute(char objectType, int matchNum, String attribute, String[] inputStrings) {
            try {
                Socket s = new Socket("10.0.2.2", 9876);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                DataInputStream dis = new DataInputStream((s.getInputStream()));
                OutputStream out = s.getOutputStream();
                dos.writeChar(objectType);
                dos.writeInt(matchNum);
                dos.writeUTF(attribute);
                dos.writeBoolean(false);
                if (attribute.equals("allMatches")) {
                    values = new String[]{dis.readUTF(), dis.readUTF(),dis.readUTF(),dis.readUTF(),dis.readUTF()};
                }
                out.close();
                s.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            if (values != null) {
                updateMatches(values );
            }
        }
    }
}
