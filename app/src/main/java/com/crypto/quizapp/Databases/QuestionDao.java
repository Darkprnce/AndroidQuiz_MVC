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

    // getting all the questions in the database

    @Query("SELECT * FROM QuestionsBean")
    List<QuestionsBean> getAll();


    // getting all the questions in the database filtered on the basis of topic and level

    @Query("SELECT * FROM QuestionsBean WHERE topic IN(:lang) AND level IN(:lvl)")
    List<QuestionsBean> getSelectedQuestions(String lang, int lvl);


    // getting all the levels in the database filtered on the basis of topic

    @Query("SELECT * FROM QuestionsBean WHERE topic IN(:lang)")
    List<QuestionsBean> getLevels(String lang);


    // deleting all the questions in the database

    @Query("DELETE FROM QuestionsBean")
    void deleteAllQuestions();


    // to add a question in the database

    @Insert
    void insert(QuestionsBean questionsBean);


    // to delete a question from the database

    @Delete
    void delete(QuestionsBean questionsBean);


    // update the question in the database

    @Update
    void update(QuestionsBean questionsBean);



}
