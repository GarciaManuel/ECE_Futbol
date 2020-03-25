package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }
}
