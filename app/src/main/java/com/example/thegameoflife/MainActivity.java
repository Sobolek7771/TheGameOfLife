package com.example.thegameoflife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GameOfLifeView gameOfLifeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameOfLifeView = (GameOfLifeView) findViewById(R.id.game_of_life);


        // Кнопка play/pause
        ImageButton playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOfLifeView.isRunning) {
                    gameOfLifeView.start();
                    playButton.setImageResource(R.drawable.ic_pause);
                } else {
                    gameOfLifeView.stop();
                    playButton.setImageResource(R.drawable.ic_play);
                }
            }
        });

        // Кнопка refreshButton
        ImageButton refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOfLifeView.initWorld();
            }
        });
    }


}