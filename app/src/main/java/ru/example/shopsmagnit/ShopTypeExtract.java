package ru.example.shopsmagnit;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShopTypeExtract {
    public List<ShopType> getShopType() {
        List<ShopType> mShopTypes = new ArrayList<>();
        // Create URL
        URL url = null;
        HttpURLConnection myConnection = null;
        int responseCode = 0;

        try {
            url = new URL("http://mobiapp.tander.ru/magnit-api/shops/types");

            // Create connection
            myConnection = (HttpURLConnection) url.openConnection();

            //добавление заголовка запроса
            myConnection.setRequestProperty("version", "4");

            responseCode = myConnection.getResponseCode();
            //чтение ответов
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = myConnection.getInputStream();
                // Success
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                //чтение JSON
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                jsonReader.beginArray(); // Start processing the JSON Array

                while (jsonReader.hasNext()) {
                    ShopType jsonShopType = readShopType(jsonReader);
                    if (jsonShopType != null) {
                        mShopTypes.add(jsonShopType);
                    }
                }
                jsonReader.endArray();
            } else {
                // Error handling code goes here
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myConnection.disconnect();
        }
        return mShopTypes;
    }

    public ShopType readShopType(JsonReader reader) throws IOException {
        Integer idType = -1;
        String nameType = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("id")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    idType = reader.nextInt();
                }
            } else if (key.equals("name")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    nameType = reader.nextString();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (nameType != null) {
            return new ShopType(idType, nameType);
        } else {
            return null;
        }
    }

}
