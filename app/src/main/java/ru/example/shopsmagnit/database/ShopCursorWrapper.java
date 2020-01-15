package ru.example.shopsmagnit.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.example.shopsmagnit.Shop;
import ru.example.shopsmagnit.database.ShopDbSchema.ShopTable;


//дополнение класса Cursor новыми методами

public class ShopCursorWrapper extends CursorWrapper {
    public ShopCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Shop getShop() {
        Integer id = getInt(getColumnIndex(ShopTable.Cols.ID));
        String name = getString(getColumnIndex(ShopTable.Cols.NAME));
        Integer typeId = getInt(getColumnIndex(ShopTable.Cols.TYPE_ID));
        String address = getString(getColumnIndex(ShopTable.Cols.ADDRESS));
        Double lng = getDouble(getColumnIndex(ShopTable.Cols.LNG));
        Double lat = getDouble(getColumnIndex(ShopTable.Cols.LAT));
        String open = getString(getColumnIndex(ShopTable.Cols.OPEN));
        String close = getString(getColumnIndex(ShopTable.Cols.CLOSE));

        Shop shop = new Shop(id);
        shop.setName(name);
        shop.setType(typeId);
        shop.setAddress(address);
        shop.setLat(lat);
        shop.setLng(lng);
        shop.setOpening(open);
        shop.setClosing(close);

        return shop;
    }

    public Integer getShopId() {
        return getInt(getColumnIndex(ShopTable.Cols.ID));
    }
}
