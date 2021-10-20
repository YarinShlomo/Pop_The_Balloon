package com.example.pop_the_balloon;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;


/**
 * this activity in charge of presenting the leaderboard to the user
 */
public class HighestScore extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ViewGroup mContentView;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_score);

        mContentView = findViewById(R.id.highestScore);

        //set to full screen
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });

        dbManager = new DBManager(this);

        loadtableHS();

        // color the button and the listview background
        ListView lv1 = findViewById(R.id.lvhighscore);
        GradientDrawable gd1 = (GradientDrawable) lv1.getBackground();
        gd1.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_green, null));

        Button returnBtn = findViewById(R.id.returnHighScore_btn);
        GradientDrawable gd2 = (GradientDrawable) returnBtn.getBackground();
        gd2.setColor(ResourcesCompat.getColor(getResources(),
                R.color.google_green, null));
    }

    ArrayList<AdapterItems> listnewsData = new ArrayList<>();
    MyCustomAdapter myadapter;


    /**
     * load in to the listview the top 20 player along side their score
     */
    private void loadtableHS() {
        listnewsData.clear();
        String[] projection = {"Name", "Score"};
        Cursor cursor = dbManager.Query(projection,null, null, DBManager.Score);
        int a = 0;
        if (cursor.moveToFirst()) {
            do {
                if(a < 20){
                    a++;
                    listnewsData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(DBManager.Name)),
                            cursor.getString(cursor.getColumnIndex(DBManager.Score))));
                }
            } while (cursor.moveToNext());
        }

        // use adapter in order to show on the listview
        myadapter=new MyCustomAdapter(listnewsData);
        ListView lsNews = findViewById(R.id.lvhighscore);
        lsNews.setAdapter(myadapter);
    }

    // return goes back to the mainmenu screen (Activity)
    public void Return(View v) {

        Button b1 = findViewById(R.id.returnHighScore_btn);

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        return false;
    }

    private class MyCustomAdapter extends BaseAdapter {
        ArrayList<AdapterItems> listnewsDataAdpater ;

        MyCustomAdapter(ArrayList<AdapterItems> listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
        }


        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         *
         * @param position of the data in the arraylist
         * @param convertView
         * @param parent
         * @return view of (name, score)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket,null);

            final AdapterItems s = listnewsDataAdpater.get(position);

            TextView Nametv = myView.findViewById(R.id.tvName);
            Nametv.setText(s.Name);

            TextView ScoreTv = myView.findViewById(R.id.tvScore);
            ScoreTv.setText(s.Score);

            return myView;
        }

    }

}
