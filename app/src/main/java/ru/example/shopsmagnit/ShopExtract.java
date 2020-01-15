package ru.example.shopsmagnit;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShopExtract {

    private List<Shop> mShops = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat;

    public ShopExtract() {
        String pattern = "EEE MMM dd yyyy HH:mm:ss 'GMT +0300 (MSK)'";
        simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
    }

    public List<Shop> getShops(Long saveDate) {
        // Create URL
        URL url = null;
        HttpURLConnection myConnection = null;

        try {
            url = new URL("http://mobiapp.tander.ru/magnit-api/shops");

            // Create connection
            myConnection = (HttpURLConnection) url.openConnection();

            //добавление заголовка запроса
            myConnection.setRequestProperty("version", "4");
            Date oldDate = new Date(saveDate);
            Date dateToday = new Date();
            long milliseconds = dateToday.getTime() - oldDate.getTime();
            int deltaDays = (int) (milliseconds / (24 * 60 * 60 * 1000));
            Calendar instance = Calendar.getInstance();

            if (saveDate > 0 && deltaDays < 400) {
                //проверка на дату и цикл по дням
                for (int i = deltaDays; i < deltaDays; i++) {
                    instance.setTime(oldDate);
                    instance.add(Calendar.DAY_OF_MONTH, 1);
                    Date nextDate = instance.getTime();
                    oldDate = nextDate;

                    openingConnect(myConnection, oldDate);

                }
            } else {
                oldDate = new Date(0);
                openingConnect(myConnection, oldDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myConnection.disconnect();
        }
        return mShops;
    }

    public void openingConnect(HttpURLConnection myConnection, Date oldDate) {
        try {
            String ifModifiedSinceString = simpleDateFormat.format(oldDate.getTime());
            myConnection.setRequestProperty("If-Modified-Since", ifModifiedSinceString);


            int responseCode = 0;
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
                    Shop jsonShop = readShop(jsonReader);
                    if (jsonShop != null) {
                        mShops.add(jsonShop);
                    }
                }
                jsonReader.endArray();
            } else {
                // Error
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Shop readShop(JsonReader reader) throws IOException {
        Integer id = -1;
        Integer type = -1;
        String name = null;
        String code = null;
        String address = null;
        double lng = 0.0;
        double lat = 0.0;
        String opening = null;
        String closing = null;
        Boolean plastic = false;
        String modification = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("id")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    id = reader.nextInt();
                }
            } else if (key.equals("type")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    type = reader.nextInt();
                }
            } else if (key.equals("name")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    name = reader.nextString();
                }
            } else if (key.equals("code")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    code = reader.nextString();
                }
            } else if (key.equals("address")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    address = reader.nextString();
                }
            } else if (key.equals("lng")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    lng = reader.nextDouble();
                }
            } else if (key.equals("lat")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    lat = reader.nextDouble();
                }
            } else if (key.equals("opening")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    opening = reader.nextString();
                }
            } else if (key.equals("closing")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    closing = reader.nextString();
                }
            } else if (key.equals("modification")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    modification = reader.nextString();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (name != null) {
            ShopType mShopType = ShopTypeLab.get().getShopType(type);
            double distance = 0.0;
            return new Shop(id, type, name, code, address, lng, lat, opening, closing, plastic, modification, mShopType, distance);
        } else {
            return null;
        }
    }

}
