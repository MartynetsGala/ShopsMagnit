package ru.example.shopsmagnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DiscountFragment extends Fragment {
    private static final String TAG = "DiscountFragment";
    private static final String ARG_DISCOUNT_ID = "discount_id";

    private Discount mDiscount;
    private ImageView mDiscountImage;
    private TextView mNameField;
    private TextView mDateField;
    private TextView mTypeNameField;
    private TextView mOldPriceField;
    private TextView mNewPriceField;

    public static DiscountFragment newInstance(Integer discountId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DISCOUNT_ID, discountId);

        DiscountFragment fragment = new DiscountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Integer discountId = (Integer) getArguments().getSerializable(ARG_DISCOUNT_ID);
        mDiscount = DiscountLab.get().getDiscount(discountId);

        if (mDiscount.getBitmap() == null) {
            new ImageTask().execute(mDiscount.getImageName());
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Явное заполнение представления фрагмента
        //С передачей идентификатора ресурса макета - R.layout.fragment_shop
        View v = inflater.inflate(R.layout.fragment_discount, container, false);

        mDiscountImage = (ImageView) v.findViewById(R.id.discount_image);
        Drawable drawable = new BitmapDrawable(getResources(), mDiscount.getBitmap());
        mDiscountImage.setImageDrawable(drawable);

        mNameField = (TextView) v.findViewById(R.id.discount_name);
        mNameField.setText(mDiscount.getName());

        mDateField = (TextView) v.findViewById(R.id.discount_start_end_date);
        mDateField.setText(mDiscount.getStartDate() + " - " + mDiscount.getEndDate());

        mTypeNameField = (TextView) v.findViewById(R.id.discount_type_name);
        mTypeNameField.setText(mDiscount.getDiscountCategory());

        mOldPriceField = (TextView) v.findViewById(R.id.discount_old_price);
        mOldPriceField.setPaintFlags(mOldPriceField.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mOldPriceField.setText(mDiscount.getOldPrice().toString());

        mNewPriceField = (TextView) v.findViewById(R.id.discount_new_price);
        mNewPriceField.setText(mDiscount.getNewPrice().toString());

        return v;
    }

    private class ImageTask extends AsyncTask<String , Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                for (String parameter: params) {
                    byte[] bitmapBytes = new DiscountImageExtract().getUrlBytes(parameter);
                    final Bitmap bitmap = BitmapFactory
                            .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                    mDiscount.setBitmap(bitmap);

                }

            } catch (Exception ioe) {
                System.out.println(ioe.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            if (mDiscount.getBitmap() != null) {
                Drawable drawable = new BitmapDrawable(getResources(), mDiscount.getBitmap());
                mDiscountImage.setImageDrawable(drawable);
            }
        }
    }
}
