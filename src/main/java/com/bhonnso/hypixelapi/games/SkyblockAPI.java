package com.bhonnso.hypixelapi.games;

import com.bhonnso.hypixelapi.APIUtils;
import com.bhonnso.hypixelapi.games.skyblock.bazaar.Bazaar;
import com.bhonnso.hypixelapi.games.skyblock.profile.ProfileName;
import com.bhonnso.hypixelapi.games.skyblock.profile.SkyblockProfile;
import com.bhonnso.hypixelapi.games.skyblock.profile.collections.CollectionType;
import com.bhonnso.hypixelapi.hypixel.HypixelPlayer;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SkyblockAPI {

    private Bazaar bazaarInstance;
    private APIUtils apiUtils;

    private LoadingCache<ProfileName, SkyblockProfile> profileCache = APIUtils.createCache(this::getProfileInternal, 40);

    public SkyblockAPI(APIUtils apiUtils) {
        this.apiUtils = apiUtils;
        this.bazaarInstance = new Bazaar(this);
    }

    public APIUtils getAPIUtils() {
        return apiUtils;
    }

    public CompletableFuture<SkyblockProfile> getProfile(ProfileName profile) {
        return APIUtils.completableFuture(() -> profileCache.getUnchecked(profile));
    }

    public CompletableFuture<SkyblockProfile> getProfileInternal(ProfileName profile) {
        return apiUtils.get("skyblock/profile", "profile", profile.getUuid()).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("Player not found");
            else
                return obj;
        }).thenApply(SkyblockProfile::new);
    }

    public CompletableFuture<List<SkyblockProfile>> getProfiles(List<ProfileName> profiles) {
        return APIUtils.combineFutures(profiles.stream().map(this::getProfile).collect(Collectors.toList()));
    }

    public CompletableFuture<Void> loadCollections() {
        return apiUtils.get("resources/skyblock/collections").thenAccept(obj -> {
            if(!APIUtils.isSuccessful(obj))
                throw new IllegalArgumentException("API Error");
            else
                CollectionType.loadCollections(obj.getJSONObject("collections"));
        });
    }

    public Bazaar getBazaar() {
        return bazaarInstance;
    }

}
