package com.bhonnso.hypixelapi.games.skyblock.profile.minions;

import com.bhonnso.hypixelapi.games.skyblock.profile.collections.Category;

import java.util.Arrays;

import static com.bhonnso.hypixelapi.games.skyblock.profile.collections.Category.*;

public enum MinionType {

    COBBLESTONE(MINING, "Cobblestone"),
    OBSIDIAN(MINING, "Obsidian"),
    GLOWSTONE(MINING, "Glowstone"),
    GRAVEL(MINING, "Gravel"),
    SAND(MINING, "Sand"),
    CLAY(MINING, "Clay"),
    ICE(MINING, "Ice"),
    SNOW(MINING, "Snow"),
    COAL(MINING, "Coal"),
    IRON(MINING, "Iron"),
    GOLD(MINING, "Gold"),
    DIAMOND(MINING, "Diamond"),
    LAPIS(MINING, "Lapis"),
    REDSTONE(MINING, "Redstone"),
    EMERALD(MINING, "Emerald"),
    QUARTZ(MINING, "Quartz"),
    ENDER_STONE(MINING, "End Stone"),
    WHEAT(FARMING, "Wheat"),
    MELON(FARMING, "Melon"),
    PUMPKIN(FARMING, "Pumpkin"),
    CARROT(FARMING, "Carrot"),
    POTATO(FARMING, "Potato"),
    MUSHROOM(FARMING, "Mushroom"),
    CACTUS(FARMING, "Cactus"),
    COCOA(FARMING, "Cocoa Beans"),
    SUGAR_CANE(FARMING, "Sugar Cane"),
    NETHER_WARTS(FARMING, "Nether Wart"),
    FLOWER(FARMING, "Flower"),
    FISHING(Category.FISHING, "Fishing"),
    ZOMBIE(COMBAT, "Zombie"),
    REVENANT(COMBAT, "Revenant"),
    SKELETON(COMBAT, "Skeleton"),
    CREEPER(COMBAT, "Creeper"),
    SPIDER(COMBAT, "Spider"),
    TARANTULA(COMBAT, "Tarantula"),
    CAVESPIDER(COMBAT, "Cave Spider"),
    BLAZE(COMBAT, "Blaze"),
    MAGMA_CUBE(COMBAT, "Magma Cube"),
    ENDERMAN(COMBAT, "Enderman"),
    GHAST(COMBAT, "Ghast"),
    SLIME(COMBAT, "Slime"),
    COW(FARMING, "Cow"),
    PIG(FARMING, "Pig"),
    CHICKEN(FARMING, "Chicken"),
    SHEEP(FARMING, "Sheep"),
    RABBIT(FARMING, "Rabbit"),
    OAK(FORAGING, "Oak"),
    BIRCH(FORAGING, "Birch"),
    SPRUCE(FORAGING, "Spruce"),
    DARK_OAK(FORAGING, "Dark Oak"),
    ACACIA(FORAGING, "Acacia"),
    JUNGLE(FORAGING, "Jungle");

    private final Category category;
    private final String name;

    MinionType(Category category, String name) {
        this.category = category;
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public static MinionType getByName(String name) {
        return Arrays.stream(values()).filter(minionType -> minionType.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public String getDisplayName() {
        return String.format("%s Minion", name);
    }

}
