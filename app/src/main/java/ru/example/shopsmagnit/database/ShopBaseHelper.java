package ru.example.shopsmagnit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.example.shopsmagnit.database.ShopDbSchema.ShopTable;

//*****создание базы данных*****
//создание, проверка версии и upgrade

public class ShopBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "shopBase.db";

    public ShopBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ShopTable.NAME + "(" +
                ShopTable.Cols.ID + "," +
                ShopTable.Cols.NAME + "," +
                ShopTable.Cols.TYPE_ID + "," +
                ShopTable.Cols.ADDRESS + "," +
                ShopTable.Cols.LNG + "," +
                ShopTable.Cols.LAT + "," +
                ShopTable.Cols.OPEN + "," +
                ShopTable.Cols.CLOSE +
                        ")"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
