package com.appsatwork.piggybank;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity
{
    //Currency
    Currency currency;
    int fractionDigits;
    String symbol;

    //Wage
    private static final double HOURLYWAGE = 12.50;
    private static final int MSPERUPDATE = 10;
    private double msWage;
    private double runningTotal = 0;

    //Timer
    Timer timer;
    TimerTask updateCounterTask;
    final Handler handler = new Handler();

    //Views
    AutoResizingTextView counter;
    Button startStopButton;
    Button resetButton;

    //Saving data onPause
    long millisWhenPaused;

    //Bug workaround: http://stackoverflow.com/questions/5033012/auto-scale-textview-text-to-fit-within-bounds/21851157#21851157
    final String DOUBLE_BYTE_SPACE = "\u3000";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Currency
        currency = CurrencyFactory.GetCurrency(CurrencyCode.EUR);
        fractionDigits = currency.getDefaultFractionDigits();
        symbol = currency.getSymbol();

        //Initialize Views
        counter = (AutoResizingTextView) findViewById(R.id.counter);
        startStopButton = (Button) findViewById(R.id.startstopbutton);
        resetButton = (Button) findViewById(R.id.resetbutton);

        //Calculate wage per millisecond
        msWage = HOURLYWAGE / (60 * 60 * 1000);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        stopTimer();
        millisWhenPaused = System.currentTimeMillis();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(millisWhenPaused != 0)
        {
            long currentTimeMillis = System.currentTimeMillis();
            runningTotal += (currentTimeMillis - millisWhenPaused) * msWage;
        }
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

    public void startTimer()
    {
        //Initialize timer
        timer = new Timer();

        //Initialize timer task
        initializeTimerTask();

        //Run timer task every millisecond
        timer.scheduleAtFixedRate(updateCounterTask, MSPERUPDATE, MSPERUPDATE);

        //Update button
        startStopButton.setText(this.getText(R.string.startstopbutton_stop));
    }

    public void stopTimer()
    {
        if (timer != null)
            timer.cancel();
        timer = null;

        //Update button
        startStopButton.setText(this.getText(R.string.startstopbutton_start));
    }

    private void initializeTimerTask()
    {
        updateCounterTask = new TimerTask()
        {
            public void run()
            {
                handler.post(new Runnable()
                {
                    public void run()
                    {
                        runningTotal += MSPERUPDATE * msWage;
                        updateCounterText();
                    }
                });
            }
        };
    }

    private void updateCounterText()
    {

        BigDecimal rounded = new BigDecimal(runningTotal).setScale(fractionDigits, BigDecimal.ROUND_HALF_UP);

        //Bug workaround as described in member variable DOUBLE_BYTE_SPACE
        String fixString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1
                && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            fixString = DOUBLE_BYTE_SPACE;
        }

        counter.setText(fixString + symbol + rounded.toString() + fixString);
    }

    public void ResetButtonPressed(View view)
    {
        stopTimer();
        runningTotal = 0;
        updateCounterText();
    }

    public void StartStopButtonPressed(View view)
    {
        if(timer != null)
            stopTimer();
        else
            startTimer();
    }
}
