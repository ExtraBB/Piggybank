package com.appsatwork.piggybank;

/**
 * Created by Bruno on 10-5-2015.
 */
public class Wage {
    public double Hourly;
    public double MillisecondWage;

    public Wage(double hourlyWage)
    {
        Hourly = hourlyWage;
        MillisecondWage = Hourly / 3600000;
    }

     public void SetHourly(double hourly)
     {
         Hourly = hourly;
         MillisecondWage = Hourly / 3600000;
     }
}
