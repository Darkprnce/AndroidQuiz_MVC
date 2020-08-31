package com.crypto.quizapp.Databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Beans.UsersBean;

import java.util.List;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM UsersBean")
    List<UsersBean> getAll();

    @Query("SELECT * FROM UsersBean WHERE email IN(:email) AND password IN(:pass)")
    boolean checkUser(String email, String pass);

    @Query("DELETE FROM UsersBean")
    void deleteAllUsers();

    @Insert
    void insert(UsersBean usersBean);

    @Delete
    void delete(UsersBean usersBean);

    @Update
    void update(UsersBean usersBean);

}
