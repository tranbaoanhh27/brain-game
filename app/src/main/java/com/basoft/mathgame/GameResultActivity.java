package com.basoft.mathgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class GameResultActivity extends AppCompatActivity {

    private TextView scoreTextView, infoTextView;
    private AppCompatButton mainMenuButton, playAgainButton;
    private long score;
    private String info;
    private String game;
    public static final String SCORE = "score";
    public static final String INFO = "info";
    public static final String GAME = "game";
    public static final String CALCULATION = "calculation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        findViews();
        getExtraFromIntent();
        setViewContents();
        setViewListeners();
    }

    private void setViewListeners() {
        mainMenuButton.setOnClickListener(v -> GameResultActivity.this.finish());
        playAgainButton.setOnClickListener(v -> startNewGame());
    }

    private void startNewGame() {
        Intent newGame = null;
        if (game.equals(CALCULATION)) {
            newGame = new Intent(GameResultActivity.this, CalculationGameActivity.class);

        }
        if (newGame != null) startActivity(newGame);
        GameResultActivity.this.finish();
    }

    private void setViewContents() {
        scoreTextView.setText(String.valueOf(score));
        infoTextView.setText(info);
    }

    private void getExtraFromIntent() {
        Intent intent = getIntent();
        score = intent.getLongExtra(SCORE, 0);
        info = intent.getStringExtra(INFO);
        game = intent.getStringExtra(GAME);
    }

    private void findViews() {
        scoreTextView = findViewById(R.id.game_result_your_score);
        infoTextView = findViewById(R.id.game_result_more_info);
        mainMenuButton = findViewById(R.id.game_result_main_menu);
        playAgainButton = findViewById(R.id.game_result_play_again);
    }
}