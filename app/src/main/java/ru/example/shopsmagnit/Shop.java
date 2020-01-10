package ru.example.shopsmagnit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Shop {
    private Integer mId;
    private Integer mType;
    private String mName;
    private String mCode;
    private String mAddress;
    private Double mLng;
    private Double mLat;
    private String mOpening;
    private String mClosing;
    private Boolean mPlastic;
    private String mModification;
    private ShopType mShopType;
    private double mDistance;

    public Shop(Integer mId, Integer mType, String mName, String mCode, String mAddress
                , Double mLng, Double mLat, String mOpening, String mClosing, Boolean mPlastic
                , String mModification, ShopType mShopType, double mDistance) {
        this.mId = mId;
        this.mType = mType;
        this.mName = mName;
        this.mCode = mCode;
        this.mAddress = mAddress;
        this.mLng = mLng;
        this.mLat = mLat;
        this.mOpening = mOpening;
        this.mClosing = mClosing;
        this.mPlastic = mPlastic;
        this.mModification = mModification;
        this.mShopType = mShopType;
        this.mDistance = mDistance;

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

    public String getAddress() {
        return mAddress;
    }

    public String getOpening() {
        return mOpening;
    }

    public String getClosing() {
        return mClosing;
    }

    public String getModification() {
        return mModification;
    }

    public ShopType getShopType() {
        return mShopType;
    }

    public double getDistance() { return mDistance; }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public Double getLng() {
        return mLng;
    }

    public Double getLat() {
        return mLat;
    }

    public boolean Opening() {
        //открыт-закрыт
        Integer mHoursOpen = 0;
        Integer mMinOpen = 0;
        Integer mHoursClose = 0;
        Integer mMinClose = 0;
        String mShopOpen = mOpening;
        String mShopClose = mClosing;

        if ( mShopOpen != null && !mShopOpen.equals("")) {
            mHoursOpen = Integer.valueOf(mShopOpen.substring(0,2));
            mMinOpen = Integer.valueOf(mShopOpen.substring(3,5));
        }
        if (mShopClose != null && !mShopClose.equals("")) {
            mHoursClose = Integer.valueOf(mShopClose.substring(0,2));
            mMinClose = Integer.valueOf(mShopClose.substring(3,5));
        }

        String pattern = "HH:mm";
        SimpleDateFormat sDF = new SimpleDateFormat(pattern);
        String sysHoursMin = sDF.format(new Date());
        Integer mSysHours = Integer.valueOf(sysHoursMin.substring(0,2));
        Integer mSysMin = Integer.valueOf(sysHoursMin.substring(3,5));

        if (mHoursOpen > 0 ) {
            if (mHoursClose < mHoursOpen) {
                mHoursClose += 24;
            }
            if (mSysHours < mHoursOpen) {
                mSysHours += 24;
            }
            if ((mSysHours > mHoursOpen && mSysHours < mHoursClose) ||
                    (mSysHours == mHoursOpen && mSysMin >= mMinOpen) ||
                    (mSysHours == mHoursClose && mSysMin < mMinClose)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
