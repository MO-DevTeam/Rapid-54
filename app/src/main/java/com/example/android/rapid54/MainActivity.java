package com.example.android.rapid54;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.String;
import java.lang.Object;



public class MainActivity extends AppCompatActivity {

    TextView text;
    CountDownTimer timeo = null;

    public int c = 1, mintime, maxscore, time = 0;


    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public void help(View view){
        if (timeo != null){
            timeo.cancel();
        }
        setContentView(R.layout.activity_help);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        editor  = sharedPref.edit();

        boolean first = sharedPref.getBoolean("first", true);

        if (first){
            help(findViewById(R.id.helpbutton));
            editor.putBoolean("first", false);
            editor.apply();
        }

        else {

            List<Integer> list = new ArrayList<>();

            int num, count = 1;

            for (int i = 1; i <= 9; i++) {

                int id = getResources().getIdentifier("LL" + i, "id", getPackageName());
                LinearLayout LL = (LinearLayout) findViewById(id);
                LL.setGravity(Gravity.CENTER);

                int x = LL.getWidth() / 6, y = ((LL.getHeight() - 24) / 2);

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

                    text.setPadding(2, -y, 2, -y);
                    text.setWidth(x);
                    text.setText("" + num);
                    text.setTextSize(24);
                    text.setId(num);
                    text.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    text.setClickable(true);

                    text.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int a = v.getId();
                            if (a == c) {
                                c++;
                                TextView now = (TextView) findViewById(v.getId());
                                now.setBackgroundColor(Color.parseColor("#424242"));
                                now.setText(" ");
                                now.setClickable(false);

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
                                    setContentView(R.layout.activity_2);
                                    TextView disp = (TextView) findViewById(R.id.disp);
                                    disp.setText("CONGRATS!");
                                    TextView yscore = (TextView) findViewById(R.id.yourscore);
                                    yscore.setText("Your Score : " + time + " sec");
                                    yscore.setTextSize(20);
                                    TextView mscore = (TextView) findViewById(R.id.maxscore);
                                    mscore.setText("Max Score : " + mintime + " sec");
                                    mscore.setTextSize(20);
                                }
                            } else {
                                TextView now = (TextView) findViewById(v.getId());
                                now.setBackgroundColor(Color.parseColor("#D50000"));

                                if (timeo != null) {
                                    timeo.cancel();
                                }
                                now.setClickable(false);


                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        setContentView(R.layout.activity_2);

                                        c = c - 1;
                                        maxscore = sharedPref.getInt("maxscore", 0);
                                        if (maxscore < c) {
                                            maxscore = c;
                                            editor.putInt("maxscore", maxscore);
                                            editor.apply();
                                        }
                                        TextView disp = (TextView) findViewById(R.id.disp);
                                        disp.setText("GAME OVER");
                                        TextView yscore = (TextView) findViewById(R.id.yourscore);
                                        yscore.setText("Your Score : " + c);
                                        yscore.setTextSize(20);
                                        TextView mscore = (TextView) findViewById(R.id.maxscore);
                                        mscore.setText("Max Score : " + maxscore);
                                        mscore.setTextSize(20);
                                    }
                                };

                                Handler h = new Handler();
                                h.postDelayed(r, 1000);


                            }
                        }
                    });


                }


            }
            start_timer();
        }

    }

    public void makeToast(){
        Toast.makeText(this, "Time Up", Toast.LENGTH_LONG).show();
    }

    public void playAgain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void start_timer()
    {    final TextView timer  = (TextView) findViewById(R.id.timer);
        timeo = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timer.setText("" + millisUntilFinished / 1000);
            time = 60 - (int) millisUntilFinished/1000;
            if (time >= 50){
                timer.setTextColor(Color.parseColor("#D50000"));
            }

        }

        @Override
        public void onFinish() {

            makeToast();

            setContentView(R.layout.activity_2);
            c = c-1;
            maxscore = sharedPref.getInt("maxscore", 0);
            if (maxscore < c){
                maxscore = c;
                editor.putInt("maxscore", maxscore);
                editor.apply();
            }
            TextView disp = (TextView) findViewById(R.id.disp);
            disp.setText("GAME OVER");
            TextView yscore = (TextView) findViewById(R.id.yourscore);
            yscore.setText("Your Score : " + c);
            yscore.setTextSize(20);
            TextView mscore = (TextView) findViewById(R.id.maxscore);
            mscore.setText("Max Score : " + maxscore);
            mscore.setTextSize(20);

        }
    }.start();


    }









}
