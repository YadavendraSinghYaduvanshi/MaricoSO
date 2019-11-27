package com.cpm.Marico_so.utilities;

import com.cpm.Marico_so.dailyEntry.QuizActivity;

public class CountDownTimer extends android.os.CountDownTimer {

    private QuizActivity sourceActivity;

    public void setSourceActivity(QuizActivity sourceActivity) {
        this.sourceActivity = sourceActivity;
    }


    public CountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(this.sourceActivity!=null) {
            // Invoke source activity's tick event method.
            this.sourceActivity.onCountDownTimerTickEvent(millisUntilFinished);
        }

    }

    @Override
    public void onFinish() {
        if(this.sourceActivity!=null)
        {
            // Invoke source activity's tick event method.
            this.sourceActivity.onCountDownTimerFinishEvent();
        }
    }
}
