package com.appsatwork.piggybank;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity
{

    private static final double HOURLYWAGE = 7.70;
    private static final int MSPERUPDATE = 10;

    private double msWage;
    private double runningTotal = 0;

    Timer timer;
    TimerTask updateCounterTask;
    final Handler handler = new Handler();

    TextView counter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize counter
        counter = (TextView)findViewById(R.id.counter);

        //Calculate wage per millisecond
        msWage = HOURLYWAGE / (60*60*1000);

        //Start the timer to update the counter
        startTimer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startTimer() {

        //Initialize timer
        timer = new Timer();

        //Initialize timer task
        initializeTimerTask();

        //Run timer task every millisecond
        timer.scheduleAtFixedRate(updateCounterTask, 0, MSPERUPDATE); //
    }

    private void initializeTimerTask() {
        updateCounterTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        runningTotal += MSPERUPDATE * msWage;
                        counter.setText(Double.toString(runningTotal));
                    }
                });
            }
        };
    }

}
