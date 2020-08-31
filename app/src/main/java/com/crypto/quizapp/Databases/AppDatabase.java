package com.crypto.quizapp.Databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Beans.UsersBean;

@Database(entities = {QuestionsBean.class, UsersBean.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuestionDao questionDao();
    public abstract UsersDao usersDao();
}
