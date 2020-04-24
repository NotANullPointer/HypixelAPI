package com.bhonnso.hypixelapi.games.skyblock.bazaar;

import com.bhonnso.hypixelapi.APIUtils;
import com.bhonnso.hypixelapi.games.SkyblockAPI;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Bazaar {

    private ArrayList<String> items = new ArrayList<>();
    private final SkyblockAPI skyblockAPI;

    public Bazaar(SkyblockAPI skyblockAPI) {
        this.skyblockAPI = skyblockAPI;
    }

    public CompletableFuture<BazaarProduct> getItem(String name) {
        return skyblockAPI.getAPIUtils().get("skyblock/bazaar/product", "productId", name).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Item not found");
            else
                return obj;
        }).thenApply(BazaarProduct::new);
    }

    public CompletableFuture<ArrayList<String>> loadItemList() {
        return skyblockAPI.getAPIUtils().get("skyblock/bazaar/products").whenComplete((JSONObject obj, Throwable t) -> {
            JSONArray jsonItems = obj.getJSONArray("productIds");
            jsonItems.forEach(o -> items.add((String)o));
        }).thenApply((jsonObject) -> items);
    }

    public CompletableFuture<HashMap<String, BazaarProduct>> getItems() {
        return skyblockAPI.getAPIUtils().get("skyblock/bazaar").thenApply((jsonObject) -> {
            HashMap<String, BazaarProduct> ret = new HashMap<>();
            JSONObject products = jsonObject.getJSONObject("products");
            products.keySet()
                    .stream()
                    .map(products::getJSONObject)
                    .map(BazaarProduct::new)
                    .forEach(product -> ret.put(product.getProductId(), product));
            return ret;
        });
    }

    public ArrayList<String> getItemList() {
        if(items.isEmpty()) {
            loadItemList().join();
        }
        return items;
    }

}
