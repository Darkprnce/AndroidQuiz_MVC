package com.crypto.quizapp.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.R;


import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private Context context;
    public List<QuestionsBean> questions;
    int clickedPos = 0;
    int radioButtonID = 0;
    private String optionvalue = "";

    public QuestionsAdapter(Context applicationcontext, List<QuestionsBean> questions) {
        this.context = applicationcontext;
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.quiz_layout, parent, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_quesno.setText("Q." + questions.get(position).getQuestionno() + " ");
        holder.tv_ques.setText(questions.get(position).getQuestion());
        holder.rb_option1.setText(questions.get(position).getOption1());
        holder.rb_option2.setText(questions.get(position).getOption2());
        holder.rb_option3.setText(questions.get(position).getOption3());
        holder.rb_option4.setText(questions.get(position).getOption4());


        holder.answer_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                clickedPos = (int) radioGroup.getTag();
                radioButtonID = holder.answer_grp.getCheckedRadioButtonId();
                questions.get(clickedPos).setSelectedRadioButton(radioButtonID);

                if (holder.rb_option1.isChecked()) {
                    optionvalue = holder.rb_option1.getText().toString();
                } else if (holder.rb_option2.isChecked()) {
                    optionvalue = holder.rb_option2.getText().toString();
                } else if (holder.rb_option3.isChecked()) {
                    optionvalue = holder.rb_option3.getText().toString();
                } else if (holder.rb_option4.isChecked()) {
                    optionvalue = holder.rb_option4.getText().toString();
                } else if (!holder.rb_option1.isChecked() && !holder.rb_option2.isChecked() && !holder.rb_option3.isChecked() && !holder.rb_option4.isChecked()) {
                    optionvalue = "-1";
                }

                questions.get(position).setUseranswer(optionvalue);
                updateTask(questions.get(position));

            }
        });

        holder.answer_grp.setTag(position);
        if (questions.get(position).getSelectedRadioButton() != -1) {
            holder.answer_grp.check(questions.get(position).getSelectedRadioButton());

        } else {
            holder.answer_grp.clearCheck();

        }
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_quesno, tv_ques;
        RadioGroup answer_grp;
        RadioButton rb_option1, rb_option2, rb_option3, rb_option4;

        public ViewHolder(final View view) {
            super(view);
            tv_quesno = view.findViewById(R.id.txtview_ques_no);
            tv_ques = view.findViewById(R.id.txtview_ques);
            answer_grp = (RadioGroup) view.findViewById(R.id.answer_radiogroup);
            rb_option1 = view.findViewById(R.id.option1);
            rb_option2 = view.findViewById(R.id.option2);
            rb_option3 = view.findViewById(R.id.option3);
            rb_option4 = view.findViewById(R.id.option4);
        }
    }

    private void updateTask(final QuestionsBean questionsBean) {

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(context).getAppDatabase()
                        .questionDao()
                        .update(questionsBean);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("update", "onPostExecute: ");
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    private void deleteTask(final QuestionsBean questionsBean) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .questionDao()
                        .delete(questionsBean);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

}

