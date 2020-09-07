package com.crypto.quizapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crypto.quizapp.Adapters.QuestionsAdapter;
import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner topic_spinner;
    private Spinner level_spinner;
    private EditText question_edt;
    private EditText option_one_edt;
    private EditText option_two_edt;
    private EditText option_three_edt;
    private EditText option_four_edt;
    private RadioButton option_one_rb;
    private RadioButton option_two_rb;
    private RadioButton option_three_rb;
    private RadioButton option_four_rb;
    private RadioGroup correct_grp;
    private Button add_question_btn;

    private List<String> topics_list;
    private List<String> levels_list;
    private ArrayAdapter<String> topics_adapter;
    private ArrayAdapter<String> levels_adapter;
    private String topic_st, level_st, correct_st, question_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        setTitle("Add Questions"); // setting title of the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // setting the back press icon in toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); // setting the back press icon in toolbar

        initView();
    }

    private void initView() {
        topic_spinner = (Spinner) findViewById(R.id.topic_spinner);
        level_spinner = (Spinner) findViewById(R.id.level_spinner);
        question_edt = (EditText) findViewById(R.id.question_edt);
        option_one_edt = (EditText) findViewById(R.id.option_one_edt);
        option_two_edt = (EditText) findViewById(R.id.option_two_edt);
        option_three_edt = (EditText) findViewById(R.id.option_three_edt);
        option_four_edt = (EditText) findViewById(R.id.option_four_edt);
        option_one_rb = (RadioButton) findViewById(R.id.option_one_rb);
        option_two_rb = (RadioButton) findViewById(R.id.option_two_rb);
        option_three_rb = (RadioButton) findViewById(R.id.option_three_rb);
        option_four_rb = (RadioButton) findViewById(R.id.option_four_rb);
        correct_grp = (RadioGroup) findViewById(R.id.correct_grp);
        add_question_btn = (Button) findViewById(R.id.add_question_btn);
        add_question_btn.setOnClickListener(this); // adding onclicklistener on addquestionbtn


        option_one_rb.setChecked(true); // selecting the first option radio button By default, you can change to whatever radio button you like

        // adding Textwatcher so the values in edittext will be displayed on the radio buttons
        option_one_edt.addTextChangedListener(new MyTextWacther(option_one_edt));
        option_two_edt.addTextChangedListener(new MyTextWacther(option_two_edt));
        option_three_edt.addTextChangedListener(new MyTextWacther(option_three_edt));
        option_four_edt.addTextChangedListener(new MyTextWacther(option_four_edt));

        getTopics();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_question_btn:
                submit();
                break;

        }
    }

    private void submit() {

        // validations

        if (TextUtils.isEmpty(question_edt.getText().toString().trim())) {
            question_edt.setError("Please enter a question");
            requestFocus(question_edt);
        } else if (TextUtils.isEmpty(option_one_edt.getText().toString().trim())) {
            option_one_edt.setError("Please enter value of option 1");
            requestFocus(option_one_edt);
        } else if (TextUtils.isEmpty(option_two_edt.getText().toString().trim())) {
            option_two_edt.setError("Please enter value of option 2");
            requestFocus(option_two_edt);
        } else if (TextUtils.isEmpty(option_three_edt.getText().toString().trim())) {
            option_three_edt.setError("Please enter value of option 3");
            requestFocus(option_two_edt);
        } else if (TextUtils.isEmpty(option_four_edt.getText().toString().trim())) {
            option_four_edt.setError("Please enter value of option 4");
            requestFocus(option_four_edt);
        } else {
            setdata();
        }
    }

    private void setdata() {

        //getting user data

        if (option_one_rb.isChecked()) {
            correct_st = option_one_rb.getText().toString().trim();
        } else if (option_two_rb.isChecked()) {
            correct_st = option_two_rb.getText().toString().trim();
        } else if (option_three_rb.isChecked()) {
            correct_st = option_three_rb.getText().toString().trim();
        } else if (option_four_rb.isChecked()) {
            correct_st = option_four_rb.getText().toString().trim();
        }

        QuestionsBean questionsBean = new QuestionsBean();
        questionsBean.setTopic(topic_st);
        questionsBean.setLevel(Integer.parseInt(level_st));
        questionsBean.setCleared("no");
        questionsBean.setQuestion(question_edt.getText().toString().trim());
        questionsBean.setOption1(option_one_edt.getText().toString().trim());
        questionsBean.setOption2(option_two_edt.getText().toString().trim());
        questionsBean.setOption3(option_three_edt.getText().toString().trim());
        questionsBean.setOption4(option_four_edt.getText().toString().trim());
        questionsBean.setCorrectoption(correct_st);
        questionsBean.setUseranswer("");
        questionsBean.setQuestionno(question_no);

        AddQuestions(questionsBean);
    }

    private void getTopics() {

        //getting the list of all the topics

        class GetTopics extends AsyncTask<Void, Void, List<QuestionsBean>> {

            @Override
            protected List<QuestionsBean> doInBackground(Void... voids) {
                List<QuestionsBean> taskList = DatabaseClient
                        .getInstance(AddQuestionActivity.this)
                        .getAppDatabase()
                        .questionDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<QuestionsBean> questionsList) {
                super.onPostExecute(questionsList);

                String lang = null;
                topics_list = new ArrayList<>(); //topics array list
                for (int i = 0; i < questionsList.size(); i++) {

                    if (i == 0) {
                        lang = questionsList.get(0).getTopic();
                        topics_list.add(lang); // adding topic to array list
                    }

                    if (!topics_list.contains(questionsList.get(i).getTopic())) {
                        lang = questionsList.get(i).getTopic();
                        topics_list.add(lang); // adding topic to array list
                    }
                }

                topics_list.add("Add other"); // adding an extra value

                // defining the adapter for topic spinner
                topics_adapter = new ArrayAdapter<String>(AddQuestionActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, topics_list);

                //setting the adapter to the topic spinner
                topic_spinner.setAdapter(topics_adapter);

                // listener for topic spinner
                topic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long d) {
                        String selected = parent.getItemAtPosition(position).toString();

                        // if the selected value is (add other) then display the dialog box
                        // if not then select the value in spinner and get predefined levels
                        if (selected.equalsIgnoreCase("Add other")) {
                            showTopicDialog();
                        } else {
                            topic_st = selected;
                            getLevels(topic_st);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }

        GetTopics gt = new GetTopics();
        gt.execute();
    }

    private void getLevels(final String language) {

        //  getting level based on the topics selected

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
                levels_list = new ArrayList<>(); // level array list
                int lvl = 0;
                for (int i = 0; i < questionsBeanList.size(); i++) {
                    if (questionsBeanList.get(i).getLevel() > lvl) {
                        lvl = questionsBeanList.get(i).getLevel();
                        levels_list.add("Level " + lvl);  // adding levels to the level array list
                    }
                }

                levels_list.add("Add Level"); // adding extra values

                // defining the level adapter
                levels_adapter = new ArrayAdapter<String>(AddQuestionActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, levels_list);

                // setting the adapter for level spinner
                level_spinner.setAdapter(levels_adapter);

                // listener for level spinner
                level_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long d) {
                        String selected = parent.getItemAtPosition(position).toString();

                        //if the selected value is (add level) then add another level to the list and set the adapter

                        if (selected.equalsIgnoreCase("Add Level")) {
                            levels_list.add((levels_list.size() == 0 ? 0 : levels_list.size() - 1), "Level " + levels_list.size());
                            levels_adapter = new ArrayAdapter<String>(AddQuestionActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, levels_list);
                            level_spinner.setAdapter(levels_adapter);
                            level_spinner.setSelection(levels_list.size() - 2, false); // selecting the new adding level
                        } else {
                            String[] separated = selected.split(" ");
                            level_st = separated[1];
                            getQuestions(language, Integer.parseInt(level_st));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }

        GetLevels getLevels = new GetLevels();
        getLevels.execute();
    }

    private void getQuestions(final String languagevalue, final int levelvalue) {

        //getting the questions list from level, so we can get the next question no.

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
                // if the list is empty then the question no is 1
                // if the list is not empty then question no will be next value from the last
                if (questionsBeanList.size() == 0) {
                    question_no = "1";
                } else {
                    question_no = String.valueOf(questionsBeanList.size() + 1);
                }

            }
        }

        GetQuestions getQuestions = new GetQuestions();
        getQuestions.execute();
    }

    private void AddQuestions(final QuestionsBean questionsBean) {

        // adding question to the database

        class Addquestions extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(AddQuestionActivity.this).getAppDatabase()
                        .questionDao()
                        .insert(questionsBean);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                // toast to display after adding the question to the database

                Toast.makeText(AddQuestionActivity.this, "Question Added", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }

        Addquestions addquestions = new Addquestions();
        addquestions.execute();
    }


    private void showTopicDialog() {

        // custom dialog to add new topic

        final Dialog dialog = new Dialog(AddQuestionActivity.this);
        dialog.setContentView(R.layout.add_topic_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);

        Button add_btn = dialog.findViewById(R.id.add_question_btn);
        ImageView close_btn = (ImageView) dialog.findViewById(R.id.close);

        final EditText topic_edt = dialog.findViewById(R.id.topic_edt);
        topic_edt.setFocusable(true);

        RelativeLayout forgot_main = (RelativeLayout) dialog.findViewById(R.id.forgot_main);
        forgot_main.getBackground().setAlpha(1);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validations

                if (TextUtils.isEmpty(topic_edt.getText().toString())) {
                    topic_edt.setError("Please add a topic first");
                } else {
                    topics_list.add((topics_list.size() == 0 ? 0 : topics_list.size() - 1), topic_edt.getText().toString().trim());
                    topics_adapter = new ArrayAdapter<String>(AddQuestionActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, topics_list);
                    topic_spinner.setAdapter(topics_adapter);

                    topic_spinner.setSelection(topics_list.size() - 2, false); // selecting the new added value in spinner
                    dialog.dismiss(); // dismiss the dialog after adding
                }
            }
        });


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topic_spinner.setSelection(0); // selecting the first value if nothing is added
                dialog.dismiss(); // dismiss dialog
            }
        });
    }


    private class MyTextWacther implements TextWatcher {
        private View view;

        public MyTextWacther(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                // defining the textwatcher for options Edittext

                case R.id.option_one_edt:
                    validateOptionOne();
                    break;
                case R.id.option_two_edt:
                    validateOptionTwo();
                    break;
                case R.id.option_three_edt:
                    validateOptionThree();
                    break;
                case R.id.option_four_edt:
                    validateOptionFour();
                    break;

            }

        }

    }

    private boolean validateOptionOne() {
        String option_one_st = option_one_edt.getText().toString().trim();
        if (!option_one_st.isEmpty()) {
            option_one_rb.setText(option_one_st);
            return false;
        } else {
            option_one_rb.setText("N/A");
        }
        return true;
    }

    private boolean validateOptionTwo() {
        String option_two_st = option_two_edt.getText().toString().trim();
        if (!option_two_st.isEmpty()) {
            option_two_rb.setText(option_two_st);
            return false;
        } else {
            option_two_rb.setText("N/A");
        }
        return true;
    }

    private boolean validateOptionThree() {
        String option_three_st = option_three_edt.getText().toString().trim();
        if (!option_three_st.isEmpty()) {
            option_three_rb.setText(option_three_st);
            return false;
        } else {
            option_three_rb.setText("N/A");
        }
        return true;
    }

    private boolean validateOptionFour() {
        String option_four_st = option_four_edt.getText().toString().trim();
        if (!option_four_st.isEmpty()) {
            option_four_rb.setText(option_four_st);
            return false;
        } else {
            option_four_rb.setText("N/A");
        }
        return true;
    }

    private void requestFocus(View view) {
        // getting the focus on EditText
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddQuestionActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();  // toolbar back button functionality
        return super.onSupportNavigateUp();
    }
}