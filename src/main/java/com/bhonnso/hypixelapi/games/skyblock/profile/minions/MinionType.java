package com.bhonnso.hypixelapi.games.skyblock.profile.minions;

import java.util.Arrays;

import static com.bhonnso.hypixelapi.games.skyblock.profile.minions.MinionType.Category.*;

public enum MinionType {

    COBBLESTONE(MINING),
    OBSIDIAN(MINING),
    GLOWSTONE(MINING),
    GRAVEL(MINING),
    SAND(MINING),
    CLAY(MINING),
    ICE(MINING),
    SNOW(MINING),
    COAL(MINING),
    IRON(MINING),
    GOLD(MINING),
    DIAMOND(MINING),
    LAPIS(MINING),
    REDSTONE(MINING),
    EMERALD(MINING),
    QUARTZ(MINING),
    ENDER_STONE(MINING),
    WHEAT(FARMING),
    MELON(FARMING),
    PUMPKIN(FARMING),
    CARROT(FARMING),
    POTATO(FARMING),
    MUSHROOM(FARMING),
    CACTUS(FARMING),
    COCOA(FARMING),
    SUGAR_CANE(FARMING),
    NETHER_WARTS(FARMING),
    FLOWER(FARMING),
    FISHING(Category.FISHING),
    ZOMBIE(COMBAT),
    REVENANT(COMBAT),
    SKELETON(COMBAT),
    CREEPER(COMBAT),
    SPIDER(COMBAT),
    TARANTULA(COMBAT),
    CAVESPIDER(COMBAT),
    BLAZE(COMBAT),
    MAGMA_CUBE(COMBAT),
    ENDERMAN(COMBAT),
    GHAST(COMBAT),
    SLIME(COMBAT),
    COW(FARMING),
    PIG(FARMING),
    CHICKEN(FARMING),
    SHEEP(FARMING),
    RABBIT(FARMING),
    OAK(FARMING),
    BIRCH(FARMING),
    SPRUCE(FARMING),
    DARK_OAK(FARMING),
    ACACIA(FARMING),
    JUNGLE(FARMING);

    private Category category;

    MinionType(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public static MinionType getByName(String name) {
        return Arrays.stream(values()).filter(minionType -> minionType.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public enum Category {
        MINING,
        FARMING,
        COMBAT,
        FISHING;
    }

}
