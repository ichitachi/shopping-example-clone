package com.teikaiz.quiz.share;

import com.teikaiz.quiz.R;
import com.teikaiz.quiz.inum.CategoryEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryUtils {

    public static Map<Integer, List<CategoryEnum>> getAdminCategories() {
        Map<Integer, List<CategoryEnum>> categories = new HashMap<>();
        categories.put(R.id.linear_category_layout_1, CategoryEnum.getClothes());
        categories.put(R.id.linear_category_layout_2, CategoryEnum.getAccessories());
        categories.put(R.id.linear_category_layout_3, CategoryEnum.getElectronics());
        return categories;
    }
}
