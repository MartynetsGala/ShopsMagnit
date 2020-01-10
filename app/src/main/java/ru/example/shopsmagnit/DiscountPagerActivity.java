package ru.example.shopsmagnit;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class DiscountPagerActivity extends AppCompatActivity {
    private static final String EXTRA_SHOP_ID = "ru.example.android.shopsmagnit.shop_id";
    private static final String EXTRA_SHOP_ID_DISCOUNT = "ru.example.android.shopsmagnit.shop_id_discount";

    private ViewPager mViewPager;
    private List<Discount> mDiscounts;
    private FragmentManager fragmentManager;

    public static Intent newIntentDiscount(Context packageContext, Integer shopId) {
        Intent intent = new Intent(packageContext, DiscountPagerActivity.class);
        intent.putExtra(EXTRA_SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Integer shopId = (Integer) getIntent()
                .getSerializableExtra(EXTRA_SHOP_ID);

        Integer shopIdDiscount = 0;
        if (savedInstanceState != null && savedInstanceState.getString(EXTRA_SHOP_ID_DISCOUNT) != null) {
            shopIdDiscount = Integer.valueOf(savedInstanceState.getString(EXTRA_SHOP_ID_DISCOUNT));
        }

        if (mDiscounts == null && !shopId.equals(shopIdDiscount)) {
            new DiscountTask().execute();
        }

        setContentView(R.layout.activity_discount_pager);

        mViewPager = (ViewPager) findViewById(R.id.discount_view_pager);
        fragmentManager = getSupportFragmentManager();
        updateUI();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Integer shopId = (Integer) getIntent()
                .getSerializableExtra(EXTRA_SHOP_ID);
        outState.putString(EXTRA_SHOP_ID_DISCOUNT, shopId.toString());
    }

    private void updateUI() {

        mDiscounts = DiscountLab.get().getDiscounts();

        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

            mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager, 1) {
                @Override
                public Fragment getItem(int position) {
                    Discount discount = mDiscounts.get(position);
                    return DiscountFragment.newInstance(discount.getId());
                }

                @Override
                public int getCount() {
                    return mDiscounts.size();
                }
            });

    }

    private class DiscountTask extends AsyncTask<Void, Void, Void> {

        Integer shopId = (Integer) getIntent()
                .getSerializableExtra(EXTRA_SHOP_ID);

        @Override
        protected Void doInBackground(Void... params) {
            try {
                List<Discount> listDiscounts= new DiscountExtract().getDiscounts(shopId);
                DiscountLab.get().setDiscounts(listDiscounts);
            } catch (Exception ioe) {
                System.out.println(ioe.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            updateUI();
        }
    }


}
