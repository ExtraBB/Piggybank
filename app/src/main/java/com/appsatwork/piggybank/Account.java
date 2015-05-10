package com.appsatwork.piggybank;
import java.math.BigDecimal;
import java.util.Currency;
/**
 * Created by Bruno on 10-5-2015.
 */
public class Account {

    public Currency Currency;
    public Wage Wage;
    public double RunningTotal;
    public int FractionDigits;
    public String Symbol;

    public Account(Currency curr, Wage wage)
    {
        Currency = curr;
        Wage = wage;
        RunningTotal = 0;
        FractionDigits = Currency.getDefaultFractionDigits();
        Symbol = Currency.getSymbol();
    }

    public void SetCurrency(Currency curr)
    {
        Currency = curr;
        FractionDigits = Currency.getDefaultFractionDigits();
        Symbol = Currency.getSymbol();
        Reset();
    }

    public void SetWage(Wage wage)
    {
        Wage = wage;
        Reset();
    }

    public void SetWage(double hourlyWage)
    {
        Wage.SetHourly(hourlyWage);
        Reset();
    }

    public void Reset()
    {
        RunningTotal = 0;
    }

    public String ToString()
    {
        return new BigDecimal(RunningTotal).setScale(FractionDigits, BigDecimal.ROUND_HALF_UP).toString();
    }
}
