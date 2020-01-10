package ru.example.shopsmagnit;

import android.net.Uri;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscountExtract {
    public List<Discount> getDiscounts(Integer shopId) {
        List<Discount> mDiscounts = new ArrayList<>();
        // Create URL
        URL url = null;
        HttpURLConnection myConnection = null;
        int responseCode = 0;

        try {
            String uri = Uri.parse("http://mobiapp.tander.ru/magnit-api/discounts/with-publisher/")
                    .buildUpon()
                    .appendQueryParameter("shopId",shopId.toString())
                    .appendQueryParameter("publisher","МПМ")
                    .build().toString();

            url = new URL(uri);

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
                    Discount jsonDiscount = readDiscount(jsonReader);
                    if (jsonDiscount != null) {
                        mDiscounts.add(jsonDiscount);
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
        return mDiscounts;
    }

    public Discount readDiscount(JsonReader reader) throws IOException {
        Integer id = -1;
        Integer type = -1;
        String name = null;
        String imageName = null;
        String discountCategory = null;
        String startDate = null;
        String endDate = null;
        double oldPrice = 0.0;
        double newPrice = 0.0;


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
            } else if (key.equals("image")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    imageName = reader.nextString();
                }
            } else if (key.equals("discountCategory")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    discountCategory = reader.nextString();
                }
            } else if (key.equals("startDate")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    startDate = reader.nextString();
                }
            } else if (key.equals("endDate")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    endDate = reader.nextString();
                }
             } else if (key.equals("oldPrice")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    oldPrice = reader.nextDouble();
                }
            } else if (key.equals("price")) {
                if(reader.peek()== JsonToken.NULL){
                    reader.skipValue();
                } else {
                    newPrice = reader.nextDouble();
                }
            }  else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (name != null) {
            return new Discount(id, type, name, imageName, discountCategory, startDate, endDate, oldPrice, newPrice, null);
        } else {
            return null;
        }
    }

}
