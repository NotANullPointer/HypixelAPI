package com.bhonnso.hypixelapi;

import com.bhonnso.hypixelapi.games.SkyblockAPI;
import com.bhonnso.hypixelapi.guilds.Guild;
import com.bhonnso.hypixelapi.hypixel.HypixelPlayer;
import com.google.common.base.Preconditions;
import com.google.common.cache.LoadingCache;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class HypixelAPI {

    private UUID apiKey;
    private SkyblockAPI skyblockAPI;
    private APIUtils apiUtils;

    private LoadingCache<String, Optional<Guild>> guildIdCache = APIUtils.createCache(this::getGuildByIdInternal, 5);
    private LoadingCache<String, Optional<Guild>> guildNameCache = APIUtils.createCache(this::getGuildByNameInternal, 5);

    private LoadingCache<String, Optional<HypixelPlayer>> playerIdCache = APIUtils.createCache(this::getPlayerByIdInternal, 10);
    private LoadingCache<String, Optional<HypixelPlayer>> playerNameCache = APIUtils.createCache(this::getPlayerByNameInternal, 10);

    private HypixelAPI(UUID apiKey) {
        this.apiKey = apiKey;
        this.apiUtils = new APIUtils(apiKey);
        this.skyblockAPI = new SkyblockAPI(apiUtils);
    }

    public static HypixelAPI getAPI(UUID apiKey) {
        Preconditions.checkNotNull(apiKey);
        return new HypixelAPI(apiKey);
    }

    public static HypixelAPI getAPI(String apiKey) {
        Preconditions.checkNotNull(apiKey);
        return getAPI(UUID.fromString(apiKey));
    }

    public SkyblockAPI getSkyblockAPI() {
        return skyblockAPI;
    }

    public CompletableFuture<Optional<Guild>> getGuildById(String guildId) {
        return APIUtils.completableFuture(() -> guildIdCache.getUnchecked(guildId));
    }

    public CompletableFuture<Optional<Guild>> getGuildByName(String guildName) {
        return APIUtils.completableFuture(() -> guildNameCache.getUnchecked(guildName));
    }

    public CompletableFuture<Optional<HypixelPlayer>> getPlayerById(String playerId) {
        return APIUtils.completableFuture(() -> playerIdCache.getUnchecked(playerId));
    }

    public CompletableFuture<Optional<HypixelPlayer>> getPlayerByName(String playerName) {
        return APIUtils.completableFuture(() -> playerNameCache.getUnchecked(playerName));
    }

    private CompletableFuture<Optional<Guild>> getGuildByIdInternal(String guildId) {
        return apiUtils.get("guild", "id", guildId).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                return null;
            else
                return new Guild(obj);
        }).thenApply(Optional::ofNullable);
    }

    private CompletableFuture<Optional<Guild>> getGuildByNameInternal(String name) {
        return apiUtils.get("guild", "name", name).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                return null;
            else
                return new Guild(obj);
        }).thenApply(Optional::ofNullable);
    }

    private CompletableFuture<Optional<HypixelPlayer>> getPlayerByIdInternal(String uuid) {
        return apiUtils.get("player", "uuid", uuid).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                return null;
            else
                return new HypixelPlayer(obj);
        }).thenApply(Optional::ofNullable);
    }

    private CompletableFuture<Optional<HypixelPlayer>> getPlayerByNameInternal(String name) {
        return apiUtils.get("player", "name", name).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                return null;
            else
                return new HypixelPlayer(obj);
        }).thenApply(Optional::ofNullable);
    }
}
