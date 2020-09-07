package com.crypto.quizapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.R;

import java.util.List;

public class LevelSelectActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout linearLayout;
    private String language;
    private Button score_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelselect_activity);
        setTitle("Levels");                                             // setting the title on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);         // enabling the back button on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);         // enabling the back button on toolbar

        language = getIntent().getStringExtra("lang");   // getting the selected topic value from previous fragment
        initView();

    }

    private void initView() {
        linearLayout = findViewById(R.id.lvl_linear);
        score_board = findViewById(R.id.btn_scoreboard);
        score_board.setOnClickListener(this);
        getQuestions();
    }

    private void getQuestions() {

        // getting levels

        class GetQuestions extends AsyncTask<Void, Void, List<QuestionsBean>> {

            @Override
            protected List<QuestionsBean> doInBackground(Void... voids) {
                List<QuestionsBean> questionsBeanList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .questionDao()
                        .getLevels(language);
                return questionsBeanList;
            }

            @Override
            protected void onPostExecute(List<QuestionsBean> questionsBeanList) {
                super.onPostExecute(questionsBeanList);
                setdata(questionsBeanList);
            }
        }

        GetQuestions getQuestions = new GetQuestions();
        getQuestions.execute();
    }


    private void setdata(List<QuestionsBean> questionsBeanList) {

        // dynamic layouts
        //  clear linear layout before adding view to the root layout

        linearLayout.removeAllViews();
        int lvl = 0;
        for (int i = 0; i < questionsBeanList.size(); i++) {
            if (questionsBeanList.get(i).getLevel() > lvl) {

                lvl = questionsBeanList.get(i).getLevel();

                // layout inflater to add views
                LayoutInflater inflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                // layout parameter needed for views
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 20, 50, 30);

                // custom view to be created dynamically

                final View view = inflater.inflate(R.layout.level_select_item_lay, null);

                // parent layout
                final LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.lvl_linear_lay);
                layout1.setLayoutParams(params);

                Button button = view.findViewById(R.id.lvl_select_btn);
                button.setText("Level " + lvl);   // setting level value to the button
                final int finalLvl1 = lvl;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // moving to the quiz activity

                        Intent a = new Intent(LevelSelectActivity.this, QuizActivity.class);
                        a.putExtra("lvl", finalLvl1);
                        a.putExtra("lang", language);
                        startActivity(a);
                    }
                });


                // checking if the previous level is cleared or not
                // if cleared then allow the next level
                // if not then disable the next level button

                if (questionsBeanList.get(i).getCleared().equalsIgnoreCase("no")) {
                    int pos = 0;
                    if (i - 1 > 0) {
                        pos=i-1;
                        if (questionsBeanList.get(pos).getCleared().equalsIgnoreCase("no")) {
                            button.setEnabled(false);
                            button.setBackgroundTintList(getResources().getColorStateList(R.color.light_green));
                            button.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            button.setEnabled(true);
                            button.setTextColor(getResources().getColor(R.color.white));
                            button.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                        }
                    }else {
                        button.setEnabled(true);
                        button.setTextColor(getResources().getColor(R.color.white));
                        button.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                    }
                } else {
                    button.setEnabled(true);
                    button.setTextColor(getResources().getColor(R.color.white));
                    button.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                }

                // adding the view to the root layout
                linearLayout.addView(view);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_scoreboard:

                // moving to the Scoreboard Activity

                Intent d = new Intent(LevelSelectActivity.this, ScoreboardActivity.class);
                d.putExtra("lang", language);
                startActivity(d);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();   // toolbar back button functionality
        return super.onSupportNavigateUp();
    }
}
