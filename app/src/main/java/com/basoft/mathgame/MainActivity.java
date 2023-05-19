package com.basoft.mathgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String HIGH_SCORE_PREFS = "ba_soft_high_score";
    public static final String CALCULATION_HIGHEST_SCORE = "calculation_highest_score";
    private CardView calculationGameCard;
    private TextView calculationHighScoreTextView;
    private long calculationHighestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setupNavigationRoutes();
        setupHighestScores();
    }

    private void setupHighestScores() {
        SharedPreferences preferences = getSharedPreferences(HIGH_SCORE_PREFS, MODE_PRIVATE);
        calculationHighestScore = preferences.getLong(CALCULATION_HIGHEST_SCORE, 0);
        calculationHighScoreTextView.setText(String.valueOf(calculationHighestScore));
    }

    private void setupNavigationRoutes() {
        calculationGameCard.setOnClickListener(v -> startCalculationGameActivity());
    }

    private void startCalculationGameActivity() {
        Intent calculationGame = new Intent(MainActivity.this, CalculationGameActivity.class);
        calculationGame.putExtra(CALCULATION_HIGHEST_SCORE, calculationHighestScore);
        startActivity(calculationGame);
    }

    private void findViews() {
        calculationGameCard = findViewById(R.id.calculation_card);
        calculationHighScoreTextView = findViewById(R.id.calculation_high_score);
    }
}