package ru.example.shopsmagnit;

public class ShopType {
    private Integer mIdType;
    private String mNameType;

    public ShopType(Integer mId,  String mName) {
        this.mIdType = mId;
        this.mNameType = mName;
    }

    public Integer getIdType() {
        return mIdType;
    }

    public String getNameType() {
        return mNameType;
    }
}
