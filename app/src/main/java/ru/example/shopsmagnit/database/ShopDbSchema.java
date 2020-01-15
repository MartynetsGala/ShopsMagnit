package ru.example.shopsmagnit.database;

//определение таблицы и столбцов таблицы
public class ShopDbSchema {
    public static final class ShopTable {
        public static final String NAME = "shops";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String TYPE_ID = "type_id";
            public static final String ADDRESS = "address";
            public static final String LNG = "lng";
            public static final String LAT = "lat";
            public static final String OPEN = "open";
            public static final String CLOSE = "close";
        }
    }
}
