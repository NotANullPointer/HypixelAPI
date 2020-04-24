package com.bhonnso.hypixelapi.guilds;

import com.bhonnso.hypixelapi.APIUtils;
import com.bhonnso.hypixelapi.games.skyblock.bazaar.BazaarProduct;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Guild extends com.bhonnso.hypixelapi.JSONObject {

    private String id;
    private String name;
    private String tag;
    private int exp;
    private ArrayList<GuildMember> members = new ArrayList<>();

    public Guild(JSONObject data) {
        super(data);
        data = data.getJSONObject("guild");
        System.out.println(data);
        this.id = data.getString("_id");
        this.name = data.getString("name");
        this.tag = data.getString("tag");
        this.exp = data.getInt("exp");
        APIUtils.extractJSONToArray(GuildMember.class, data.getJSONArray("members"), members);

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public int getExp() {
        return exp;
    }

    public List<GuildMember> getMembers() {
        return members;
    }

    public static class GuildMember extends com.bhonnso.hypixelapi.JSONObject {

        private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

        private String uuid;
        private String rank;
        private HashMap<Date, Integer> expHistory;

        public GuildMember(JSONObject data) {
            super(data);
            this.uuid = data.getString("uuid");
            this.rank = data.getString("rank");
            this.expHistory = new HashMap<>(data.getJSONObject("expHistory").toMap().entrySet().stream().map(e -> {
                try {
                    return new AbstractMap.SimpleEntry<>(DATE_FORMAT.parse(e.getKey()), e.getValue().toString());
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return new AbstractMap.SimpleEntry<Date, String>(null, "");
                }
            }).collect(Collectors.toMap(e -> (Date)e.getKey(), e -> Integer.valueOf(e.getValue()))));
        }

        public String getUuid() {
            return uuid;
        }

        public String getRank() {
            return rank;
        }

        public HashMap<Date, Integer> getExpHistory() {
            return expHistory;
        }

    }

}
