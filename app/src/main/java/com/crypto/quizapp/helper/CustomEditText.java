package com.crypto.quizapp.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

@SuppressLint("AppCompatCustomView")
public class CustomEditText extends AppCompatEditText {

    // custom EditText with custom font

    public CustomEditText(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "DAYPBL.ttf");
        this.setTypeface(face);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(),"DAYPBL.ttf");
        this.setTypeface(face);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "DAYPBL.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

    }
}
