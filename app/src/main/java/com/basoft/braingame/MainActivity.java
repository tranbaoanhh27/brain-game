package com.basoft.braingame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private CardView calculationGameCard;
    private TextView calculationHighScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setupNavigationRoutes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHighScoresFromSharedPreferences();
    }

    private void loadHighScoresFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(DataStore.HIGH_SCORE_PREFS, MODE_PRIVATE);
        DataStore.calculationGameHighestScore = preferences.getLong(DataStore.CALCULATION_HIGHEST_SCORE, 0);
        calculationHighScoreTextView.setText(String.valueOf(DataStore.calculationGameHighestScore));
    }

    private void setupNavigationRoutes() {
        calculationGameCard.setOnClickListener(v -> startCalculationGameActivity());
    }

    private void startCalculationGameActivity() {
        Intent calculationGame = new Intent(MainActivity.this, CalculationGameActivity.class);
        startActivity(calculationGame);
    }

    private void findViews() {
        calculationGameCard = findViewById(R.id.calculation_card);
        calculationHighScoreTextView = findViewById(R.id.calculation_high_score);
    }
}