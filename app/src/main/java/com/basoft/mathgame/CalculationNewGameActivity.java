package com.basoft.mathgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class CalculationNewGameActivity extends AppCompatActivity implements CalculationGameObserver {

    private TextView highestScoreTextView, playerScoreTextView, playerLifeTextView, remainingTimeTextView;
    private TextView problemTextView, answerTextView;
    private AppCompatButton button1, button2, button3, button4, button5, button6, button7;
    private AppCompatButton button8, button9, button0, submitButton, minusButton;
    private AppCompatImageButton backspaceButton;
    private CalculationGame game;
    private long highestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_new_game);
        findViews();
        setViewContents();
        setViewListeners();
        game = new CalculationGame(CalculationNewGameActivity.this, CalculationNewGameActivity.this);
        game.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(MainActivity.CALCULATION_HIGHEST_SCORE, highestScore);
        editor.apply();
    }

    private void setViewContents() {
        Intent originalIntent = getIntent();
        highestScore = originalIntent.getLongExtra(MainActivity.CALCULATION_HIGHEST_SCORE, 0);
        highestScoreTextView.setText(String.valueOf(highestScore));
    }

    private void setViewListeners() {
        View.OnClickListener numberButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ((AppCompatButton) v).getText().toString();
                String currentAnswer = answerTextView.getText().toString();
                if (currentAnswer.equals("?")) currentAnswer = "";
                answerTextView.setText(currentAnswer.concat(number));
            }
        };

        button1.setOnClickListener(numberButtonClickListener);
        button2.setOnClickListener(numberButtonClickListener);
        button3.setOnClickListener(numberButtonClickListener);
        button4.setOnClickListener(numberButtonClickListener);
        button5.setOnClickListener(numberButtonClickListener);
        button6.setOnClickListener(numberButtonClickListener);
        button7.setOnClickListener(numberButtonClickListener);
        button8.setOnClickListener(numberButtonClickListener);
        button9.setOnClickListener(numberButtonClickListener);
        button0.setOnClickListener(numberButtonClickListener);
        minusButton.setOnClickListener(v -> inputMinusSign());
        backspaceButton.setOnClickListener(v -> backspace());
        submitButton.setOnClickListener(v -> submitAnswer());
    }

    private void inputMinusSign() {
        String currentAnswer = answerTextView.getText().toString();
        if (currentAnswer.equals("?")) currentAnswer = "";
        answerTextView.setText(currentAnswer.concat("-"));
    }

    private void submitAnswer() {
        String answerString = answerTextView.getText().toString();
        game.checkAnswer(answerString);
    }

    private void backspace() {
        String currentText = answerTextView.getText().toString();
        int size = currentText.length();
        if (currentText.length() > 0) answerTextView.setText(currentText.substring(0, size - 1));
    }

    private void findViews() {
        highestScoreTextView = findViewById(R.id.calculation_new_game_highest_score_text);
        playerScoreTextView = findViewById(R.id.calculation_new_game_player_score_text);
        playerLifeTextView = findViewById(R.id.calculation_new_game_player_life_text);
        remainingTimeTextView = findViewById(R.id.calculation_new_game_count_down);
        problemTextView = findViewById(R.id.calculation_new_game_problem);
        answerTextView = findViewById(R.id.calculation_new_game_user_answer);
        button1 = findViewById(R.id.calculation_new_game_1_button);
        button2 = findViewById(R.id.calculation_new_game_2_button);
        button3 = findViewById(R.id.calculation_new_game_3_button);
        button4 = findViewById(R.id.calculation_new_game_4_button);
        button5 = findViewById(R.id.calculation_new_game_5_button);
        button6 = findViewById(R.id.calculation_new_game_6_button);
        button7 = findViewById(R.id.calculation_new_game_7_button);
        button8 = findViewById(R.id.calculation_new_game_8_button);
        button9 = findViewById(R.id.calculation_new_game_9_button);
        button0 = findViewById(R.id.calculation_new_game_0_button);
        minusButton = findViewById(R.id.calculation_new_game_minus_button);
        backspaceButton = findViewById(R.id.calculation_new_game_backspace_button);
        submitButton = findViewById(R.id.calculation_new_game_submit_button);
    }

    @Override
    public void updateHighestScore(long score) {
        if (score > highestScore) highestScore = score;
    }

    @Override
    public void updatePlayerScore(long score) {
        playerScoreTextView.setText(String.format(Locale.getDefault(), "%d", score));
    }

    @Override
    public void updatePlayerLife(int life) {
        playerLifeTextView.setText(String.format(Locale.getDefault(), "%d", life));
    }

    @Override
    public void updateRemainingTime(long millisUntilEnd) {
        remainingTimeTextView.setText(String.format(Locale.getDefault(), "%d", (millisUntilEnd / 1000 + 1)));
    }

    @Override
    public void updateExpression(String expression) {
        problemTextView.setText(expression);
        answerTextView.setText("?");
        answerTextView.setBackgroundResource(R.drawable.rounded_yellow_input_box);
    }

    @Override
    public void updateCorrectAnswer(long answer, AnswerStatus status) {
        String correctString = "";
        if (status == AnswerStatus.WRONG) {
            answerTextView.setBackgroundResource(R.drawable.rounded_red_input_box);
            correctString = String.format(Locale.getDefault(), "(%s) %d", answerTextView.getText().toString(), answer);
        }
        else if (status == AnswerStatus.CORRECT) {
            answerTextView.setBackgroundResource(R.drawable.rounded_green_input_box);
            correctString = String.format(Locale.getDefault(), "%d", answer);
        }
        answerTextView.setText(correctString);
    }

    @Override
    public String getCurrentUserAnswer() {
        return answerTextView.getText().toString();
    }

    @Override
    public void freezeInput() {
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
        button4.setClickable(false);
        button5.setClickable(false);
        button6.setClickable(false);
        button7.setClickable(false);
        button8.setClickable(false);
        button9.setClickable(false);
        button0.setClickable(false);
        minusButton.setClickable(false);
        submitButton.setClickable(false);
        backspaceButton.setClickable(false);
    }

    @Override
    public void unfreezeInput() {
        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);
        button4.setClickable(true);
        button5.setClickable(true);
        button6.setClickable(true);
        button7.setClickable(true);
        button8.setClickable(true);
        button9.setClickable(true);
        button0.setClickable(true);
        minusButton.setClickable(true);
        submitButton.setClickable(true);
        backspaceButton.setClickable(true);
    }
}