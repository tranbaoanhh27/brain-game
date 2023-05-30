package com.basoft.braingame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NumberGuessingGameActivity extends AppCompatActivity implements NumberGuessingGameObserver {

    private TypeWriter typeWriter;
    private ImageView magicianImageView;
    private AppCompatButton startButton;
    private NumberGuessingGame game = new NumberGuessingGame(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_guessing_game);
        findViews();
        typeWriter.animateText("Welcome to Number Guessing Game! If you are ready, press the below START button!");
        setViewListeners();
    }

    private void setViewListeners() {
        startButton.setOnClickListener(startListener);
    }

    View.OnClickListener startListener = v -> {
        startButton.setVisibility(View.GONE);
        game.start();
    };

    private void findViews() {
        typeWriter = findViewById(R.id.number_guessing_game_text);
        magicianImageView = findViewById(R.id.number_guessing_game_magician);
        startButton = findViewById(R.id.number_guessing_game_start_button);
    }

    @Override
    public void say(String speech) {
        typeWriter.animateText(speech);
    }

    @Override
    public void setMagicianImage(int resourceId) {
        magicianImageView.setImageResource(resourceId);
    }
}