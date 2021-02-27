package com.teikaiz.quiz.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.teikaiz.quiz.R;
import com.teikaiz.quiz.firebase.ProductsService;
import com.teikaiz.quiz.firebase.StorageService;
import com.teikaiz.quiz.inum.CategoryEnum;
import com.teikaiz.quiz.model.Products;
import com.teikaiz.quiz.share.Constant;
import com.teikaiz.quiz.share.DateUtils;
import com.teikaiz.quiz.share.EditTextUtils;
import com.teikaiz.quiz.share.ToastUtils;
import com.teikaiz.quiz.share.ValidateUtils;

import java.util.Calendar;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddProductActivity extends AppCompatActivity {
    private ImageView selectImg;
    private EditText nameEditText, descriptionEditText, priceEditText;
    private int galleryPick = 1;
    private Uri imageUri;
    private CategoryEnum category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        category = (CategoryEnum) Objects.requireNonNull(getIntent().getExtras())
                .get(Constant.CATEGORY);
        TextView groupTitle = findViewById(R.id.product_group_name);
        assert category != null;
        groupTitle.setText(category.getTitle());
        ToastUtils.showToast(category.getTitle(), this);

        selectImg = findViewById(R.id.select_product_image);
        nameEditText = findViewById(R.id.product_title);
        descriptionEditText = findViewById(R.id.product_description);
        priceEditText = findViewById(R.id.product_price);
        Button btnAdd = findViewById(R.id.add_btn);
        this.onBtnAddClick(btnAdd);
        this.onSelectImageViewClick(selectImg);
    }

    private void onBtnAddClick(Button btnAdd) {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excAddAction();
            }
        });
    }

    private void onSelectImageViewClick(ImageView selectImg) {
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            selectImg.setImageURI(imageUri);
        }
    }

    private Products getProductData() {
        String title = EditTextUtils.getText(nameEditText);
        String description = EditTextUtils.getText(descriptionEditText);
        Double price = EditTextUtils.getDouble(priceEditText);
        Products product = new Products(title, description, price);
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateUtils.dateToString(DateUtils.DATE_STRING_FORMAT, calendar.getTime());
        String currentTime = DateUtils.dateToString(DateUtils.TIME_STRING_FORMAT, calendar.getTime());
        product.setDate(currentDate);
        product.setTime(currentTime);
        product.setCategory(category.getCategoryId());
        product.setPid(this.getProductRandomKey(product));
       return product;
    }

    private void excAddAction() {
        Products product = getProductData();
        if(ValidateUtils.validate(product, this)) {
            storeProductInfo(product);
        }
    }

    private String getProductRandomKey(Products product) {
        return product.getDate() + product.getTime();
    }

    private void storeProductInfo(Products product) {
        ProgressDialog progressDialog = ProgressDialog.show(this, "Add product", "Please wait, while we are adding the new product.");
         UploadTask uploadTask = StorageService.getInstance()
                .storage(Constant.PRODUCT_FOLDER, imageUri, product.getPid(), Constant.SUFFIX_IMAGE);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                ToastUtils.showToast( "Error: "+ e.getMessage(), AddProductActivity.this);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ToastUtils.showToast("Image uploaded Successfully!", AddProductActivity.this);
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
                            ToastUtils.showToast("Got image Url successfully", AddProductActivity.this);
                            product.setImage(Objects.requireNonNull(task.getResult()).toString());
                            ProductsService.getInstance().add(product, AddProductActivity.this, progressDialog);
                        }
                    }
                });
            }
        });

    }
}