package ru.example.shopsmagnit;

import java.util.ArrayList;
import java.util.List;

public class DiscountLab {
    private static DiscountLab sDiscountLab;

    private List<Discount> mDiscounts;

    public static DiscountLab get() {
        if (sDiscountLab == null) {
            sDiscountLab = new DiscountLab();
        }
        return sDiscountLab;
    }

    public Discount getDiscount(Integer id) {
        for (Discount discount : mDiscounts) {
            if (discount.getId().equals(id)) {
                return discount;
            }
        }
        return null;
    }
    private DiscountLab() {
        mDiscounts = new ArrayList<>();
    }

    public List<Discount> getDiscounts() {
        return mDiscounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        mDiscounts = discounts;
    }
}
