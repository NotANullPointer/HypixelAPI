package com.bhonnso.hypixelapi.hypixel;

import com.bhonnso.hypixelapi.games.skyblock.profile.ProfileName;
import com.bhonnso.hypixelapi.games.skyblock.profile.skills.Skill;
import com.bhonnso.hypixelapi.games.skyblock.profile.skills.SkillType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HypixelPlayer extends com.bhonnso.hypixelapi.JSONObject{

    private ArrayList<ProfileName> skyblockProfiles = new ArrayList<>();
    private String name;
    private String uuid;
    private String discordTag = "";
    private JSONObject data;
    private static HashMap<String, String> skillToAchievement = new HashMap<>();

    static {
        skillToAchievement.put("foraging", "gatherer");
        skillToAchievement.put("farming", "harvester");
        skillToAchievement.put("mining", "excavator");
        skillToAchievement.put("combat", "combat");
        skillToAchievement.put("enchanting", "augmentation");
        skillToAchievement.put("fishing", "angler");
        skillToAchievement.put("alchemy", "concoctor");
    }

    public HypixelPlayer(JSONObject data) {
        super(data);
        data = data.getJSONObject("player");
        this.data = data;
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

    public List<Skill> getSkillsApprox() {
        JSONObject achievements = data.getJSONObject("achievements");
        return Arrays.stream(SkillType.values())
                .filter(skillType -> !skillType.isCosmetic())
                .map(skillType -> getSkillApprox(skillType, achievements))
                .collect(Collectors.toList());
    }

    private Skill getSkillApprox(SkillType skillType, JSONObject achievements) {
        String achievementName = "skyblock_" + skillToAchievement.get(skillType.name().toLowerCase());
        if(achievements.has(achievementName)) {
            int level = achievements.getInt(achievementName);
            return new Skill(level, skillType, false);
        }
        return new Skill(0, skillType);
    }

    public String getName() {
        return name;
    }

    public String getDiscordTag() {
        return discordTag;
    }

    public String getUuid() {
        return uuid;
    }

    public ArrayList<ProfileName> getSkyblockProfiles() {
        return skyblockProfiles;
    }
}
