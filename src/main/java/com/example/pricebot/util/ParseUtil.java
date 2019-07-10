package com.example.pricebot.util;

import com.example.pricebot.entity.Product;
import com.example.pricebot.entity.Shop;
import com.example.pricebot.exception.ProductNotFoundException;
import com.example.pricebot.exception.ShopsNotFoundException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParseUtil {
    //TODO: исключения вместо нуллов
    @Nullable
    public static Product parseProduct(String productCode) throws IOException, ProductNotFoundException{

        String productJsonUrl = "https://catalog.api.onliner.by/products/"+ productCode;

        JsonObject productData = jsonParser(productJsonUrl);


        if (productData.has("message")){
            throw new ProductNotFoundException();
        } else return new Product(
                productData.get("full_name").getAsString(),
                productData.get("key").getAsString(),
                productData.get("html_url").getAsString()
        );
    }


    public static Set<Shop> parseShops(String productCode) throws IOException, ShopsNotFoundException{

        String productJsonUrl = "https://shop.api.onliner.by/products/" + productCode + "/positions";

        JsonObject shopsData = jsonParser(productJsonUrl);

        if (shopsData.has("message") || !shopsData.get("shops").isJsonObject()){
            throw new ShopsNotFoundException();
        } else {
            Set<Shop> shops = new HashSet<>();
            for (Map.Entry<String,JsonElement> entry : shopsData.get("shops").getAsJsonObject().entrySet()) {
                JsonObject shopData = entry.getValue().getAsJsonObject();
                shops.add(new Shop(shopData.get("title").getAsString(),shopData.get("id").getAsString()));
            }
            return shops;
        }

    }

    private static JsonObject jsonParser(String url) throws IOException {

        URL jsonUrl = new URL(url);
        URLConnection request = jsonUrl.openConnection();
        request.connect();
        JsonElement root = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));

        return root.getAsJsonObject();

    }




}
