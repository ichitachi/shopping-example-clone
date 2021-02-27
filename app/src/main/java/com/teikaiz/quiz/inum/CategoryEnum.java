package com.teikaiz.quiz.inum;

import com.teikaiz.quiz.R;

import java.util.Arrays;
import java.util.List;

public enum CategoryEnum {
    T_SHIRTS(1L, R.drawable.tshirts, "T Shirts"),
    SPORTS(2L, R.drawable.sports, "Sport Shirts"),
    FEMALE_DRESSES(3L, R.drawable.female_dresses, "Female Dresses"),
    COAT(4L, R.drawable.sweather, "Coat"),
    GLASSES(5L, R.drawable.glasses, "Glasses"),
    PURSES_BAGS(6L, R.drawable.purses_bags, "Purses bags"),
    HATS(7L, R.drawable.hats, "Hats"),
    HEADPHONES(8L, R.drawable.headphoness, "Headphones"),
    LAPTOPS(9L, R.drawable.laptops, "Laptops"),
    MOBILES(10L, R.drawable.mobiles, "Mobiles"),
    WATCHES(11L, R.drawable.watches, "Watches"),
    SHOES(12L, R.drawable.shoess, "Shoes");
    Long categoryId;
    Integer imgSrc;
    String title;
    CategoryEnum(Long categoryId, Integer imgSrc, String title){
        this.categoryId = categoryId;
        this.imgSrc = imgSrc;
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Integer getImgSrc() {
        return imgSrc;
    }

    public String getTitle() {
        return title;
    }

    public static List<CategoryEnum> getClothes() {
        return Arrays.asList(T_SHIRTS, SPORTS, FEMALE_DRESSES, COAT);
    }

    public static List<CategoryEnum> getAccessories() {
        return Arrays.asList(GLASSES, PURSES_BAGS, HATS, SHOES);
    }

    public static List<CategoryEnum> getElectronics() {
        return Arrays.asList(HEADPHONES, LAPTOPS, MOBILES, WATCHES);
    }
}
