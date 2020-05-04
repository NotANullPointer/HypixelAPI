package com.bhonnso.hypixelapi;

import com.bhonnso.hypixelapi.games.skyblock.profile.collections.Collection;
import com.bhonnso.hypixelapi.games.skyblock.profile.collections.CollectionTier;
import com.bhonnso.hypixelapi.games.skyblock.profile.collections.CollectionType;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.Minion;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.MinionTier;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.MinionType;
import com.bhonnso.hypixelapi.guilds.Guild;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.java2d.loops.TransformHelper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class APIUtils {

    private final UUID apiKey;
    private static final String BASE_URL = "https://api.hypixel.net/";
    public static final ScheduledExecutorService THREAD_POOL = Executors.newScheduledThreadPool(1000);

    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }


    APIUtils(UUID apiKey) {
        this.apiKey = apiKey;
        JSONObject check = get("key").join();
        if(!isSuccessful(check)) throw new IllegalArgumentException("Invalid API Key");
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
                CloseableHttpResponse response = HTTP_CLIENT.execute(new HttpGet(url.toString()));
                HttpEntity content = response.getEntity();
                JSONObject json = new JSONObject(EntityUtils.toString(content));
                EntityUtils.consume(content);
                response.close();
                responseFuture.complete(json);
            } catch (IOException e) {
                e.printStackTrace();
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
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }

    public static <T> CompletableFuture<List<T>> combineOptionalFutures(List<CompletableFuture<Optional<T>>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allDoneFuture.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));
    }

    public static <T, Y> LoadingCache<T, Y> createCache(Function<T, CompletableFuture<Y>> cacher, int size) {
        return CacheBuilder.newBuilder()
                .maximumSize(size)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<T, Y>() {
                    @Override
                    public Y load(T t) {
                        return cacher.apply(t).join();
                    }
                });
    }


    private static final Pattern MINION_COLLECTION_PATTERN = Pattern.compile("(.+)_([0-1]?[0-9])");

    public static AbstractMap.SimpleEntry<MinionType, MinionTier> toMinionData(String minionString) {

        Matcher matcher = MINION_COLLECTION_PATTERN.matcher(minionString);
        if (matcher.find()) {
            MinionType minionType = MinionType.fromName(matcher.group(1)).orElse(MinionType.COBBLESTONE);
            MinionTier minionTier = MinionTier.fromInt(Integer.parseInt(matcher.group(2))).orElse(MinionTier.I);
            return new AbstractMap.SimpleEntry<>(minionType, minionTier);
        }
        return null;
    }

    public static AbstractMap.SimpleEntry<CollectionType, Integer> toCollectionValue(Map.Entry<String, Integer> valueEntry) {
        CollectionType collectionType = CollectionType.fromName(valueEntry.getKey()).orElse(CollectionType.COBBLESTONE);
        return new AbstractMap.SimpleEntry<>(collectionType, valueEntry.getValue());
    }

    public static AbstractMap.SimpleEntry<CollectionType, CollectionTier> toCollectionData(String collectionString) {
        CollectionType collectionType;
        CollectionTier collectionTier;
        int tier;
        Matcher matcher = MINION_COLLECTION_PATTERN.matcher(collectionString);
        if (matcher.find()) {
            collectionType = CollectionType.fromName(matcher.group(1)).orElse(CollectionType.COBBLESTONE);
            tier = Integer.parseInt(matcher.group(2));
            collectionTier = null;
            if (tier > 0) {
                collectionTier = collectionType.getTiers().stream().filter(t -> t.getTier().toInt() == tier).findAny().orElse(null);

            }
            return new AbstractMap.SimpleEntry<>(collectionType, collectionTier);
        }
        return null;
    }

    public static <T> CompletableFuture<T> completableFuture(Supplier<T> method) {
        return CompletableFuture.supplyAsync(method, THREAD_POOL);
    }

    public static boolean listContainsMinionType(List<Minion> list, MinionType minionType) {
        return list.stream().map(Minion::getMinionType).anyMatch(minionType::equals);
    }

    public  static String toRoman(int number) {
        if(number == 0) return "0";
        int l =  map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }


}
