package com.bhonnso.hypixelapi.guilds;

import com.bhonnso.hypixelapi.APIUtils;
import org.json.JSONObject;

import java.time.LocalDate;
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

        private String uuid;
        private String rank;
        private HashMap<LocalDate, Integer> expHistory;

        public GuildMember(JSONObject data) {
            super(data);
            this.uuid = data.getString("uuid");
            this.rank = data.getString("rank");
            this.expHistory = new HashMap<>(data.getJSONObject("expHistory")
                    .toMap()
                    .entrySet()
                    .stream()
                    .map(e -> new AbstractMap.SimpleEntry<>(LocalDate.parse(e.getKey()), Integer.valueOf(e.getValue().toString())))
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
        }

        public String getUuid() {
            return uuid;
        }

        public String getRank() {
            return rank;
        }

        public HashMap<LocalDate, Integer> getExpHistory() {
            return expHistory;
        }

    }

}
