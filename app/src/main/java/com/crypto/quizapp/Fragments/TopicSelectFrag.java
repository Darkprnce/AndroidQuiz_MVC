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
        mContext = getActivity(); // getting context
        mView = inflater.inflate(R.layout.fragment_topicselect, container, false); // defining the mView
        initview();
        return mView;
    }

    private void initview() {
        getActivity().setTitle("Topics"); // setting title in toolbar
        topic_recycler = mView.findViewById(R.id.topic_recycler);
        getTopics();
    }

    private void getTopics() {

        // getting the topics list

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
                List<String> topic_list = new ArrayList<>(); // topics array list
                for (int i = 0; i <questionsList.size() ; i++) {

                    if(i==0){
                        lang = questionsList.get(0).getTopic();
                        topic_list.add(lang); // adding value in topics list
                    }

                    if(!topic_list.contains(questionsList.get(i).getTopic())){
                        lang = questionsList.get(i).getTopic();
                        topic_list.add(lang); // adding value in topics list

                    }
                }

                // defining the linearlayoutManager for recyclerview
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);

                // setting linearlayoutManager to recyclerview
                topic_recycler.setLayoutManager(linearLayoutManager);

                // defining custom adapter for recyclerView
                TopicsAdapter topicsAdapter = new TopicsAdapter(mContext,topic_list);

                // setting adpter to the recyclerview
                topic_recycler.setAdapter(topicsAdapter);

            }
        }

        GetTopics gt = new GetTopics();
        gt.execute();
    }


}
