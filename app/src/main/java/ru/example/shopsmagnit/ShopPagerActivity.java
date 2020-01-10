package ru.example.shopsmagnit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class ShopPagerActivity extends AppCompatActivity {
    private static final String EXTRA_SHOP_ID = "ru.example.android.shopsmagnit.shop_id";

    private ViewPager mViewPager;
    private List<Shop> mShops;

    public static Intent newIntent(Context packageContext, Integer shopId) {
        Intent intent = new Intent(packageContext, ShopPagerActivity.class);
        intent.putExtra(EXTRA_SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_pager);

        Integer shopId = (Integer) getIntent()
                .getSerializableExtra(EXTRA_SHOP_ID);

        mViewPager = (ViewPager) findViewById(R.id.shop_view_pager);

        mShops = ShopLab.get().getShops();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager, 1) {
            @Override
            public Fragment getItem(int position) {
                Shop shop = mShops.get(position);
                return ShopFragment.newInstance(shop.getId());
            }

            @Override
            public int getCount() {
                return mShops.size();
            }
        });

        for (int i = 0; i<mShops.size(); i++) {
            if (mShops.get(i).getId().equals(shopId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }


}
