package ru.example.shopsmagnit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ShopFragment extends Fragment {
    private static final String ARG_SHOP_ID = "shop_id";

    private Shop mShop;
    private TextView mNameField;
    private TextView mTypeNameField;
    private TextView mAddressField;
    private TextView mOpenCloseField;
    private TextView mOpeningField;
    private Button mBtnAction;

    public static ShopFragment newInstance(Integer shopId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SHOP_ID, shopId);

        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Integer shopId = (Integer) getArguments().getSerializable(ARG_SHOP_ID);
        mShop = ShopLab.get().getShops(shopId);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Явное заполнение представления фрагмента
        //С передачей идентификатора ресурса макета - R.layout.fragment_shop
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        mTypeNameField = (TextView) v.findViewById(R.id.shop_type_name);
        String typeName = mShop.getShopType().getNameType();
        if (typeName.equals("DG")) {
            mTypeNameField.setText(R.string.shop_type_name_dg);
        } else if (typeName.equals("MA")) {
            mTypeNameField.setText(R.string.shop_type_name_ma);
        } else if (typeName.equals("MO")) {
            mTypeNameField.setText(R.string.shop_type_name_mo);
        } else {
            mTypeNameField.setText(R.string.shop_type_name_mm);
        }

        mNameField = (TextView) v.findViewById(R.id.shop_name);
        mNameField.setText(mShop.getName());

        mAddressField = (TextView) v.findViewById(R.id.shop_address);
        mAddressField.setText(mShop.getAddress());

        mOpenCloseField = (TextView) v.findViewById(R.id.shop_open_close);
        if (mShop.getOpening() == null) {
            mOpenCloseField.setText("");
        } else {
            mOpenCloseField.setText(mShop.getOpening() + " - " + mShop.getClosing());
        }

        mOpeningField = (TextView) v.findViewById(R.id.shop_opening);
        if (mShop.Opening()) {
            mOpeningField.setText(R.string.shop_str_opening);
        } else {
            mOpeningField.setText(R.string.shop_str_closing);
        }

        mBtnAction = (Button) v.findViewById(R.id.btn_shop_action);
        mBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //показать акции
                Integer idShop = mShop.getId();
                Intent intent = DiscountPagerActivity.newIntentDiscount(getActivity(), idShop);
                startActivity(intent);
            }
        });

        return v;
    }


}
