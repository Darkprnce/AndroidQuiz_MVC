package com.crypto.quizapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.R;
import com.crypto.quizapp.helper.CommonSharedPreference;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_signin;
    private LinearLayout btn_signup;
    private EditText email_edt;
    private EditText pass_edt;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();   // hide toolbar
        getWindow().setStatusBarColor(this.getColor(R.color.lightpink));
        initViews();
    }


    private void initViews() {
        email_edt = findViewById(R.id.email_edt);
        pass_edt = findViewById(R.id.pass_edt);
        btn_signin = findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(this);
        btn_signup = findViewById(R.id.signup_btn_signin);
        btn_signup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_btn_signin:

                // moving from Signinactivity to SignupActivity

                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
                break;
            case R.id.btn_signin:
                submit();
                break;
        }
    }

    private void submit() {

        // validations

        if (!validateEmail()) {
            return;
        } else if (!validatepassword()) {
            return;
        } else {
            signInUser();
        }
    }

    private void signInUser() {

        final String email = email_edt.getText().toString().trim();
        final String pass = pass_edt.getText().toString().trim();

        // Checking if the user exist in the database

        class SignInUser extends AsyncTask<Void, Void, Boolean> {

            @Override
            protected Boolean doInBackground(Void... voids) {

               boolean usr_exist = DatabaseClient.getInstance(SignInActivity.this).getAppDatabase()
                        .usersDao()
                        .checkUser(email,pass);
                return usr_exist;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean){
                    CommonSharedPreference.setsharedText(SignInActivity.this,"logged","yes");
                    Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(SignInActivity.this, "user does not exist from this email and password", Toast.LENGTH_SHORT).show();
                }
            }
        }

        SignInUser signInUser = new SignInUser();
        signInUser.execute();
    }

    private boolean validatepassword() {
        String pass_st = pass_edt.getText().toString().trim();
        if (pass_st.isEmpty()) {
            pass_edt.setError("Please enter your password");
            requestFocus(pass_edt);
            return false;
        } else if (pass_st.length() < 6) {
            pass_edt.setError("Password must be of 6 digits");
            requestFocus(pass_edt);
            return false;
        } else {
            pass_edt.setError(null);
            pass_edt.clearFocus();
        }
        return true;
    }


    private boolean validateEmail() {
        String emailString = email_edt.getText().toString().trim();
        if (TextUtils.isEmpty(emailString)) {
            email_edt.setError("Please enter your email");
            requestFocus(email_edt);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email_edt.setError("Please enter a valid email");
            requestFocus(email_edt);
            return false;
        } else {
            email_edt.setError(null);
            email_edt.clearFocus();
        }
        return true;
    }

    private void requestFocus(View view) {
        // getting focus on editText
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
