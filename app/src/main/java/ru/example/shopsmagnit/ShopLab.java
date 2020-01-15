package ru.example.shopsmagnit;

import android.content.Context;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ShopLab {
    private static ShopLab sShopLab;

    private double sLat;
    private double sLng;

    private List<Shop> mShops;

    public List<Shop> getShops() {
        return mShops;
    }


    public static ShopLab get() {
        if (sShopLab == null) {
            sShopLab = new ShopLab();
        }
        return sShopLab;
    }

    private ShopLab() {
        mShops = new ArrayList<>();
    }

    public Shop getShop(Integer id) {
        for (Shop shop : mShops) {
            if (shop.getId().equals(id)) {
                return shop;
            }
        }
        return null;
    }



    public void setShops(List<Shop> shops) {
        mShops = shops;
    }

    public void setLat(double sLat) {
        this.sLat = sLat;
    }

    public void setLng(double sLng) {
        this.sLng = sLng;
    }



    public void updateLocation() {
        if (sLat!=0.0 || sLng!=0.0){
            for (Shop shop : mShops) {
                double distance = Math.abs(sLat - shop.getLat()) + Math.abs(sLng - shop.getLng());
                shop.setDistance(distance);
            }
            mShops.sort(distanceComparator);
        }
    }

    // компаратор сортирует список или массив объектов по дистанции
    public static Comparator<Shop> distanceComparator = new Comparator<Shop>() {

        @Override
        public int compare(Shop s1, Shop s2) {
            if (s1.getDistance()>s2.getDistance()) {
                return 1;
            } else if (s1.getDistance()<s2.getDistance()) {
                return -1;
            } else {
                return 0;
            }
        }
    };

}
