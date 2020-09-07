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


    // getting all the users in the database
    @Query("SELECT * FROM UsersBean")
    List<UsersBean> getAll();

    // checking if the user exist in the database or not
    @Query("SELECT * FROM UsersBean WHERE email IN(:email) AND password IN(:pass)")
    boolean checkUser(String email, String pass);

    // delete all the users from the database
    @Query("DELETE FROM UsersBean")
    void deleteAllUsers();

    // add a user in the database
    @Insert
    void insert(UsersBean usersBean);

    // delete a user from the database
    @Delete
    void delete(UsersBean usersBean);

    // update the user from the database
    @Update
    void update(UsersBean usersBean);

}
