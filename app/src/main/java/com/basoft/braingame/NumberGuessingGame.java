package com.basoft.braingame;

import android.content.Context;

import java.util.Locale;
import java.util.Random;

public class NumberGuessingGame {
    private Context context;
    private NumberGuessingGameObserver observer;
    private int max, number, remainingChanges;
    private long score, oldHighScore;
    private static final Random random = new Random();

    public NumberGuessingGame(Context context, NumberGuessingGameObserver observer) {
        this.observer = observer;
        this.context = context;
        max = 10;
        remainingChanges = 10;
        score = 0;
        oldHighScore = DataStore.numberGuessingGameHighestScore;
    }

    public void start() {
        oldHighScore = DataStore.numberGuessingGameHighestScore;
        initNewRound();
    }

    public void initNewRound() {
        max = getMaxNumber();
        number = random.nextInt(max + 1);
        remainingChanges = 10;
        observer.giveQuestion(String.format(Locale.getDefault(),
                context.getString(R.string.number_guessing_game_question_format),
                max,
                remainingChanges
        ));
    }

    private int getMaxNumber() {
        int current = 0, total = 0;
        while (total <= score) {
            current += 10;
            total += current;
        }
        return current;
    }

    public void continueGame() {
        observer.getUserGuessingNumber(String.format(Locale.getDefault(),
                context.getString(R.string.number_guessing_game_get_user_number_format),
                remainingChanges,
                max
        ));
    }

    public void updateScore(long score) {
        this.score = score;
        if (score > DataStore.numberGuessingGameHighestScore)
            DataStore.numberGuessingGameHighestScore = score;
        observer.updateScore(score);
    }

    public void submit(int number) {
        if (number == this.number) {
            updateScore(score + max);
            observer.giveCongratulation(String.format(Locale.getDefault(),
                    context.getString(R.string.number_guessing_game_congratulation_format),
                    this.number,
                    10 - remainingChanges + 1
            ));
        } else {
            remainingChanges--;
            if (remainingChanges > 0) {
                String message = String.format(Locale.getDefault(),
                        context.getString(R.string.number_guessing_game_guess_result_format),
                        number < this.number ? context.getString(R.string.low) : context.getString(R.string.high),
                        remainingChanges,
                        max
                );
                observer.getUserGuessingNumber(message);
            } else {
                observer.gameOver(String.format(Locale.getDefault(),
                        context.getString(R.string.number_guessing_game_over_format),
                        this.number
                ));
            }
        }
    }

    public boolean breakedScoreRecord() {
        return score > oldHighScore;
    }

    public long getOldHighScore() {
        return oldHighScore;
    }

    public long getScore() {
        return score;
    }
}
