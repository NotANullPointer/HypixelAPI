package com.bhonnso.hypixelapi;

import com.bhonnso.hypixelapi.games.skyblock.profile.minions.Minion;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.MinionType;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class APIUtils {

    private final UUID apiKey;
    private static final String BASE_URL = "https://api.hypixel.net/";
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    APIUtils(UUID apiKey) {
        this.apiKey = apiKey;
    }

    /**
     *
     * @param methodUrl The method URL
     * @param args The method args (String key1, Object arg1, String key2, Object arg2...)
     * @return A CompletableFuture containing the response
     */
    public CompletableFuture<JSONObject> get(String methodUrl, Object... args) {
        if(args.length % 2 == 1) {
            throw new IllegalArgumentException("Args must be a multiple of two");
        }
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append(methodUrl).append("?key=").append(apiKey);
        for(int i = 0; i<args.length-1; i+=2) {
            if(!(args[i] instanceof String)) {
                throw new IllegalArgumentException("Invalid args");
            }
            url.append("&").append(args[i]).append("=").append(args[i+1]);
        }

        CompletableFuture<JSONObject> responseFuture = new CompletableFuture<>();

        THREAD_POOL.submit(() -> {
            try {
                JSONObject response = HTTP_CLIENT.execute(new HttpGet(url.toString()), r -> {
                    String jsonText = EntityUtils.toString(r.getEntity(), "UTF-8");
                    return new JSONObject(jsonText);
                });

                responseFuture.complete(response);
            } catch (IOException e) {
                responseFuture.completeExceptionally(e);
            }
        });
        return responseFuture;
    }

    public static <T extends com.bhonnso.hypixelapi.JSONObject> void extractJSONToArray(Class<T> clazz, JSONArray array, ArrayList<T> arrayList) {
        try {
            Constructor<T> constructor = clazz.getConstructor(JSONObject.class);
            array.forEach(o -> {
                try {
                    arrayList.add(constructor.newInstance(o));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("API error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isSuccessful(JSONObject request) {
        return request.getBoolean("success");
    }

    public static <T> CompletableFuture<List<T>> combineFutures(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allDoneFuture.thenApply(v ->
                futures.stream().
                        map(CompletableFuture::join).
                        collect(Collectors.toList())
        );
    }

    public static boolean listContainsMinionType(List<Minion> list, MinionType minionType) {
        return list.stream().map(Minion::getMinionType).anyMatch(minionType::equals);
    }

}
