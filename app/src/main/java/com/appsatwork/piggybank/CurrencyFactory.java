package com.appsatwork.piggybank;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bruno on 2-5-2015.
 */
public class CurrencyFactory
{
    public static List<Currency> GetAllCurrencies()
    {
        List<Currency> result = new ArrayList<Currency>();
        for (CurrencyCode cc : CurrencyCode.values())
            result.add(Currency.getInstance(cc.name()));
        return result;
    }

    public static Currency GetLocalCurrency()
    {
        return Currency.getInstance(Locale.getDefault());
    }

    public static Currency GetCurrency(String currencyCode)
    {
        return Currency.getInstance(currencyCode);
    }

    public static Currency GetCurrency(CurrencyCode currencyCode)
    {
        return Currency.getInstance(currencyCode.name());
    }

    public static String GetCurrencyName(Currency curr, Context c)
    {
        Resources r = c.getResources();
        String name = c.getPackageName();
        return r.getString(r.getIdentifier(curr.getCurrencyCode(), "string", name));
    }

    //For the value of 1 USD
    public static int DigitsBeforeDecimal(Currency curr, Context c)
    {
        Resources r = c.getResources();
        String name = c.getPackageName();
        TypedValue typedValue = new TypedValue();
        r.getValue(r.getIdentifier(curr.getCurrencyCode(), "dimen", name), typedValue, true);
        return (int) Math.ceil(Math.log10(typedValue.getFloat())) + 1;
    }
}

