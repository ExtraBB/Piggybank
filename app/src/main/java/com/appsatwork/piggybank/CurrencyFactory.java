package com.appsatwork.piggybank;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by Broodrooster on 2-5-2015.
 */
public class CurrencyFactory
{
    public List<Currency> GetAllCurrencies()
    {
        List<Currency> result = new ArrayList<Currency>();
        for(CurrencyCode cc : CurrencyCode.values())
            result.add(Currency.getInstance(cc.name()));
        return result;
    }

    public Currency GetLocalCurrency()
    {
        return Currency.getInstance(Locale.getDefault());
    }

    public Currency GetCurrency(String currencyCode)
    {
        return Currency.getInstance(currencyCode);
    }

    public String GetCurrencyName(Currency curr, Context c)
    {
        Resources r = c.getResources();
        String name = c.getPackageName();
        return r.getString(r.getIdentifier(curr.getCurrencyCode(), "string", name));
    }

    //For the value of 1 USD
    public int DigitsBeforeDecimal(Currency curr, Context c)
    {
        Resources r = c.getResources();
        String name = c.getPackageName();
        TypedValue typedValue = new TypedValue();
        r.getValue(r.getIdentifier(curr.getCurrencyCode(), "dimen", name), typedValue, true);
        return (int)Math.ceil(Math.log10(typedValue.getFloat()));
    }
}
