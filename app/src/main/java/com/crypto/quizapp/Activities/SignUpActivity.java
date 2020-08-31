package com.crypto.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.crypto.quizapp.Beans.UsersBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.helper.CommonSharedPreference;
import com.crypto.quizapp.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_signup;
    private LinearLayout btn_signup_signin;
    private EditText email_edt;
    private EditText pass_edt;
    private EditText name_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.lightpink));
        initviews();
    }

    private void initviews() {
        email_edt = findViewById(R.id.email_edt);
        pass_edt = findViewById(R.id.pass_edt);
        name_edt = findViewById(R.id.name_edt);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
        btn_signup_signin = findViewById(R.id.signin_btn_signup);
        btn_signup_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_btn_signup:
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
                break;
            case R.id.btn_signup:
                submit();
                break;
        }
    }

    private void submit() {
        if (!validateEmail()) {
            return;
        } else if (!validatepassword()) {
            return;
        } else if (!validatename()) {
            return;
        } else {
            registerUser();
        }
    }

    private void registerUser() {

        final UsersBean usersBean = new UsersBean();
        usersBean.setEmail(email_edt.getText().toString().trim());
        usersBean.setPassword(pass_edt.getText().toString().trim());
        usersBean.setName(name_edt.getText().toString().trim());

        class RegisterUser extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(SignUpActivity.this).getAppDatabase()
                        .usersDao()
                        .insert(usersBean);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                CommonSharedPreference.setsharedText(SignUpActivity.this,"logged","yes");
                Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }

        RegisterUser registerUser = new RegisterUser();
        registerUser.execute();


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

    private boolean validatename() {
        String name_st = name_edt.getText().toString().trim();
        if (TextUtils.isEmpty(name_st)) {
            name_edt.setError("Please enter your name");
            requestFocus(name_edt);
            return false;
        } else {
            name_edt.setError(null);
            name_edt.clearFocus();
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
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
