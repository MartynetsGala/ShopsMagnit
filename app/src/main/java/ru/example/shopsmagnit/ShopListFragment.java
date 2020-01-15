package ru.example.shopsmagnit;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShopListFragment extends Fragment {

    private RecyclerView mShopRecyclerView;
    private ShopAdapter mAdapter;

    private Long mSaveDate;

    public void setSaveDate(Long saveDate) {
        mSaveDate = saveDate;
    }
    private class DatabaseTask extends AsyncTask<List<Shop>, Void, Void> {
        @Override
        protected Void doInBackground(List<Shop>... params) {
            try {
                ShopDBLab shopDBLab = new ShopDBLab(getActivity());
                //записали все или изменения в БД
                for (List<Shop> shops: params) {
                    shopDBLab.updateShops(shops);
                }
            } catch (Exception ioe) {
                System.out.println(ioe.toString());
            }
            return null;
        }
    }

    private class ShopTask extends AsyncTask<Void, Void, Void> {

        public List<Shop> listShops;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                List<ShopType> listShopTypes= new ShopTypeExtract().getShopType();
                ShopTypeLab.get().setShopTypes(listShopTypes);

                //получили список магазинов все или изменения
                listShops = new ShopExtract().getShops(mSaveDate);
                //записали все или изменения в БД
                if  (mSaveDate == 0) {
                    //сохранять в параллельном потоке, использовать полученный список
                    ShopLab.get().setShops(listShops);
                } else {
                    //сохранять в UI потоке и использовать список из БД
                    ShopDBLab shopDBLab = new ShopDBLab(getActivity());
                    shopDBLab.updateShops(listShops);
                    //считать ВСЕ из БД в новый список
                    List<Shop> allShopInDB = shopDBLab.getShops();
                    ShopLab.get().setShops(allShopInDB);
                }
                ShopLab.get().updateLocation();
            } catch (Exception ioe) {
                System.out.println(ioe.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            updateUI();
            if  (mSaveDate == 0) {
                //сохранять в  бд в AsyncTask
                new DatabaseTask().execute(listShops);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ShopTask().execute();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
        mShopRecyclerView = (RecyclerView) view
                .findViewById(R.id.shop_recycler_view);
        mShopRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void updateUI() {
        ShopLab shopLab = ShopLab.get();
        List<Shop> shops = shopLab.getShops();

        if (mAdapter == null) {
            mAdapter = new ShopAdapter(shops);
            mShopRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setShops(shops);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class ShopHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mTypeNameTextView;
        private TextView mOpenCloseTextView;
        private TextView mAddressTextView;
        private TextView mOpeningTextView;
        private ImageView mShopTypeImageView;

        private Shop mShop;

        public ShopHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_shop, parent, false));
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.shop_name);
            mTypeNameTextView = (TextView) itemView.findViewById(R.id.shop_type_name);
            mOpenCloseTextView = (TextView) itemView.findViewById(R.id.shop_open_close);
            mAddressTextView = (TextView) itemView.findViewById(R.id.shop_address);
            mOpeningTextView = (TextView) itemView.findViewById(R.id.shop_opening);
            mShopTypeImageView = (ImageView) itemView.findViewById(R.id.shop_type);

        }

        @Override
        public void onClick(View view) {
            Integer idShop = mShop.getId();
            Intent intent = ShopPagerActivity.newIntent(getActivity(), idShop);
            //запуск активности из фрагмента
            startActivity(intent);
        }

        public void bind(Shop shop) {
            mShop = shop;
            mNameTextView.setText(mShop.getName());
            if (mShop.getShopType().getNameType().equals("GM") ) {
                mTypeNameTextView.setText(R.string.shop_type_name_gm);
            } else if (mShop.getShopType().getNameType().equals("MA") ) {
                mTypeNameTextView.setText(R.string.shop_type_name_ma);
            } else if (mShop.getShopType().getNameType().equals("MO") ) {
                mTypeNameTextView.setText(R.string.shop_type_name_mo);
            } else if (mShop.getShopType().getNameType().equals("DG") ) {
                mTypeNameTextView.setText(R.string.shop_type_name_dg);
            } else {
                mTypeNameTextView.setText(R.string.shop_type_name_mm);
            }

            if (mShop.getOpening() == null) {
                mOpenCloseTextView.setText("");
                mOpeningTextView.setText("");
            } else {
                mOpenCloseTextView.setText(mShop.getOpening() + " - " + mShop.getClosing());
                if (mShop.Opening()) {
                    mOpeningTextView.setText(R.string.shop_str_opening);
                    mOpeningTextView.setBackgroundResource(R.color.colorWhite);
                } else {
                    mOpeningTextView.setText(R.string.shop_str_closing);
                    mOpeningTextView.setBackgroundResource(R.color.colorRed);
                }
            }
            mAddressTextView.setText(mShop.getAddress());

            //установка картинки
            if (mShop.getType() == 3) {
                mShopTypeImageView.setImageResource(R.drawable.shop_type_mk);
            } else if (mShop.getType() == 4) {
                mShopTypeImageView.setImageResource(R.drawable.shop_type_ma);
            } else if (mShop.getType() == 5) {
                mShopTypeImageView.setImageResource(R.drawable.shop_type_mo);
            } else {
                mShopTypeImageView.setImageResource(R.drawable.shop_type_md);
            }
        }
    }

    private class ShopAdapter extends RecyclerView.Adapter<ShopHolder> {
        private List<Shop> mShops;

        public ShopAdapter(List<Shop> shops) {
            mShops = shops;
        }

        @NonNull
        @Override
        public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ShopHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ShopHolder holder, int position) {
            Shop shop = mShops.get(position);
            holder.bind(shop);
        }

        @Override
        public int getItemCount() {
            return mShops.size();
        }

        public void setShops(List<Shop> shops) {
            mShops = shops;
        }
    }
}
