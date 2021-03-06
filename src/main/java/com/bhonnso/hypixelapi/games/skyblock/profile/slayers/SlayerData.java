package com.bhonnso.hypixelapi.games.skyblock.profile.slayers;

import com.google.common.base.Preconditions;
import org.json.JSONObject;

import java.util.Comparator;

public class SlayerData extends com.bhonnso.hypixelapi.JSONObject implements Comparable<SlayerData> {

    private static final SlayerData EMPTY = new SlayerData();
    private static final Comparator<SlayerData> COMPARATOR = Comparator.comparingInt(SlayerData::getTotalXp);
    private SlayerTypeData zombie;
    private SlayerTypeData spider;
    private SlayerTypeData wolf;

    public SlayerData(JSONObject data) {
        super(data);
        this.zombie = new SlayerTypeData(data.getJSONObject("zombie"));
        this.spider = new SlayerTypeData(data.getJSONObject("spider"));
        this.wolf = new SlayerTypeData(data.getJSONObject("wolf"));
    }

    private SlayerData() {
        super(null);
        this.zombie = SlayerTypeData.EMPTY;
        this.spider = SlayerTypeData.EMPTY;
        this.wolf = SlayerTypeData.EMPTY;
    }

    public static SlayerData empty() {
        return EMPTY;
    }

    public SlayerTypeData getZombieSlayerData() {
        return zombie;
    }

    public SlayerTypeData getSpiderSlayerData() {
        return spider;
    }

    public SlayerTypeData getWolfSlayerData() {
        return wolf;
    }

    public int getTotalXp() {
        return zombie.xp + spider.xp + wolf.xp;
    }

    @Override
    public int compareTo(SlayerData slayerData) {
        Preconditions.checkNotNull(slayerData);
        return COMPARATOR.compare(this, slayerData);
    }

    public static class SlayerTypeData extends com.bhonnso.hypixelapi.JSONObject implements Comparable<SlayerTypeData> {

        private static final SlayerTypeData EMPTY = new SlayerTypeData();
        private static final Comparator<SlayerTypeData> COMPARATOR = Comparator.comparingInt(SlayerTypeData::getXp);
        private int xp = 0;
        private int t1 = 0;
        private int t2 = 0;
        private int t3 = 0;
        private int t4 = 0;

        SlayerTypeData(JSONObject data) {
            super(data);
            if(data.has("xp"))
                this.xp = data.getInt("xp");
            if(data.has("boss_kills_tier_0"))
                this.t1 = data.getInt("boss_kills_tier_0");
            if(data.has("boss_kills_tier_1"))
                this.t2 = data.getInt("boss_kills_tier_1");
            if(data.has("boss_kills_tier_2"))
                this.t3 = data.getInt("boss_kills_tier_2");
            if(data.has("boss_kills_tier_3"))
                this.t4 = data.getInt("boss_kills_tier_3");
        }

        private SlayerTypeData() {
            super(null);
        }

        public int getXp() {
            return xp;
        }

        public int getT1Kills() {
            return t1;
        }

        public int getT2Kills() {
            return t2;
        }

        public int getT3Kills() {
            return t3;
        }

        public int getT4Kills() {
            return t4;
        }

        @Override
        public int compareTo(SlayerTypeData slayerTypeData) {
            Preconditions.checkNotNull(slayerTypeData);
            return COMPARATOR.compare(this, slayerTypeData);
        }
    }
}
