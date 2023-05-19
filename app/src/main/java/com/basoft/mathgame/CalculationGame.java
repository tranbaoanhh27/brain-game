package com.basoft.mathgame;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import java.util.Locale;
import java.util.Random;

public class CalculationGame {
    private static final int DEFAULT_LIFE = 3;
    private long score;
    private int life;
    private CalculationGameObserver observer;
    private Context context;
    private Question currentQuestion;
    private CountDownTimer timer;

    public enum Level {
        VERY_EASY,
        EASY,
        NORMAL,
        HARD,
    }

    public CalculationGame(@NonNull CalculationGameObserver observer, Context context) {
        this.observer = observer;
        this.context = context;
        setScore(0);
        setLife(DEFAULT_LIFE);
    }

    public void setScore(long score) {
        this.score = score;
        observer.updatePlayerScore(score);
    }

    public void setLife(int life) {
        this.life = life;
        observer.updatePlayerLife(life);
    }

    public void start() {
        startNewQuestion();
    }

    public void startNewQuestion() {
        resetTimer();
        observer.unfreezeInput();
        currentQuestion = createQuestion();
        observer.updateExpression(currentQuestion.getExpression());
        timer = new CountDownTimer(currentQuestion.getTimeInMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                observer.updateRemainingTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                String userAnswer = observer.getCurrentUserAnswer();
                checkAnswer(userAnswer);
            }
        };
        timer.start();
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void checkAnswer(String answerString) {
        observer.freezeInput();
        resetTimer();
        try {
            int answer = Integer.parseInt(answerString);
            boolean correct = answer == currentQuestion.getCorrectAnswer();
            if (correct) {
                observer.updateCorrectAnswer(currentQuestion.getCorrectAnswer(), CalculationGameObserver.AnswerStatus.CORRECT);
                setScore(score + currentQuestion.getScoreValue());
            } else {
                observer.updateCorrectAnswer(currentQuestion.getCorrectAnswer(), CalculationGameObserver.AnswerStatus.WRONG);
                setLife(life - 1);
            }
        } catch (NumberFormatException exception) {
            observer.updateCorrectAnswer(currentQuestion.getCorrectAnswer(), CalculationGameObserver.AnswerStatus.WRONG);
            setLife(life - 1);
        }
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Do nothing
            }

            @Override
            public void onFinish() {
                if (life > 0) startNewQuestion();
                else endGame();
            }
        }.start();
    }

    private void endGame() {
        observer.updateHighestScore(score);
        Toast.makeText(context, "GAME OVER!", Toast.LENGTH_LONG).show();
        if (context instanceof Activity) ((Activity) context).finish();
    }

    private Question createQuestion() {
        if (score < 100) return new Question(Level.VERY_EASY);
        if (score < 300) return new Question(Level.EASY);
        if (score < 500) return new Question(Level.NORMAL);
        return new Question(Level.HARD);
    }
}

class Question {
    private String expression;
    private long correctAnswer;
    private long timeInMillis;
    private int scoreValue;

    public Question(CalculationGame.Level level) {
        Random randomizer = new Random();
        switch (level) {
            case VERY_EASY: {
                // Very easy question: 2 operands from 1 to 50, add or subtract, 15s, 10 points
                char[] operators = {'+', '-'};
                expression = String.format(Locale.getDefault(),
                        "%d %c %d",
                        randomizer.nextInt(50) + 1,
                        operators[randomizer.nextInt(operators.length)],
                        randomizer.nextInt(50) + 1
                );
                scoreValue = 10;
                break;
            }
            case EASY: {
                // Easy question: 3 operands from 1 to 50, add or subtract, 15s, 15 points
                char[] operators = {'+', '-'};
                expression = String.format(Locale.getDefault(),
                        "%d %c %d %c %d",
                        randomizer.nextInt(50) + 1,
                        operators[randomizer.nextInt(operators.length)],
                        randomizer.nextInt(50) + 1,
                        operators[randomizer.nextInt(operators.length)],
                        randomizer.nextInt(50) + 1
                );
                scoreValue = 15;
                break;
            }
            case NORMAL: {
                // Normal question: 3 operands from 1 to 50, add or subtract and multiply, 15s, 20 points
                char[] operators = {'+', '-', '*'};
                expression = String.format(Locale.getDefault(),
                        "%d %c %d %c %d",
                        randomizer.nextInt(50) + 1,
                        operators[randomizer.nextInt(operators.length)],
                        randomizer.nextInt(50) + 1,
                        operators[randomizer.nextInt(operators.length)],
                        randomizer.nextInt(50) + 1
                );
                scoreValue = 20;
                break;
            }
            case HARD: {
                // Hard question: 3 operands from 51 to 100, add or subtract and multiply, 15s, 25 points
                char[] operators = {'+', '-', '*'};
                expression = String.format(Locale.getDefault(),
                        "%d %c %d %c %d",
                        randomizer.nextInt(50) + 51,
                        operators[randomizer.nextInt(operators.length)],
                        randomizer.nextInt(50) + 51,
                        operators[randomizer.nextInt(operators.length)],
                        randomizer.nextInt(50) + 51
                );
                scoreValue = 25;
                break;
            }
        }
        timeInMillis = 15000;
        correctAnswer = MathHelper.evaluate(expression.replaceAll(" ", ""));
    }

    public String getExpression() {
        return expression;
    }

    public long getCorrectAnswer() {
        return correctAnswer;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public int getScoreValue() {
        return scoreValue;
    }
}