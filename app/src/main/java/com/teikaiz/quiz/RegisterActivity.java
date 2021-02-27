package com.teikaiz.quiz;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.teikaiz.quiz.firebase.UsersService;
import com.teikaiz.quiz.share.EditTextUtils;
import com.teikaiz.quiz.share.ValidateUtils;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText, pwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEditText = findViewById(R.id.register_name);
        phoneEditText = findViewById(R.id.register_phone_number_input);
        pwdEditText = findViewById(R.id.register_password_input);
        Button btnRegister = findViewById(R.id.register_btn);
        this.onBtnRegisterClick(btnRegister);
    }

    private void onBtnRegisterClick(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String name = EditTextUtils.getText(nameEditText);
        String phone = EditTextUtils.getText(phoneEditText);
        String pwd = EditTextUtils.getText(pwdEditText);
        if(ValidateUtils.validate(name, phone, pwd, this, true)) {
            ProgressDialog progressDialog = ProgressDialog.show(this, "Register Account", "Please wait, while we are checking the credentials.");
            UsersService.getInstance()
                    .register(name, phone, pwd, this, progressDialog);
        }
    }
}