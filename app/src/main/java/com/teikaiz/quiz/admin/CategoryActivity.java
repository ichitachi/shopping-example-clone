package com.teikaiz.quiz.admin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.teikaiz.quiz.R;
import com.teikaiz.quiz.inum.CategoryEnum;
import com.teikaiz.quiz.share.CategoryUtils;
import com.teikaiz.quiz.share.Constant;
import com.teikaiz.quiz.share.DimensUtils;

import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        this.drawCategories();
    }

    public void drawCategories() {
        Map<Integer, List<CategoryEnum>> categories = CategoryUtils.getAdminCategories();
        categories.forEach(this::drawLinearLayout);
    }

    private void drawLinearLayout(Integer rId, List<CategoryEnum> categories) {
        LinearLayout linearLayout = findViewById(rId);
        categories.forEach(category -> this.drawImageView(linearLayout, category));
    }

    private void drawImageView(LinearLayout linearLayout, CategoryEnum category) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(category.getImgSrc());
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(DimensUtils.getResourceDimensToInt(this ,R.dimen.img_category_item_width),
                DimensUtils.getResourceDimensToInt(this , R.dimen.img_category_item_height));
        imageView.setLayoutParams(vp);
        linearLayout.addView(imageView);
        this.onImageViewClick(imageView, category);
    }

    private void onImageViewClick(ImageView imageView, final CategoryEnum category) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, AddProductActivity.class);
                intent.putExtra(Constant.CATEGORY, category);
                startActivity(intent);
            }
        });
    }
}