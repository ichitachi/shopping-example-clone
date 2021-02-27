package com.teikaiz.quiz.ui.holder;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teikaiz.quiz.R;
import com.teikaiz.quiz.model.Products;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView itemName, itemDescription, itemPrice;
    private ImageView itemImage;
    private Products products;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.product_name_item);
        itemDescription = itemView.findViewById(R.id.product_description_item);
        itemPrice = itemView.findViewById(R.id.product_price_item);
        itemImage = itemView.findViewById(R.id.product_image_item);
        itemView.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("pid", products.getPid());
        Navigation.findNavController(v).navigate(R.id.action_home_to_detail, bundle);
    }

    public void init(Products products) {
        this.products = products;
        itemName.setText(products.getTitle());
        itemDescription.setText(products.getDescription());
        itemPrice.setText("$" + products.getPrice());
        Picasso.get().load(products.getImage()).into(itemImage);
    }
}
