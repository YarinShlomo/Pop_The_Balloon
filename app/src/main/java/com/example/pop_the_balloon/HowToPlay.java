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

/**
 * how to play screen activity - the game instructions.
 */
public class HowToPlay extends AppCompatActivity {

    private ViewGroup mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtoplay);

        mContentView = findViewById(R.id.HowToPlay);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });

        // color button and linlay
        LinearLayout HtpLinlay = findViewById(R.id.linlayHTP);
        GradientDrawable gd1 = (GradientDrawable) HtpLinlay.getBackground();
        gd1.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_red, null));

        Button ReturnBtn = findViewById(R.id.returnHTP_btn);
        GradientDrawable gd2 = (GradientDrawable) ReturnBtn.getBackground();
        gd2.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_red, null));

    }

    // returns in to the mainmenu activity
    public void Return(View v){
        Button b1 = findViewById(R.id.returnHTP_btn);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        //button animation
        ObjectAnimator animY = ObjectAnimator.ofFloat(b1, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

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
