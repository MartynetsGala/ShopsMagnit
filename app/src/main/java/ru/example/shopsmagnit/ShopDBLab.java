package ru.example.shopsmagnit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.example.shopsmagnit.database.ShopBaseHelper;
import ru.example.shopsmagnit.database.ShopCursorWrapper;
import ru.example.shopsmagnit.database.ShopDbSchema.ShopTable;

public class ShopDBLab {
    private Context mContext;
    private SQLiteDatabase mDatabase;

    //вызов создания базы данных
    public ShopDBLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ShopBaseHelper(context).getWritableDatabase();
    }

    //возвращение списка магазинов из БД
    public List<Shop> getShops() {
        List<Shop> shops = new ArrayList<>();

        ShopCursorWrapper cursor = queryShops(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //
                Shop shop = cursor.getShop();
                ShopType mShopType = ShopTypeLab.get().getShopType(shop.getType());
                shop.setShopType(mShopType);
                shops.add(shop);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return shops;
    }

    public Shop getShop(Integer id) {
        ShopCursorWrapper cursor = queryShops(
                ShopTable.Cols.ID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getShop();
        } finally {
            cursor.close();
        }
    }

    private Integer getShopId(Integer id) {
        ShopCursorWrapper cursor = queryShops(
                ShopTable.Cols.ID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getShopId();
        } finally {
            cursor.close();
        }
    }

    //преобразование объектов Shop в ContentValues
    //для записи в БД
    public static ContentValues getContentValues(Shop shop) {
        ContentValues values = new ContentValues();
        values.put(ShopTable.Cols.ID, shop.getId().toString());
        values.put(ShopTable.Cols.NAME, shop.getName());
        values.put(ShopTable.Cols.TYPE_ID, shop.getType());
        values.put(ShopTable.Cols.ADDRESS, shop.getAddress());
        values.put(ShopTable.Cols.OPEN, shop.getOpening());
        values.put(ShopTable.Cols.CLOSE, shop.getClosing());
        values.put(ShopTable.Cols.LAT, shop.getLat());
        values.put(ShopTable.Cols.LNG, shop.getLng());
        return values;
    }

    //добавление записи в БД
    public void addShop(Shop shop) {
        ContentValues values = getContentValues(shop);
        mDatabase.insert(ShopTable.NAME, null, values);
    }

    public void updateShops(List<Shop> shops) {
        int i = 0;
        for (Shop shop : shops) {
            if (shop.getModification().equals("added/changed")) {
                if (getShopId(shop.getId()) == null) {
                    addShop(shop);
                } else {
                    updateShop(shop);
                }
            } else if (shop.getModification().equals("deleted")) {
                deleteShop(shop);
            }
        }
    }

    //обновление записи в БД
    public void updateShop(Shop shop) {
        String id = shop.getId().toString();
        ContentValues values = getContentValues(shop);

        mDatabase.update(ShopTable.NAME, values,
                ShopTable.Cols.ID + " = ?",
                new String[] { id });
    }

    //обновление записи в БД
    public void deleteShop(Shop shop) {
        String id = shop.getId().toString();

        mDatabase.delete(ShopTable.NAME,
                ShopTable.Cols.ID + " = ?",
                new String[] { id });
    }

    //чтение данных из БД
    private ShopCursorWrapper queryShops(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ShopTable.NAME,
                null, //columns c null - выбираются все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ShopCursorWrapper(cursor);
    }
}

