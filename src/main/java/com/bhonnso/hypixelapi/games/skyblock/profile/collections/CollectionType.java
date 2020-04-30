package com.bhonnso.hypixelapi.games.skyblock.profile.collections;

import com.bhonnso.hypixelapi.APIUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.bhonnso.hypixelapi.games.skyblock.profile.collections.Category.*;

public enum CollectionType {

    WHEAT(FARMING),
    CARROT_ITEM(FARMING),
    POTATO_ITEM(FARMING),
    PUMPKIN(FARMING),
    MELON(FARMING),
    SEEDS(FARMING),
    MUSHROOM_COLLECTION(FARMING),
    INK_SACK__3(FARMING),
    CACTUS(FARMING),
    SUGAR_CANE(FARMING),
    FEATHER(FARMING),
    LEATHER(FARMING),
    PORK(FARMING),
    RAW_CHICKEN(FARMING),
    MUTTON(FARMING),
    RABBIT(FARMING),
    NETHER_STALK(FARMING),

    COBBLESTONE(MINING),
    COAL(MINING),
    IRON_INGOT(MINING),
    GOLD_INGOT(MINING),
    DIAMOND(MINING),
    INK_SACK__4(MINING),
    EMERALD(MINING),
    REDSTONE(MINING),
    QUARTZ(MINING),
    OBSIDIAN(MINING),
    GLOWSTONE_DUST(MINING),
    GRAVEL(MINING),
    ICE(MINING),
    NETHERRACK(MINING),
    SAND(MINING),
    ENDER_STONE(MINING),

    ROTTEN_FLESH(COMBAT),
    BONE(COMBAT),
    STRING(COMBAT),
    SPIDER_EYE(COMBAT),
    SULPHUR(COMBAT),
    ENDER_PEARL(COMBAT),
    GHAST_TEAR(COMBAT),
    SLIME_BALL(COMBAT),
    BLAZE_ROD(COMBAT),
    MAGMA_CREAM(COMBAT),

    LOG(FORAGING),
    LOG__1(FORAGING),
    LOG__2(FORAGING),
    LOG_2__1(FORAGING),
    LOG_2(FORAGING),
    LOG__3(FORAGING),

    RAW_FISH(FISHING),
    RAW_FISH__1(FISHING),
    RAW_FISH__2(FISHING),
    RAW_FISH__3(FISHING),
    PRISMARINE_SHARD(FISHING),
    PRISMARINE_CRYSTALS(FISHING),
    CLAY_BALL(FISHING),
    WATER_LILY(FISHING),
    INK_SACK(FISHING),
    SPONGE(FISHING);

    private final Category category;
    private String name;
    private final ArrayList<CollectionTier> tiers = new ArrayList<>();

    CollectionType(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public ArrayList<CollectionTier> getTiers() {
        return tiers;
    }

    public String getDisplayName() {
        return name;
    }

    public void load(JSONObject data) {
        this.name = data.getString("name");
        APIUtils.extractJSONToArray(CollectionTier.class, data.getJSONArray("tiers"), tiers);
        tiers.forEach(t -> t.setCollectionType(this));
    }

    @Override
    public String toString() {
        return name().replace("__", ":");
    }

    public static CollectionType getByName(String name) {
        return Arrays.stream(values())
                .filter(cT -> cT.toString().equalsIgnoreCase(name))
                .findAny().orElse(null);
    }

    public static void loadCollections(JSONObject data) {
        Arrays.stream(values())
                .collect(Collectors.groupingBy(CollectionType::getCategory))
                .forEach((category, collections) -> {
                    JSONObject jsonObject = data.getJSONObject(category.name()).getJSONObject("items");
                    collections.forEach(collectionType -> {
                        collectionType.load(jsonObject.getJSONObject(collectionType.toString()));
                    });
                });
    }

}
