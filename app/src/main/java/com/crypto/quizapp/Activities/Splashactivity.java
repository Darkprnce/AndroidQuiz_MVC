package com.crypto.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crypto.quizapp.R;
import com.crypto.quizapp.helper.CommonSharedPreference;

public class Splashactivity extends AppCompatActivity {
    private ImageView splash;
    private View decorView;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);
        initView();
    }

    private void initView() {
        splash = findViewById(R.id.splash);

        Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.right_to_left);
        splash.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startHandler();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(CommonSharedPreference.getsharedText(Splashactivity.this,"logged"))){
                    Intent intent = new Intent(Splashactivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(Splashactivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        };

        handler.postDelayed(runnable,3500);
    }

    public void noactionbar(){
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        noactionbar();
        if(handler !=null){
            handler.postDelayed(runnable,3500);
        }
    }
}
