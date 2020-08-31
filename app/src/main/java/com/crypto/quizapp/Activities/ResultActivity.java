package com.crypto.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.R;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView language_txt;
    private TextView lvl_txt;
    private TextView correct_txt;
    private TextView wrong_txt;
    private TextView not_ans_txt;
    private Button btn_finish, btn_nextlvl;
    private String language;
    private int level;
    private int correct_count;
    private int wrong_count;
    private int not_ans_count;
    private int total_count;
    private int passing_marks;
    private List<Integer> levels_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Score");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        language = getIntent().getStringExtra("lang");
        level = getIntent().getIntExtra("lvl", 0);

        initView();
    }

    private void initView() {
        language_txt = findViewById(R.id.lang_txt);
        lvl_txt = findViewById(R.id.lvl_count);
        correct_txt = findViewById(R.id.correct_count);
        wrong_txt = findViewById(R.id.wrong_count);
        not_ans_txt = findViewById(R.id.not_answered_count);
        btn_finish = findViewById(R.id.finish_btn);
        btn_finish.setOnClickListener(this);
        btn_nextlvl = findViewById(R.id.nxt_lvl_btn);
        btn_nextlvl.setOnClickListener(this);
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
                        .getSelectedQuestions(language, level);
                return questionsBeanList;
            }

            @Override
            protected void onPostExecute(List<QuestionsBean> questionsBeans) {
                super.onPostExecute(questionsBeans);
                getresult(questionsBeans);
            }
        }

        GetQuestions getQuestions = new GetQuestions();
        getQuestions.execute();
    }

    private void updateQuestions(final List<QuestionsBean> questionsBeanList) {
        class updateQuestions extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < questionsBeanList.size(); i++) {
                    questionsBeanList.get(i).setCleared("yes");
                    DatabaseClient.getInstance(ResultActivity.this).getAppDatabase()
                            .questionDao()
                            .update(questionsBeanList.get(i));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getLevels();
            }
        }

        updateQuestions updateQuestions = new updateQuestions();
        updateQuestions.execute();
    }

    private void getLevels() {
        class GetLevels extends AsyncTask<Void, Void, List<QuestionsBean>> {

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
                setLevel(questionsBeanList);
            }
        }

        GetLevels getLevels = new GetLevels();
        getLevels.execute();
    }


    private void setLevel(List<QuestionsBean> questionsBeanList) {
        levels_list = new ArrayList<>();
        int lvl = 0;
        for (int i = 0; i < questionsBeanList.size(); i++) {
            if (questionsBeanList.get(i).getLevel() > lvl) {
                lvl = questionsBeanList.get(i).getLevel();
                levels_list.add(lvl);
            }
        }

        if(levels_list.contains(level+1)){
            btn_nextlvl.setVisibility(View.VISIBLE);
        }else {
            btn_nextlvl.setVisibility(View.GONE);
        }
    }


    private void getresult(List<QuestionsBean> questionsBeanList) {
        for (int i = 0; i < questionsBeanList.size(); i++) {
            total_count++;
            if (questionsBeanList.get(i).getUseranswer().equalsIgnoreCase(questionsBeanList.get(i).getCorrectoption())) {
                correct_count++;
            } else {
                if (questionsBeanList.get(i).getUseranswer().equalsIgnoreCase("-1")) {
                    not_ans_count++;
                } else {
                    wrong_count++;
                }
            }
        }

        passing_marks = (60 / 100) * total_count;

        correct_txt.setText("" + correct_count);
        wrong_txt.setText("" + wrong_count);
        not_ans_txt.setText("" + not_ans_count);
        language_txt.setText(language);
        lvl_txt.setText("Level " + level);

        updateQuestions(questionsBeanList);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_btn:
                Intent i = new Intent(ResultActivity.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                break;
            case R.id.nxt_lvl_btn:
                if (correct_count < passing_marks) {
                    Toast.makeText(this, "atleat 6 answers should be correct", Toast.LENGTH_SHORT).show();
                } else {
                    Intent z = new Intent(ResultActivity.this, QuizActivity.class);
                    z.putExtra("lang", language);
                    z.putExtra("lvl", level+1);
                    startActivity(z);
                    finish();
                }
                break;
        }
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
