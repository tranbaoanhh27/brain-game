package com.basoft.mathgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Locale;

public class CalculationGameActivity extends AppCompatActivity {
    private ImageButton backButton;
    private AppCompatButton startButton;
    private TextView areYouReadyTextView, countDownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_game);
        findViews();
        setupViewListeners();
    }

    private void setupViewListeners() {
        backButton.setOnClickListener(v -> finish());
        startButton.setOnClickListener(v -> startCountdownToGame());
    }

    private void startCountdownToGame() {
        areYouReadyTextView.setVisibility(View.GONE);
        startButton.setVisibility(View.GONE);
        countDownTextView.setVisibility(View.VISIBLE);

        CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTextView.setText(String.format(
                        Locale.getDefault(),"%d", (millisUntilFinished / 1000 + 1)
                ));
            }

            @Override
            public void onFinish() {
                startCalculationNewGameActivity();
            }
        };

        timer.start();
    }

    private void startCalculationNewGameActivity() {
        Intent newCalculationGame = new Intent(CalculationGameActivity.this, CalculationNewGameActivity.class);
        Intent originalIntent = getIntent();
        long highestScore = originalIntent.getLongExtra(MainActivity.CALCULATION_HIGHEST_SCORE, 0);
        newCalculationGame.putExtra(MainActivity.CALCULATION_HIGHEST_SCORE, highestScore);
        startActivity(newCalculationGame);
        CalculationGameActivity.this.finish();
    }

    private void findViews() {
        backButton = findViewById(R.id.calculation_game_back_button);
        startButton = findViewById(R.id.calculation_game_start_button);
        areYouReadyTextView = findViewById(R.id.calculation_game_are_you_ready);
        countDownTextView = findViewById(R.id.calculation_game_countdown_to_start);
    }
}