package com.appsatwork.piggybank;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class NumberView extends View
{
    //Attributes
    private String contentChar = getResources().getString(R.string.number_view_content_char_default);
    private int contentColor = getResources().getColor(R.color.number_view_content_color_default);
    private float contentDimension = getResources().getDimension(R.dimen.number_view_content_dimension_default);
    private int backgroundColor = getResources().getColor(R.color.number_view_background_color_default);

    //Paints
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    public NumberView(Context context)
    {
        super(context);
        init(null, 0);
    }

    public NumberView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, 0);
    }

    public NumberView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.NumberView, defStyle, 0);
        contentChar = a.getString(
                R.styleable.NumberView_contentChar);
        contentColor = a.getColor(
                R.styleable.NumberView_contentColor,
                contentColor);
        backgroundColor = a.getColor(R.styleable.NumberView_backgroundColor,
                backgroundColor);

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        contentDimension = a.getDimension(
                R.styleable.NumberView_contentDimension,
                contentDimension);

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements()
    {
        mTextPaint.setTextSize(contentDimension);
        mTextPaint.setColor(contentColor);
        mTextWidth = mTextPaint.measureText(contentChar);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(contentChar,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);
    }

    public String getContentChar()
    {
        return contentChar;
    }

    public void setContentChar(String s)
    {
        contentChar = s;
        invalidateTextPaintAndMeasurements();
    }

    public int getContentColor()
    {
        return contentColor;
    }

    public void setContentColor(int c)
    {
        contentColor = c;
        invalidateTextPaintAndMeasurements();
    }

    public int getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(int c)
    {
        backgroundColor = c;
        invalidateTextPaintAndMeasurements();
    }

    public float getContentDimension()
    {
        return contentDimension;
    }

    public void setContentDimension(float d)
    {
        contentDimension = d;
        invalidateTextPaintAndMeasurements();
    }
}
