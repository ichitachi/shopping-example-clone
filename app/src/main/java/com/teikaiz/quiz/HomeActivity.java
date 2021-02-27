package com.teikaiz.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.teikaiz.quiz.firebase.StorageService;
import com.teikaiz.quiz.firebase.UsersService;
import com.teikaiz.quiz.model.Users;
import com.teikaiz.quiz.prevalent.Prevalent;
import com.teikaiz.quiz.share.Constant;
import com.teikaiz.quiz.share.ToastUtils;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private Users users;
    private ImageView profileImageView;
    private  Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu()
                .findItem(R.id.nav_logout)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        logout();
                        return true;
                    }
                });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cart, R.id.nav_orders)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Paper.init(this);
        this.setProfile(navigationView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult
                    result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            this.changeAvatar(imageUri);
            profileImageView.setImageURI(imageUri);
        } else {
            ToastUtils.showToast("Error, Try Again", HomeActivity.this);
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void logout() {
        Paper.book().destroy();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void setProfile(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView profileNameTxtView = headerView.findViewById(R.id.user_profile_name);
        profileImageView = headerView.findViewById(R.id.user_profile_image);
        profileNameTxtView.setText(Prevalent.loginUser.getName());
        users = Prevalent.loginUser;
        assert users != null;
        if(users.getAvatar() != null) {
            Picasso.get().load(users.getAvatar()).into(profileImageView);
        }
        this.onClickImageView(profileImageView);
    }

    private void onClickImageView(ImageView profileImageView) {
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCropImage();
            }
        });
    }

    private void changeAvatar(Uri imageUri) {
        ProgressDialog progressDialog = ProgressDialog.show(this, "Change avatar", "Please wait, while we are changing avatar.");
        UploadTask uploadTask = StorageService.getInstance()
                .storage(Constant.AVATAR_FOLDER, imageUri, users.getPhone(), Constant.SUFFIX_IMAGE);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                ToastUtils.showToast( "Error: "+ e.getMessage(), HomeActivity.this);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ToastUtils.showToast("Avatar Changed Successfully!", HomeActivity.this);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return taskSnapshot.getStorage().getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            users.setAvatar(Objects.requireNonNull(task.getResult()).toString());
                            UsersService.getInstance()
                                    .init(Constant.DB_USERS)
                                    .changeField(users, progressDialog);
                        }
                    }
                });
            }
        });
    }

    private void openCropImage() {
        CropImage.activity(imageUri)
                .setAspectRatio(1, 1)
                .start(HomeActivity.this);
    }
}