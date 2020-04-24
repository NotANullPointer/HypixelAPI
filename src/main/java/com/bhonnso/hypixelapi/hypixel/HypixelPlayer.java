package com.bhonnso.hypixelapi.hypixel;

import com.bhonnso.hypixelapi.games.skyblock.profile.ProfileName;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HypixelPlayer extends com.bhonnso.hypixelapi.JSONObject{

    private ArrayList<ProfileName> skyblockProfiles = new ArrayList<>();
    private String name;
    private String uuid;
    private String discordTag = "";

    public HypixelPlayer(JSONObject data) {
        super(data);
        data = data.getJSONObject("player");
        this.name = data.getString("displayname");
        this.uuid = data.getString("uuid");
        try {
            discordTag = data.getJSONObject("socialMedia").getJSONObject("links").getString("DISCORD");
        } catch (JSONException e) {
            discordTag = "";
        }
        try {
            JSONObject profilesJson = data.getJSONObject("stats").getJSONObject("SkyBlock").getJSONObject("profiles");
            for (String key : profilesJson.keySet()) {
                JSONObject profile = profilesJson.getJSONObject(key);
                skyblockProfiles.add(new ProfileName(profile.getString("cute_name"), key));
            }
        } catch (JSONException ignored) {

        }
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public ArrayList<ProfileName> getSkyblockProfiles() {
        return skyblockProfiles;
    }
}
