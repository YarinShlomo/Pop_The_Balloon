package com.example.pop_the_balloon;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Locale;

public class MainMenu extends AppCompatActivity {

    private ViewGroup mContentView;
    DBManager dbManager;
    int level = 1;
    boolean condition = false;
    private SoundHelper mSoundHelper;
    boolean isMusic = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);

        super.onCreate(savedInstanceState);
        //for language changes
         loadLocale();

        setContentView(R.layout.activity_main_menu);

        mSoundHelper = new SoundHelper(this);
        mSoundHelper.prepareMusicPlayer2(this);
        mContentView = findViewById(R.id.main_menu);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });

        dbManager = new DBManager(this);

        // set buttons color
        Button b1 = findViewById(R.id.startGame_btn1);
        GradientDrawable db1 = (GradientDrawable) b1.getBackground();
        db1.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_blue, null));

        Button b2 = findViewById(R.id.score_btn2);
        GradientDrawable db2 = (GradientDrawable) b2.getBackground();
        db2.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_green_light, null));

        Button b3 = findViewById(R.id.howto_btn3);
        GradientDrawable db3 = (GradientDrawable) b3.getBackground();
        db3.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_red_light, null));

        Button b4 = findViewById(R.id.about_btn4);
        GradientDrawable db4 = (GradientDrawable) b4.getBackground();
        db4.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_yellow_light, null));

        Button b5 = findViewById(R.id.exit_btn5);
        GradientDrawable db5 = (GradientDrawable) b5.getBackground();
        db5.setColor(ResourcesCompat.getColor(getResources(),
                R.color.colorPrimary, null));

        Button btnStart = findViewById(R.id.startLevel_btn);
        GradientDrawable gdStart = (GradientDrawable) btnStart.getBackground();
        gdStart.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_blue, null));

        Button btnReturn = findViewById(R.id.returnLevel_btn);
        GradientDrawable gdReturn = (GradientDrawable) btnReturn.getBackground();
        gdReturn.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_blue, null));

        TextView title = findViewById(R.id.headLine); //Animation of the headline
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        title.startAnimation(scale);
        GradientDrawable gdTitle = (GradientDrawable) title.getBackground();
        gdTitle.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_blue_light, null));
        ObjectAnimator animY = ObjectAnimator.ofFloat(title, "translationY",
                -10f, 10f);
        animY.setDuration(1000);
        animY.setRepeatCount(ValueAnimator.INFINITE);
        animY.setRepeatMode(ValueAnimator.REVERSE);
        animY.start();

        // picking a number to choose level
        NumberPicker np = findViewById(R.id.numberpicker);
        np.setMinValue(1);
        np.setMaxValue(20);

        np.setOnValueChangedListener(onValueChangeListener);
        mSoundHelper.playMusic();

        Button changeLang = findViewById(R.id.changeLang);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show alertDialog to display list of languages, one can be selected
                showChangeLanguageDialog();
            }
        });

        final Drawable first = getResources().getDrawable(
               R.drawable.speaker);
        final Drawable second = getResources().getDrawable(
               R.drawable.speakeroff);
        final Button btnSoundtoggle = (Button) findViewById(R.id.soundbtn);
        btnSoundtoggle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (btnSoundtoggle.getBackground().equals(first)) {
                    mSoundHelper.pauseMusic();
                    isMusic = false;
                    btnSoundtoggle.setBackgroundDrawable(second);
                } else {
                    mSoundHelper.playMusic();
                    isMusic = true;
                    btnSoundtoggle.setBackgroundDrawable(first);
                }
            }
        });
    }
    private void showChangeLanguageDialog(){
        final String[] listItems = {"English", "עברית"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainMenu.this);
        mBuilder.setTitle(R.string.language);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0){
                    // English
                    setLocale("en");
                    mSoundHelper.pauseMusic();
                    recreate();
                }
                else if (i == 1){
                    //Hebrew
                    setLocale("iw");
                    mSoundHelper.pauseMusic();
                    recreate();
                }
                //dismiss dialog
                dialogInterface.dismiss();

            }
        });
        AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.show();
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



    NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            level = newVal;
        }
    };

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

    // reveling the level picker layout
    public void menuStart(View v) {

        Button b1 = findViewById(R.id.startGame_btn1);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        // button animation
        ObjectAnimator animY = ObjectAnimator.ofFloat(b1, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout rl = findViewById(R.id.layoutSelectLevel);
                rl.setVisibility(View.VISIBLE);
                condition = true;
            }
        }, 500);
    }

    // opens the Leaderboard activity
    public void menuLeaderBord(View v) {

        Button b2 = findViewById(R.id.score_btn2);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mSoundHelper.pauseMusic();
        ObjectAnimator animY = ObjectAnimator.ofFloat(b2, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent2 = new Intent(getApplicationContext(), HighestScore.class);
                startActivity(intent2);
            }
        }, 500);
    }

    // opens the howtoplay activity
    public void menuHowToPlay(View v) {

        Button b3 = findViewById(R.id.howto_btn3);
        mSoundHelper.pauseMusic();
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        ObjectAnimator animY = ObjectAnimator.ofFloat(b3, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent3 = new Intent(getApplicationContext(), HowToPlay.class);
                startActivity(intent3);
            }
        }, 500);
    }

    // opens the about activity
    public void menuAbout(View v) {

        Button b4 = findViewById(R.id.about_btn4);
        mSoundHelper.pauseMusic();
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        ObjectAnimator animY = ObjectAnimator.ofFloat(b4, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent4 = new Intent(getApplicationContext(), About.class);
                startActivity(intent4);
            }
        }, 500);
    }

    // open the exit dialog
    public void menuExit(View v) {
        mSoundHelper.pauseMusic();
        Button b5 = findViewById(R.id.exit_btn5);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        ObjectAnimator animY = ObjectAnimator.ofFloat(b5, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

        new AlertDialog.Builder(this)
                .setMessage(R.string.before_quiting)
                .setCancelable(true)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainMenu.this.finish();
                    }
                })
                .setNegativeButton(R.string.No, null)
                .show();
    }

    // go in to the mainactivity activity (1step before starting the game)
    public void StartBtn(View view) {
        mSoundHelper.pauseMusic();
        Button btnStart = findViewById(R.id.startLevel_btn);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        ObjectAnimator animY = ObjectAnimator.ofFloat(btnStart, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout rl = findViewById(R.id.layoutSelectLevel);
                rl.setVisibility(View.GONE);
                condition = false;
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("level", level);
                bundle.putBoolean("isMusic", isMusic);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        }, 500);
    }

    // set level pick layout to gona (invisible)
    public void returnLevelBtn(View v) {
        Button btnReturn = findViewById(R.id.returnLevel_btn);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        ObjectAnimator animY = ObjectAnimator.ofFloat(btnReturn, "translationY",
                -10f, 0f);
        animY.setDuration(1000);
        animY.setInterpolator(new BounceInterpolator());
        animY.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout rl = findViewById(R.id.layoutSelectLevel);
                rl.setVisibility(View.GONE);
                condition = false;
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        if (condition) {
            RelativeLayout rl = findViewById(R.id.layoutSelectLevel);
            rl.setVisibility(View.GONE);
            condition = false;
        } else {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.before_quiting)
                    .setCancelable(true)
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainMenu.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.No, null)
                    .show();
        }
    }


}