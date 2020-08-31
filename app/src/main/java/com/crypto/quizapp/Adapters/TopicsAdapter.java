package com.crypto.quizapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.quizapp.Activities.LevelSelectActivity;
import com.crypto.quizapp.R;

import java.util.List;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {
    private Context mContext;
    public List<String> questions;

    public TopicsAdapter(Context applicationcontext, List<String> questions) {
        this.mContext = applicationcontext;
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(mContext).inflate(R.layout.topic_item_lay, parent, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.topic_btn.setText(questions.get(position));
        holder.topic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, LevelSelectActivity.class);
                i.putExtra("lang",questions.get(position));
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button topic_btn;

        public ViewHolder(final View view) {
            super(view);
            topic_btn = view.findViewById(R.id.topic_btn);
        }
    }
}


