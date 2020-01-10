package ru.example.shopsmagnit;

import android.graphics.Bitmap;

public class Discount {
    private Integer mId;
    private Integer mType;
    private String mName;
    private String mImageName;
    private String mDiscountCategory;
    private String mStartDate;
    private String mEndDate;
    private Double mOldPrice;
    private Double mNewPrice;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    private Bitmap mBitmap;

    public Discount(Integer id, Integer type, String name, String imageName, String discountCategory, String startDate, String endDate, Double oldPrice, Double newPrice, Bitmap bitmap) {
        mId = id;
        mType = type;
        mName = name;
        mImageName = imageName;
        mDiscountCategory = discountCategory;
        mStartDate = startDate;
        mEndDate = endDate;
        mOldPrice = oldPrice;
        mNewPrice = newPrice;
        mBitmap = bitmap;
    }

    public Integer getId() {
        return mId;
    }

    public Integer getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public String getImageName() {
        return mImageName;
    }

    public String getDiscountCategory() {
        return mDiscountCategory;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public Double getOldPrice() {
        return mOldPrice;
    }

    public Double getNewPrice() {
        return mNewPrice;
    }


}
