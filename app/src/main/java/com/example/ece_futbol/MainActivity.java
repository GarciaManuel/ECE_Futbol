package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    long matchGamesId[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        Button photos = findViewById(R.id.takePicture);
        photos.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent phtosActivity = new Intent(MainActivity.this, Photos.class);
                        startActivity(phtosActivity);
                    }
        });

        Button maps = findViewById(R.id.matchMap);
        maps.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mapsActivity = new Intent(MainActivity.this, Maps.class);
                        startActivity(mapsActivity);
                    }
                });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent registerActivity = new Intent(MainActivity.this, Register.class);
                        registerActivity.putExtra("currentMatch", matchGamesId[0]);
                        registerActivity.putExtra("localStorage", "true");
                        startActivity(registerActivity);
                    }
                });

        Button stats = findViewById(R.id.stats);
        stats.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent statsActivity = new Intent(MainActivity.this, Statistics.class);
                        startActivity(statsActivity);
                    }
                });

        initDatabase();

    }

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
//        MatchGame mg1 = new MatchGame(5, "TEC", "Harvard");
//        MatchGame mg2 = new MatchGame(6, "CUHK", "LaSalle");
//        MatchGame mg3 = new MatchGame(7, "Brown", "Stanford");
//        MatchGame mg4 = new MatchGame(8,"ECE", "UCLA");
//        MatchGame mg5 = new MatchGame(9, "UDEM", "Yale");

//        db.createMatchGame(mg1);
//        db.createMatchGame(mg2);
//        db.createMatchGame(mg3);
//        db.createMatchGame(mg4);
//        db.createMatchGame(mg5);

        for (int i = 0, j = 0; j < 10; i +=2, j++) {
            Team t = new Team(schools[j], persons[i], persons[i+1]);
            db.createTeam(t);
        }

        for (String per : persons) {
            Player p = new Player(per);
            db.createPlayer(p);
        }
//        Team t1 = new Team("TEC", "Andrea", "Rebeca");
//        Team t2 = new Team("CUHK", "Jenna", "James");
//        Team t3 = new Team("Brown", "Lucy", "Javier");
//        Team t4 = new Team("ECE", "Laura", "Barbara");
//        Team t5 = new Team("UDEM", "Paulina", "Carolina");
//        Team t6 = new Team("Harvard", "Atheena", "James");
//        Team t7 = new Team("LaSalle", "Renato", "Bruno");
//        Team t8 = new Team("Stanford", "Shai", "Eli");
//        Team t9 = new Team("UCLA", "Simone", "Cristy");
//        Team t10 = new Team("Yale", "Sofia", "Lazaro");

//        db.createTeam(t1);
//        db.createTeam(t2);
//        db.createTeam(t3);
//        db.createTeam(t4);
//        db.createTeam(t5);
//        db.createTeam(t6);
//        db.createTeam(t7);
//        db.createTeam(t8);
//        db.createTeam(t9);
//        db.createTeam(t10);


    }
}
