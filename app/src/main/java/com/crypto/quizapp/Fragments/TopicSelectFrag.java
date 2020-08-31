package com.crypto.quizapp.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.quizapp.Adapters.TopicsAdapter;
import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.R;

import java.util.ArrayList;
import java.util.List;

public class TopicSelectFrag extends Fragment {

    private View mView;
    private Context mContext;
    private RecyclerView topic_recycler;

    public static TopicSelectFrag newInstance() {

        Bundle args = new Bundle();

        TopicSelectFrag fragment = new TopicSelectFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_topicselect, container, false);
        initview();
        return mView;
    }

    private void initview() {
        getActivity().setTitle("Topics");
        topic_recycler = mView.findViewById(R.id.topic_recycler);
        getTopics();
    }

    private void getTopics() {
        class GetTopics extends AsyncTask<Void, Void, List<QuestionsBean>> {

            @Override
            protected List<QuestionsBean> doInBackground(Void... voids) {
                List<QuestionsBean> taskList = DatabaseClient
                        .getInstance(mContext)
                        .getAppDatabase()
                        .questionDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<QuestionsBean> questionsList) {
                super.onPostExecute(questionsList);
                String lang = null;
                List<String> topic_list = new ArrayList<>();
                for (int i = 0; i <questionsList.size() ; i++) {

                    if(i==0){
                        lang = questionsList.get(0).getTopic();
                        topic_list.add(lang);
                    }

                    if(!topic_list.contains(questionsList.get(i).getTopic())){
                        lang = questionsList.get(i).getTopic();
                        topic_list.add(lang);

                    }
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                topic_recycler.setLayoutManager(linearLayoutManager);
                TopicsAdapter topicsAdapter = new TopicsAdapter(mContext,topic_list);
                topic_recycler.setAdapter(topicsAdapter);

            }
        }

        GetTopics gt = new GetTopics();
        gt.execute();
    }


}
