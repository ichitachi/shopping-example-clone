package com.teikaiz.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.teikaiz.quiz.firebase.UsersService;
import com.teikaiz.quiz.prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button joinNowButton = findViewById(R.id.main_join_now_btn);
        Button loginButton = findViewById(R.id.main_login_btn);
        this.onClickListener(loginButton, LoginActivity.class);
        this.onClickListener(joinNowButton, RegisterActivity.class);
        Paper.init(this);
        this.loadAllowAccessRememberMe();
    }

    private void onClickListener(Button button, final Class<?> activity) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity);
                startActivity(intent);
            }
        });
    }

    private void loadAllowAccessRememberMe() {
        String phone = Paper.book().read(Prevalent.PAPER_PHONE_KEY);
        String pwd = Paper.book().read(Prevalent.PAPER_PWD_KEY);
        String dbName = Paper.book().read(Prevalent.PAPER_DB_NAME_KEY);
        if(TextUtils.isEmpty(phone) && TextUtils.isEmpty(pwd)) return;
        ProgressDialog progressDialog = ProgressDialog.show(this, "Already logged in", "Please wait, while we are checking the credentials.");
        UsersService.getInstance()
                .init(dbName)
                .login(phone, pwd, this, progressDialog, true);
    }
}