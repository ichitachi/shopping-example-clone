package com.teikaiz.quiz.ui.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.teikaiz.quiz.HomeActivity;
import com.teikaiz.quiz.R;
import com.teikaiz.quiz.firebase.ProductsService;
import com.teikaiz.quiz.model.Products;
import com.teikaiz.quiz.share.Constant;
import com.teikaiz.quiz.share.ToastUtils;

public class ProductDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String pid;
    private ImageView imgView;
    private TextView nameTextView, descriptionTextView, priceTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pid = getArguments().getString(Constant.PID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product_details, container, false);
        nameTextView = root.findViewById(R.id.product_name_detail);
        descriptionTextView = root.findViewById(R.id.product_description_detail);
        priceTextView = root.findViewById(R.id.product_price_detail);
        imgView = root.findViewById(R.id.product_image_detail);
        this.fetch();
        return root;
    }
    private void mapping(Products products) {
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle(products.getTitle());
        nameTextView.setText(products.getTitle());
        descriptionTextView.setText(products.getTitle());
        priceTextView.setText("$" + products.getPrice());
        Picasso.get().load(products.getImage()).into(imgView);
    }
    public void fetch() {
        ProductsService.getInstance()
                .findByPid(pid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            Products products = snapshot.getValue(Products.class);
                            mapping(products);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        ToastUtils.showToast("Not find product by pid: "+ pid, getContext());
                    }
                });
    }
}