package com.basoft.braingame;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

public class TypeWriter extends androidx.appcompat.widget.AppCompatTextView {
    private CharSequence mText;
    private int mIndex;
    private long mDelay = 75; //Default 75ms delay


    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private final Handler mHandler = new Handler();
    private final Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}
