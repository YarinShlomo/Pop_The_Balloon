package com.example.pop_the_balloon;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Locale;

//arrives from game-over screen to put-in the score and name
public class NameHighscore extends AppCompatActivity {

    private ViewGroup mContentView;

    TextView tvscore;
    EditText etname;
    Intent intent;
    Bundle bundle;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_name_highscore);

        mContentView = findViewById(R.id.NameHighScore);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });

        tvscore = findViewById(R.id.tvscore);
        showscore();

        dbManager = new DBManager(this);

        LinearLayout linlay = findViewById(R.id.linlayHighScore);
        GradientDrawable gd1 = (GradientDrawable) linlay.getBackground();
        gd1.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_blue_light, null));

        Button bhss = findViewById(R.id.finished_btn);
        GradientDrawable gd2 = (GradientDrawable) bhss.getBackground();
        gd2.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_blue, null));
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

    // making the user put in a nickname for the Leaderboard table
    // goes back to the mainmenu activity
    public void Return(View v){
        ContentValues values = new ContentValues();
        etname = findViewById(R.id.enterName);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        if (TextUtils.isEmpty(etname.getText())){
            etname.setError("Please Enter Your Name");
        } else {
            values.put(DBManager.Name, etname.getText().toString());
            values.put(DBManager.Score, tvscore.getText().toString());
            dbManager.Insert(values);

            Button b1 = findViewById(R.id.finished_btn);
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
    }

    // updates the user's score in the textview
    private void showscore() {

        intent = getIntent();
        bundle = intent.getExtras();
        assert bundle != null;
        int score = bundle.getInt("score");

        tvscore.setText(String.valueOf(score));

    }

    @Override
    public void onBackPressed() {
        etname = findViewById(R.id.enterName);
        etname.setError("Enter Your Name");
    }

    // load language saved in shared preferences
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }


    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().
                updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }
}
