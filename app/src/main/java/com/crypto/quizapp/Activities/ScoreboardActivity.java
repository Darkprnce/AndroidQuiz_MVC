package com.crypto.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ScoreboardActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        setTitle("ScoreBoard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        language = getIntent().getStringExtra("lang");
        initview();
    }

    private void initview() {
        linearLayout = findViewById(R.id.lvl_Score);
        getQuestions();
    }

    private void getQuestions() {
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

    private void setdata(List<QuestionsBean> tasks) {
        linearLayout.removeAllViews();
        int lvl = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getLevel() > lvl) {
                lvl = tasks.get(i).getLevel();
                LayoutInflater inflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 20, 50, 30);

                final View view = inflater.inflate(R.layout.score_item_lay, null);
                final LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.lvl_linear_lay);
                layout1.setLayoutParams(params);

                TextView textView = view.findViewById(R.id.lvl_score_txt);
                String score = getLevelScore(tasks.get(i).getLevel());
                textView.setText("Level " + lvl + " : " + score);

                linearLayout.addView(view);
            }
        }
    }


    private String sedata(List<QuestionsBean> tasks) {
        int score = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getCorrectoption().equalsIgnoreCase(tasks.get(i).getUseranswer())) {
                score++;
            }
        }
        return score + "/" + tasks.size();
    }


    private String getLevelScore(final int lvl) {
        String score = null;
        class GetScore extends AsyncTask<Void, Void, List<QuestionsBean>> {

            @Override
            protected List<QuestionsBean> doInBackground(Void... voids) {
                List<QuestionsBean> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .questionDao()
                        .getSelectedQuestions(language, lvl);
                return taskList;
            }

            @Override
            protected void onPostExecute(List<QuestionsBean> tasks) {
                super.onPostExecute(tasks);

            }
        }

        GetScore getScore = new GetScore();
        try {
            List<QuestionsBean> questionsBeanList = getScore.execute().get();
            score = sedata(questionsBeanList);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return score;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
