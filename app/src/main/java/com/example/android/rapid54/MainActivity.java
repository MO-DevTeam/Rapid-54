package com.example.android.rapid54;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    TextView text;
    CountDownTimer timeo = null;

    public int c = 1, mintime, maxscore, time = 0, a=0, timem = 0;
    public boolean doubleBackConfirm = false;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public void help(View view){
        if (timeo != null){
            timeo.cancel();
        }
        setContentViewById(R.layout.activity_help);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_main);

        sharedPref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        editor  = sharedPref.edit();

        TextView timetext = (TextView) findViewById(R.id.timetext);
        TextView timertext = (TextView) findViewById(R.id.timer);
        TextView scoretext = (TextView) findViewById(R.id.scoretext);
        TextView score = (TextView) findViewById(R.id.score);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels/2;

        timertext.setWidth(width);
        timetext.setWidth(width);

        final Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/fontgen.ttf");
        timertext.setTypeface(face);
        timetext.setTypeface(face);
        scoretext.setTypeface(face);
        score.setTypeface(face);

        boolean first = sharedPref.getBoolean("first", true);

        if (first){
            help(findViewById(R.id.helpbutton));
            editor.putBoolean("first", false);
            editor.apply();
        }

        else {

            List<Integer> list = new ArrayList<>();
//            final LinearLayout MainLL = (LinearLayout) findViewById(R.id.MainLL);
            int num, count = 1;

            for (int i = 1; i <= 9; i++) {

                int id = getResources().getIdentifier("LL" + i, "id", getPackageName());
                LinearLayout LL = (LinearLayout) findViewById(id);
                LL.setGravity(Gravity.CENTER);



                int x = LL.getWidth() / 6;

                for (int j = 0; j < 6; j++) {


                    int resid = getResources().getIdentifier("text" + count, "id", getPackageName());
                    text = (TextView) findViewById(resid);

                    count++;

                    while (true) {

                        Random ran = new Random();
                        num = ran.nextInt(54) + 1;
                        if (!list.contains(num)) {
                            list.add(num);
                            break;
                        }
                    }
                    text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    text.setWidth(x);
                    text.setText("" + num + "");
                    text.setTextSize(21);
                    text.setTextColor(Color.parseColor("#000000"));
                    text.setId(num);
                    text.setBackgroundColor(Color.parseColor("#80CBC4"));
                    text.setClickable(true);
                    text.setTypeface(face);
                    text.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int a = v.getId();
                            if (a == c) {
                                c++;
                                TextView now = (TextView) findViewById(v.getId());
                                now.setBackgroundColor(Color.argb(150, 117, 117, 117));

                                now.setText(" ");
                                now.setClickable(false);
                                TextView score = (TextView) findViewById(R.id.score);
                                score.setText("" + (c-1) + "");

                                if (c == 55) {
                                    mintime = sharedPref.getInt("mintime", 60);
                                    if (mintime > time) {
                                        mintime = time;
                                        editor.putInt("mintime", mintime);
                                        editor.apply();
                                    }
                                    if (timeo != null) {
                                        timeo.cancel();
                                    }
                                    setContentViewById(R.layout.activity_2);
                                    TextView disp = (TextView) findViewById(R.id.disp);
                                    disp.setText("CONGRATS!");
                                    Typeface face2 = Typeface.createFromAsset(getAssets(),
                                            "fonts/endscr.ttf");
                                    disp.setTypeface(face2);
                                    TextView yscore = (TextView) findViewById(R.id.yourscore);
                                    yscore.setText("Your Score : " + time + " sec");
                                    yscore.setTextSize(20);
                                    yscore.setTypeface(face);
                                    TextView mscore = (TextView) findViewById(R.id.maxscore);
                                    mscore.setText("High Score : " + mintime + " sec");
                                    mscore.setTextSize(20);
                                    mscore.setTypeface(face);
                                }
                            } else {
                                TextView now = (TextView) findViewById(v.getId());
                                now.setBackgroundColor(Color.argb(200, 213, 0, 0));

                                if (timeo != null) {
                                    timeo.cancel();
                                }
                                now.setClickable(false);
                                for (int u = 1; u<=54; u++){

                                    //noinspection ResourceType
                                    TextView tv = (TextView) findViewById(u);
                                    tv.setClickable(false);
                                }

                                ImageView iv = (ImageView) findViewById(R.id.helpbutton);
                                iv.setClickable(false);

                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        setContentViewById(R.layout.activity_2);

                                        c = c - 1;
                                        maxscore = sharedPref.getInt("maxscore", 0);
                                        if (maxscore < c) {
                                            maxscore = c;
                                            editor.putInt("maxscore", maxscore);
                                            editor.apply();
                                        }
                                        TextView disp = (TextView) findViewById(R.id.disp);
                                        disp.setText("GAME OVER");
                                        Typeface face2 = Typeface.createFromAsset(getAssets(),
                                                "fonts/endscr.ttf");
                                        disp.setTypeface(face2);
                                        TextView yscore = (TextView) findViewById(R.id.yourscore);
                                        yscore.setText("Your Score : " + c + "");
                                        yscore.setTextSize(24);
                                        yscore.setTypeface(face);

                                        TextView mscore = (TextView) findViewById(R.id.maxscore);
                                        mscore.setText("High Score : " + maxscore + "");
                                        mscore.setTextSize(24);
                                        mscore.setTypeface(face);
                                    }
                                };

                                Handler h = new Handler();
                                h.postDelayed(r, 1000);


                            }
                        }
                    });


                }


            }
            Runnable e = new Runnable() {
                @Override
                public void run() {
                    start_timer(60000);
                }
            };

            Handler u = new Handler();
            u.postDelayed(e, 750);

        }

    }

    private int currentViewId = -1;

    public void setContentViewById(int id)
    {
        setContentView(id);
        currentViewId = id;
    }

    public int getCurrentViewById()
    {
        return currentViewId;
    }

    @Override
    public void onBackPressed(){
        if (timeo != null){
            timeo.cancel();
        }
        if (a == 1){
            super.onBackPressed();
        }
        else {
            if (getCurrentViewById() == R.layout.activity_main)
                {initiatePopupWindow();}
            else if (getCurrentViewById() == R.layout.activity_help){
                playAgain(findViewById(R.id.play));
            }
            else {
                if (doubleBackConfirm){
                    super.onBackPressed();
                }
                else {
                    makeToast("Click Again To Exit");
                    doubleBackConfirm = true;
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackConfirm=false;
                        }
                    }, 2000);
                }
            }
        }
    }

    public void makeToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void playAgain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void start_timer(int o)
    {    final TextView timer  = (TextView) findViewById(R.id.timer);
        timer.setTextColor(Color.parseColor("#000000"));
        timeo = new CountDownTimer(o, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            String timerText1 = millisUntilFinished / 1000 + " : ", timerText2 = "" + (millisUntilFinished%1000)/10;
            if (timerText2.length() == 1){
                timerText2 = timerText2 + 0;
            }
            if (timerText1.length() == 4){
                timerText1 = 0 + timerText1;
            }
            String timerText = timerText1 + timerText2;
            timer.setText(timerText);
            time = 60 - (int) millisUntilFinished/1000;
            timem = (int) millisUntilFinished;
            if (time >= 50){
                timer.setTextColor(Color.parseColor("#D50000"));
            }

        }

        @Override
        public void onFinish() {

            makeToast("Time up");

            setContentViewById(R.layout.activity_2);
            c = c-1;
            maxscore = sharedPref.getInt("maxscore", 0);
            if (maxscore < c){
                maxscore = c;
                editor.putInt("maxscore", maxscore);
                editor.apply();
            }
            TextView disp = (TextView) findViewById(R.id.disp);
            disp.setText("GAME OVER");
            Typeface face2 = Typeface.createFromAsset(getAssets(),
                    "fonts/endscr.ttf");
            disp.setTypeface(face2);
            TextView yscore = (TextView) findViewById(R.id.yourscore);
            yscore.setText("Your Score : " + c + "");
            yscore.setTextSize(20);
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/fontgen.ttf");
            yscore.setTypeface(face);
            TextView mscore = (TextView) findViewById(R.id.maxscore);
            mscore.setText("High Score : " + maxscore + "");
            mscore.setTextSize(20);
            mscore.setTypeface(face);

        }
    }.start();




    }

    public PopupWindow pwindo;

    public void initiatePopupWindow() {
        try {

            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.exit_popup,
                    (ViewGroup) findViewById(R.id.popup_element));

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width = displayMetrics.widthPixels - 70;

            pwindo = new PopupWindow(layout);
            pwindo.setFocusable(true);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                pwindo.setElevation(100f);
                width = width - 60;
            } else {

                pwindo.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.dialog_holo_light_frame));
            }
            pwindo.setHeight(width / 2);
            pwindo.setWidth(width);
            pwindo.setOutsideTouchable(false);


            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            View container = (View) pwindo.getContentView().getParent();
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0.6f;
            wm.updateViewLayout(container, p);

            pwindo.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    pwindo.dismiss();
                    start_timer(timem);
                }
            });


            TextView resume = (TextView) layout.findViewById(R.id.resume);
            TextView exit = (TextView) layout.findViewById(R.id.exit);

            resume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pwindo.dismiss();

                }
            });

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    a = 1;
                    onBackPressed();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
