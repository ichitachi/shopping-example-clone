package com.teikaiz.quiz;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.rey.material.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.teikaiz.quiz.firebase.UsersService;
import com.teikaiz.quiz.share.Constant;
import com.teikaiz.quiz.share.EditTextUtils;
import com.teikaiz.quiz.share.ValidateUtils;

import io.paperdb.Paper;

@RequiresApi(api = Build.VERSION_CODES.N)
public class LoginActivity extends AppCompatActivity {
    private EditText phoneEditText, pwdEditText;
    private CheckBox chbRememberMe;
    private String dbName = Constant.DB_USERS;
    private Button btnLogin;
    private TextView linkAdmin, linkNotAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneEditText = findViewById(R.id.login_phone_number_input);
        pwdEditText = findViewById(R.id.login_password_input);
        btnLogin = findViewById(R.id.login_btn);
        chbRememberMe = findViewById(R.id.remember_me_chkb);
        linkAdmin = findViewById(R.id.link_admin);
        linkNotAdmin = findViewById(R.id.link_not_admin);
        init("Login", View.VISIBLE, View.INVISIBLE, Constant.DB_USERS);
        Paper.init(this);
        this.onBtnLoginClick(btnLogin);
        this.onLinkAdminClick(linkAdmin);
        this.onLinkNotAdminClick(linkNotAdmin);
    }

    private void init(String btnLoginText, int linkAdminVisibility, int linkNotAdminVisibility, String dbName) {
        btnLogin.setText(btnLoginText);
        linkAdmin.setVisibility(linkAdminVisibility);
        linkNotAdmin.setVisibility(linkNotAdminVisibility);
        this.dbName = dbName;
    }

    private void onLinkAdminClick(TextView linkAdmin) {
        linkAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init("Admin Login", View.INVISIBLE, View.VISIBLE, Constant.DB_ADMINS);
            }
        });
    }

    private void onLinkNotAdminClick(TextView linkNotAdmin) {
        linkNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init("Login", View.VISIBLE, View.INVISIBLE, Constant.DB_USERS);
            }
        });
    }

    private void onBtnLoginClick(Button btnLogin) {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    private void login() {
         String phone = EditTextUtils.getText(phoneEditText);
         String pwd = EditTextUtils.getText(pwdEditText);
         if(ValidateUtils.validate(null, phone, pwd, this, false)) {
             ProgressDialog progressDialog = ProgressDialog.show(this, "Login Account", "Please wait, while we are checking the credentials.");
             UsersService
                     .getInstance()
                     .init(dbName)
                     .login(phone, pwd, this, progressDialog, chbRememberMe.isChecked());
         }
    }
}