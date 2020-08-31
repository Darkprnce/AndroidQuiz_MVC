package com.crypto.quizapp.Databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.crypto.quizapp.Beans.QuestionsBean;

import java.util.List;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM QuestionsBean")
    List<QuestionsBean> getAll();

    @Query("SELECT * FROM QuestionsBean WHERE topic IN(:lang) AND level IN(:lvl)")
    List<QuestionsBean> getSelectedQuestions(String lang, int lvl);

    @Query("SELECT * FROM QuestionsBean WHERE topic IN(:lang)")
    List<QuestionsBean> getLevels(String lang);

    @Query("DELETE FROM QuestionsBean")
    void deleteAllQuestions();

    @Insert
    void insert(QuestionsBean questionsBean);

    @Delete
    void delete(QuestionsBean questionsBean);

    @Update
    void update(QuestionsBean questionsBean);



}
