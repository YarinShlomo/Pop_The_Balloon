package com.example.pop_the_balloon;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

// this Activity describe the game to the user
public class About extends AppCompatActivity {

    private ViewGroup mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mContentView = findViewById(R.id.About);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });

        // set buttons and linlay background's color
        LinearLayout AboutLinlay = findViewById(R.id.linlayAbout);
        GradientDrawable gd1 = (GradientDrawable) AboutLinlay.getBackground();
        gd1.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_yellow, null));

        Button returnBtn = findViewById(R.id.returnAbout_btn);
        GradientDrawable gd2 = (GradientDrawable) returnBtn.getBackground();
        gd2.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_yellow, null));

    }

    public void Return(View v){
        Button btnReturn = findViewById(R.id.returnAbout_btn);
        //set button's sound
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        // button animation (jumpy)
        ObjectAnimator animY = ObjectAnimator.ofFloat(btnReturn, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

        //go back to mainmenu activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 500);
    }

    // set to full screen :)
    private void setToFullScreen() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToFullScreen();
    }
}
