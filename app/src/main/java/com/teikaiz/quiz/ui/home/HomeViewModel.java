package com.teikaiz.quiz.ui.home;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.teikaiz.quiz.R;
import com.teikaiz.quiz.firebase.ProductsService;
import com.teikaiz.quiz.model.Products;
import com.teikaiz.quiz.ui.holder.ProductViewHolder;

@RequiresApi(api = Build.VERSION_CODES.M)
public class HomeViewModel extends ViewModel {

    private FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;

    public HomeViewModel() {
        FirebaseRecyclerOptions<Products> options = ProductsService.getInstance()
                .getAllProducts();
       adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.init(model);
            }
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item, parent, false);
                return  new ProductViewHolder(view);
            }
        };
    }
    public FirebaseRecyclerAdapter<Products, ProductViewHolder> getAdapter() {
        return adapter;
    }

}