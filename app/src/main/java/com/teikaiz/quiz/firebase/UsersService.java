package com.teikaiz.quiz.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teikaiz.quiz.HomeActivity;
import com.teikaiz.quiz.LoginActivity;
import com.teikaiz.quiz.MainActivity;
import com.teikaiz.quiz.admin.CategoryActivity;
import com.teikaiz.quiz.model.Products;
import com.teikaiz.quiz.model.Users;
import com.teikaiz.quiz.prevalent.Prevalent;
import com.teikaiz.quiz.share.Constant;
import com.teikaiz.quiz.share.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class UsersService {
    private UsersService() {}
    private String dbName = Constant.DB_USERS;
    private static class SingletonHelper{
        private static final UsersService instance = new UsersService();
    }
    public static UsersService getInstance() {
        return SingletonHelper.instance;
    }
    public UsersService init(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public void register(final String name, final String phone, final String pwd,  final Context context, final ProgressDialog progressDialog) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapsUsers = snapshot.child(Constant.DB_USERS);
                boolean isPhoneExists = snapsUsers.child(phone).exists();
                if(!isPhoneExists) {
                    Users users = new Users(name, phone, pwd);
                    ref.child(Constant.DB_USERS).child(phone).setValue(users)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        ToastUtils.showToast("Congratulations, your account has been created", context);
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                    } else {
                                        ToastUtils.showToast("Network Error: Please try again after some time...", context);
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                } else {
                    ToastUtils.showToast(String.format("This %s already exists!", phone), context);
                    progressDialog.dismiss();
                    ToastUtils.showToast("Please try again using another phone", context);
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void login(final String phone, final String pwd, final Context context, final ProgressDialog progressDialog, final boolean isRememberMe) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapsUsers = snapshot.child(dbName);
                boolean isPhoneExists = snapsUsers.child(phone).exists();
                if(isPhoneExists) {
                    Users users = snapsUsers.child(phone).getValue(Users.class);
                    assert users != null;
                    if(isLogin(users, pwd)) {
                        intRememberMe(phone, pwd, context, isRememberMe);
                        Prevalent.loginUser = users;
                        ToastUtils.showToast("Logged in successfully...", context);
                        progressDialog.dismiss();
                        Intent intent = new Intent(context, Constant.DB_USERS.equals(dbName) ? HomeActivity.class : CategoryActivity.class);
                        context.startActivity(intent);
                    } else {
                        ToastUtils.showToast("Oos, password is wrong !", context);
                        progressDialog.dismiss();
                    }
                } else {
                    ToastUtils.showToast(String.format("This phone %s do not exists!", phone), context);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void intRememberMe(String phone, String pwd, final Context context, boolean isRememberMe) {
        if(isRememberMe) {
            Paper.init(context);
            Paper.book().write(Prevalent.PAPER_PHONE_KEY, phone);
            Paper.book().write(Prevalent.PAPER_PWD_KEY, pwd);
            Paper.book().write(Prevalent.PAPER_DB_NAME_KEY, dbName);
        }
    }
    private boolean isLogin(Users users, String pwd) {
        return pwd.equals(users.getPwd());
    }

    public void changeField(Users users, ProgressDialog progressDialog) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(dbName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snap = snapshot.child(users.getPhone());
                Users old = snap.getValue(Users.class);
                assert old != null;
                old.setAvatar(users.getAvatar());
                ref.child(users.getPhone())
                        .setValue(old);
                Prevalent.loginUser = old;
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
