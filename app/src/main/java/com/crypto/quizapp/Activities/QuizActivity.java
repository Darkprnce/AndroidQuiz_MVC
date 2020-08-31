package com.crypto.quizapp.Activities;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.R;
import com.crypto.quizapp.Adapters.QuestionsAdapter;


import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView quiz_Recyclerview;
    private String languagevalue;
    private int levelvalue;
    private Button btn_submit;
    private List<QuestionsBean> questionslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        levelvalue = getIntent().getIntExtra("lvl", 0);
        languagevalue = getIntent().getStringExtra("lang");
        setTitle("Level "+levelvalue);
        initView();
    }

    private void initView() {
        quiz_Recyclerview = findViewById(R.id.quiz_recyclerview);
        quiz_Recyclerview.setHasFixedSize(true);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        getQuestions();
    }


    private void getQuestions() {
        class GetQuestions extends AsyncTask<Void, Void, List<QuestionsBean>> {

            @Override
            protected List<QuestionsBean> doInBackground(Void... voids) {
                List<QuestionsBean> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .questionDao()
                        .getSelectedQuestions(languagevalue, levelvalue);
                return taskList;
            }

            @Override
            protected void onPostExecute(List<QuestionsBean> questionsBeanList) {
                super.onPostExecute(questionsBeanList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                quiz_Recyclerview.setLayoutManager(linearLayoutManager);

                QuestionsAdapter qadapter = new QuestionsAdapter(QuizActivity.this, questionsBeanList);
                quiz_Recyclerview.setAdapter(qadapter);
            }
        }

        GetQuestions getQuestions = new GetQuestions();
        getQuestions.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                Intent i = new Intent(QuizActivity.this, ResultActivity.class);
                i.putExtra("lang", languagevalue);
                i.putExtra("lvl", levelvalue);
                startActivity(i);
                finish();
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

