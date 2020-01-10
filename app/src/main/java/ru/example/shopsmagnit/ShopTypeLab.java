package ru.example.shopsmagnit;

import java.util.ArrayList;
import java.util.List;

public class ShopTypeLab {
    private static ShopTypeLab sShopTypeLab;

    private List<ShopType> mShopTypes;

    public static ShopTypeLab get() {
        if (sShopTypeLab == null) {
            sShopTypeLab = new ShopTypeLab();
        }
        return sShopTypeLab;
    }

    private ShopTypeLab() {
        mShopTypes = new ArrayList<>();
    }

    public ShopType getShopType(Integer id) {
        for (ShopType shopType : mShopTypes) {
            if (shopType.getIdType().equals(id)) {
                return shopType;
            }
        }
        return null;
    }

    public void setShopTypes(List<ShopType> shopTypes) {
        mShopTypes = shopTypes;
    }


}
