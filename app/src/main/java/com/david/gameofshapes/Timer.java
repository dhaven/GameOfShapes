package com.david.gameofshapes;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by ndizera on 03/07/2015.
 * //Timer that show the number of seconds resting.
 */
public class Timer {
    private long time; //time of the timer (in milliseconds)
    private boolean interrupt; //set to true if the timer is cancelled
    private TextView timerView;
    private long startingTime;
    private boolean finished; //set to true if the timer finished
    private TimerListener listener;

    //Constructor
    public Timer(long time, TextView timerView){
        this.time = time;
        this.timerView = timerView;
        timerView.setText("" + ((int) time/1000));
        finished = false;
        interrupt = false;
        listener = null;
    }

    //Constructor
    public Timer(long time, TextView timerView, TimerListener listener){
        this.time = time;
        this.timerView = timerView;
        this.listener = listener;
        timerView.setText("" + (int) time/1000);
        finished = false;
        interrupt = false;
    }

    //start the timer
    public void start(){
        startingTime = System.currentTimeMillis();
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(interrupt == false && elapsed() ==false){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateView();
                        }
                    });
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        System.err.println("Error:" + e.getMessage());
                    }
                }

                finished = true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        executeCompletionMethod();
                    }
                });

            }
        }).start();
    }

    //execute the listener method
    public void executeCompletionMethod(){
        if(listener != null && interrupt == false){
            listener.run();
        }
    }

    public boolean elapsed(){
        if(restingTime() > 0){
            return false;
        }

        return  true;
    }

    //return true if the timer is finished
    public boolean isFinished(){
        return finished;
    }

    //cancel the timer
    public void cancel(){
        interrupt = true;
    }

    //Update the TextView
    private void updateView(){
        timerView.setText("" + (int) restingTime()/1000);
    }

    //return the time resting
    public long restingTime(){
        return time - getTime();
    }

    //return the time elapsed
    public long getTime(){
        return System.currentTimeMillis() - startingTime;
    }

    //return the time elapsed (in secondes)
    public int getSecTime(){
        return (int) getTime() / 1000;
    }


    public interface TimerListener{
        public void run();
    }

}
