package com.appsatwork.piggybank;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity
{
    //Account data
    Account account;
    Currency defaultCurrency;
    Wage defaultWage;

    //Timer
    Timer timer;
    TimerTask updateCounterTask;
    final Handler handler = new Handler();
    private static final int MSPERUPDATE = 10;

    //Views
    TextView symbol;
    TextView counter;
    Button startStopButton;
    Button resetButton;
    Spinner spinner;
    EditText wageEdit;

    //Saving data onPause
    long millisWhenPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Default currency and wage
        Currency defaultCurrency = CurrencyFactory.GetLocalCurrency();
        account = new Account(defaultCurrency, new Wage(Math.pow(10, CurrencyFactory.DigitsBeforeDecimal(defaultCurrency, this) - 1)));

        //Initialize Views
        symbol = (TextView) findViewById(R.id.symbol);
        counter = (TextView) findViewById(R.id.counter);
        startStopButton = (Button) findViewById(R.id.startstopbutton);
        resetButton = (Button) findViewById(R.id.resetbutton);
        spinner = (Spinner) findViewById(R.id.spinner);
        wageEdit = (EditText) findViewById(R.id.wageEdit);

        wageEdit.setText(Double.toString(account.Wage.Hourly));
        populateSpinner();
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
            account.RunningTotal += (currentTimeMillis - millisWhenPaused) * account.Wage.MillisecondWage;
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


    private void populateSpinner()
    {
        List<String> content = new ArrayList<>();
        for(Currency c : CurrencyFactory.GetAllCurrencies())
            content.add(c.getCurrencyCode() + " - " + CurrencyFactory.GetCurrencyName(c, this));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, content);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new CurrencySelectedListener());
        spinner.setSelection(CurrencyFactory.GetAllCurrencies().indexOf(account.Currency));
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
                        account.RunningTotal += MSPERUPDATE * account.Wage.MillisecondWage;
                        updateCounterText();
                    }
                });
            }
        };
    }

    private void updateCounterText()
    {
        symbol.setText(account.Symbol);
        counter.setText(account.ToString());
    }

    public void ResetButtonPressed(View view)
    {
        stopTimer();
        account.Reset();
        updateCounterText();
    }

    public void StartStopButtonPressed(View view)
    {
        if(timer != null)
            stopTimer();
        else
            startTimer();
    }


    public class CurrencySelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            TextView tv = (TextView)view;
            Currency c = CurrencyFactory.GetCurrency(CurrencyCode.valueOf(tv.getText().subSequence(0,3).toString()));
            account.SetCurrency(c);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }
}
