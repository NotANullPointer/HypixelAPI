package com.bhonnso.hypixelapi.games;

import com.bhonnso.hypixelapi.APIUtils;
import com.bhonnso.hypixelapi.games.skyblock.bazaar.Bazaar;
import com.bhonnso.hypixelapi.games.skyblock.profile.ProfileName;
import com.bhonnso.hypixelapi.games.skyblock.profile.SkyblockProfile;
import com.bhonnso.hypixelapi.games.skyblock.profile.collections.CollectionType;
import com.google.common.base.Preconditions;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SkyblockAPI {

    private Bazaar bazaarInstance;
    private APIUtils apiUtils;

    private LoadingCache<ProfileName, Optional<SkyblockProfile>> profileCache = APIUtils.createCache(this::getProfileInternal, 40);

    public SkyblockAPI(APIUtils apiUtils) {
        this.apiUtils = apiUtils;
        this.bazaarInstance = new Bazaar(this);
    }

    public APIUtils getAPIUtils() {
        return apiUtils;
    }

    public CompletableFuture<Optional<SkyblockProfile>> getProfile(ProfileName profile) {
        Preconditions.checkNotNull(profile);
        return APIUtils.completableFuture(() -> profileCache.getUnchecked(profile));
    }

    private CompletableFuture<Optional<SkyblockProfile>> getProfileInternal(ProfileName profile) {
        Preconditions.checkNotNull(profile);
        return apiUtils.get("skyblock/profile", "profile", profile.getUuid()).handle((obj, t) -> {
            if(!APIUtils.isSuccessful(obj))
                return null;
            else
                return new SkyblockProfile(obj);
        }).thenApply(Optional::ofNullable);
    }

    public CompletableFuture<List<SkyblockProfile>> getProfiles(List<ProfileName> profiles) {
        Preconditions.checkNotNull(profiles);
        return APIUtils.combineOptionalFutures(profiles.stream().map(this::getProfile).collect(Collectors.toList()));
    }

    public CompletableFuture<Void> loadCollections() {
        return apiUtils.get("resources/skyblock/collections").thenAccept(obj -> {
            if(!APIUtils.isSuccessful(obj))
                throw new InternalError("Cannot load collections");
            else
                CollectionType.loadCollectionTypesData(obj.getJSONObject("collections"));
        });
    }

    public Bazaar getBazaar() {
        return bazaarInstance;
    }

}
