package com.basoft.braingame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class NumberGuessingGameActivity extends AppCompatActivity implements NumberGuessingGameObserver {

    private TypeWriter typeWriter;
    private TextView scoreTextView;
    private EditText userGuessEditText;
    private ImageView magicianImageView;
    private AppCompatButton actionButton;
    private ImageButton backButton;
    private NumberGuessingGame game = new NumberGuessingGame(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_guessing_game);
        findViews();
        typeWriter.animateText(getString(R.string.welcome_to_number_guessing_game));
        setViewListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveHighestScore();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @SuppressLint("ApplySharedPref")
    public void saveHighestScore() {
        SharedPreferences preferences = getSharedPreferences(DataStore.HIGH_SCORE_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(DataStore.NUMBER_GUESSING_HIGHEST_SCORE, DataStore.numberGuessingGameHighestScore);
        editor.commit();
    }

    private void setViewListeners() {
        actionButton.setOnClickListener(v -> game.start());
        backButton.setOnClickListener(v -> goBack());
        userGuessEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) ((EditText) v).setText("");
        });
    }

    private void goBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NumberGuessingGameActivity.this);

        DialogInterface.OnClickListener cancelListener = (dialog, which) -> {
            // Close the dialog
        };

        DialogInterface.OnClickListener confirmListener = (dialog, which) -> {
            saveHighestScore();
            NumberGuessingGameActivity.this.finish();
        };

        builder.setIcon(R.mipmap.ic_launcher)
                .setMessage(R.string.are_you_sure_your_highest_score_will_be_saved)
                .setTitle(R.string.exit_game)
                .setNegativeButton(R.string.no, cancelListener)
                .setPositiveButton(R.string.yes, confirmListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void findViews() {
        backButton = findViewById(R.id.number_guessing_game_back_button);
        typeWriter = findViewById(R.id.number_guessing_game_text);
        scoreTextView = findViewById(R.id.number_guessing_game_current_score);
        magicianImageView = findViewById(R.id.number_guessing_game_magician);
        actionButton = findViewById(R.id.number_guessing_game_start_button);
        userGuessEditText = findViewById(R.id.number_guessing_game_number_edit_text);
    }

    @Override
    public void say(String speech) {
        typeWriter.animateText(speech);
    }

    @Override
    public void setMagicianImage(int resourceId) {
        magicianImageView.setImageResource(resourceId);
    }

    @Override
    public void giveQuestion(String question) {
        magicianImageView.setImageResource(R.drawable.magician_question);
        typeWriter.animateText(question);
        userGuessEditText.setText(R.string.enter_your_answer);
        actionButton.setText(R.string._continue);
        actionButton.setOnClickListener(v -> game.continueGame());
    }

    @Override
    public void getUserGuessingNumber(String speech) {
        magicianImageView.setImageResource(R.drawable.magician_question);
        typeWriter.animateText(speech);
        userGuessEditText.setVisibility(View.VISIBLE);
        actionButton.setText(R.string.submit);
        actionButton.setOnClickListener(v -> {
            try {
                game.submit(Integer.parseInt(userGuessEditText.getText().toString()));
            } catch (NumberFormatException exception) {
                Toast.makeText(this, getString(R.string.please_enter_an_integer), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void giveCongratulation(String speech) {
        magicianImageView.setImageResource(R.drawable.magician_happy);
        typeWriter.animateText(speech);
        userGuessEditText.setVisibility(View.GONE);
        actionButton.setText(R.string._continue);
        actionButton.setOnClickListener(v -> game.initNewRound());
    }

    @Override
    public void gameOver(String speech) {
        magicianImageView.setImageResource(R.drawable.magician_worry);
        typeWriter.animateText(speech);
        userGuessEditText.setVisibility(View.GONE);
        actionButton.setText(R.string._continue);
        actionButton.setOnClickListener(v -> startGameResultActivity());
    }

    @Override
    public void updateScore(long score) {
        scoreTextView.setText(String.valueOf(score));
    }

    private void startGameResultActivity() {
        String info = getString(R.string.congratulation_this_is_a_new_record);
        if (!game.breakedScoreRecord())
            info = String.format(Locale.getDefault(),
                    getString(R.string.let_s_try_again_current_highest_score_is) + " %d!",
                    game.getOldHighScore()
            );
        Intent resultActivity = new Intent(this, GameResultActivity.class);
        resultActivity.putExtra(GameResultActivity.SCORE, game.getScore());
        resultActivity.putExtra(GameResultActivity.INFO, info);
        resultActivity.putExtra(GameResultActivity.GAME, GameResultActivity.NUMBER_GUESSING);
        startActivity(resultActivity);
        finish();
    }
}