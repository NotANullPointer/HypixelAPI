package com.bhonnso.hypixelapi;

import com.bhonnso.hypixelapi.games.SkyblockAPI;
import com.bhonnso.hypixelapi.guilds.Guild;
import com.bhonnso.hypixelapi.hypixel.HypixelPlayer;
import com.google.common.cache.LoadingCache;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class HypixelAPI {

    private UUID apiKey;
    private SkyblockAPI skyblockAPI;
    private APIUtils apiUtils;

    private LoadingCache<String, Guild> guildIdCache = APIUtils.createCache(this::getGuildByIdInternal, 5);
    private LoadingCache<String, Guild> guildNameCache = APIUtils.createCache(this::getGuildByNameInternal, 5);

    private LoadingCache<String, HypixelPlayer> playerIdCache = APIUtils.createCache(this::getPlayerByIdInternal, 10);
    private LoadingCache<String, HypixelPlayer> playerNameCache = APIUtils.createCache(this::getPlayerByNameInternal, 10);

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
        return APIUtils.completableFuture(() -> guildIdCache.getUnchecked(guildId));
    }

    public CompletableFuture<Guild> getGuildByName(String guildName) {
        return APIUtils.completableFuture(() -> guildNameCache.getUnchecked(guildName));
    }

    public CompletableFuture<HypixelPlayer> getPlayerById(String playerId) {
        return APIUtils.completableFuture(() -> playerIdCache.getUnchecked(playerId));
    }

    public CompletableFuture<HypixelPlayer> getPlayerByName(String playerName) {
        return APIUtils.completableFuture(() -> playerNameCache.getUnchecked(playerName));
    }

    private CompletableFuture<Guild> getGuildByIdInternal(String guildId) {
        return apiUtils.get("guild", "id", guildId).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Guild not found");
            else
                return obj;
        }).thenApply(Guild::new);
    }

    private CompletableFuture<Guild> getGuildByNameInternal(String name) {
        return apiUtils.get("guild", "name", name).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Guild not found");
            else
                return obj;
        }).thenApply(Guild::new);
    }

    private CompletableFuture<HypixelPlayer> getPlayerByIdInternal(String uuid) {
        return apiUtils.get("player", "uuid", uuid).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Player not found");
            else
                return obj;
        }).thenApply(HypixelPlayer::new);
    }

    private CompletableFuture<HypixelPlayer> getPlayerByNameInternal(String name) {
        return apiUtils.get("player", "name", name).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Player not found");
            else
                return obj;
        }).thenApply(HypixelPlayer::new);
    }
}
