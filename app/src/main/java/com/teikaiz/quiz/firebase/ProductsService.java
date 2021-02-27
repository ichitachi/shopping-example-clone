package com.teikaiz.quiz.firebase;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teikaiz.quiz.model.Products;
import com.teikaiz.quiz.share.Constant;
import com.teikaiz.quiz.share.ToastUtils;

import java.util.Objects;

public class ProductsService {
    private ProductsService() {}
    private static class SingletonHelper{
        private static final ProductsService instance = new ProductsService();
    }
    public static ProductsService getInstance() {
        return ProductsService.SingletonHelper.instance;
    }

    public void add(Products product, Context context, ProgressDialog progressDialog) {
       final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
               .child(Constant.DB_PRODUCTS);
       databaseReference.child(product.getPid())
               .setValue(product)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       progressDialog.dismiss();
                       if(task.isSuccessful()) {
                           ToastUtils.showToast("Product is added successfully", context);
                       } else {
                           ToastUtils.showToast("Error: " + Objects.requireNonNull(task.getException()).getMessage(), context);
                       }
                   }
               });
    }

    public DatabaseReference findByPid(String pid) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Constant.DB_PRODUCTS);
        return ref.child(pid);
    }

    public FirebaseRecyclerOptions<Products> getAllProducts() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constant.DB_PRODUCTS);
        return new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(databaseReference, Products.class)
                .build();
    }
}
