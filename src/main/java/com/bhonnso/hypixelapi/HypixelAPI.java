package com.bhonnso.hypixelapi;

import com.bhonnso.hypixelapi.games.SkyblockAPI;
import com.bhonnso.hypixelapi.guilds.Guild;
import com.bhonnso.hypixelapi.hypixel.HypixelPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class HypixelAPI {

    private UUID apiKey;
    private SkyblockAPI skyblockAPI;
    private APIUtils apiUtils;

    private HypixelAPI(UUID apiKey) {
        this.apiKey = apiKey;
        this.apiUtils = new APIUtils(apiKey);
    }

    public static HypixelAPI getAPI(UUID apiKey) {
        return new HypixelAPI(apiKey);
    }

    public static HypixelAPI getAPI(String apiKey) {
        return getAPI(UUID.fromString(apiKey));
    }

    public SkyblockAPI getSkyblockAPI() {
        if(skyblockAPI == null)
            skyblockAPI = new SkyblockAPI(apiUtils);
        return skyblockAPI;
    }

    public CompletableFuture<Guild> getGuildById(String guildId) {
        return apiUtils.get("guild", "id", guildId).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Guild not found");
            else
                return obj;
        }).thenApply(Guild::new);
    }

    public CompletableFuture<Guild> getGuildByName(String name) {
        return apiUtils.get("guild", "name", name).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Guild not found");
            else
                return obj;
        }).thenApply(Guild::new);
    }

    public CompletableFuture<HypixelPlayer> getPlayerById(String uuid) {
        return apiUtils.get("player", "uuid", uuid).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Player not found");
            else
                return obj;
        }).thenApply(HypixelPlayer::new);
    }

    public CompletableFuture<HypixelPlayer> getPlayerByName(String name) {
        return apiUtils.get("player", "name", name).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Player not found");
            else
                return obj;
        }).thenApply(HypixelPlayer::new);
    }
}
