package com.bhonnso.hypixelapi.games.skyblock.profile;

import com.bhonnso.hypixelapi.APIUtils;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.Minion;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.MinionType;
import com.bhonnso.hypixelapi.games.skyblock.profile.slayers.SlayerData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyblockProfile extends com.bhonnso.hypixelapi.JSONObject {

    private String profileId;
    private List<ProfileMember> profileMembers = new ArrayList<>();
    private List<Minion> minionList = new ArrayList<>();

    private static final Pattern MINION_PATTERN = Pattern.compile("(\\w+)_([0-1]?[0-9])");

    public SkyblockProfile(JSONObject data) {
        super(data);
        data = data.getJSONObject("profile");
        this.profileId = data.getString("profile_id");
        JSONObject profilesData = data.getJSONObject("members");
        profilesData.keySet().forEach(k ->
            profileMembers.add(new ProfileMember(profilesData.getJSONObject(k), k)));
        loadMinions();
    }

    private void loadMinions() {
        HashMap<String, List<Integer>> tempMap = new HashMap<>();
        profileMembers.stream()
                .map(ProfileMember::getMinionData)
                .flatMap(Collection::stream)
                .map(minion -> {
                    Matcher matcher = MINION_PATTERN.matcher(minion);
                    if(matcher.find()) {
                        String minionType = matcher.group(1);
                        int tier = Integer.parseInt(matcher.group(2));
                        return new AbstractMap.SimpleEntry<>(minionType, tier);
                    }
                    return null;
                }).forEach(entry -> {
            if(tempMap.containsKey(entry.getKey())) {
                List<Integer> tiers = tempMap.get(entry.getKey());
                tiers.add(entry.getValue());
                tempMap.replace(entry.getKey(), tiers);
            } else {
                tempMap.put(entry.getKey(), new ArrayList<>(Collections.singletonList(entry.getValue())));
            }
        });
        tempMap.forEach((key, value) -> {
            Minion minion = new Minion(MinionType.getByName(key));
            value.forEach(minion::unlockTier);
            minionList.add(minion);
        });
    }

    public List<ProfileMember> getProfileMembers() {
        return profileMembers;
    }

    public ProfileMember getProfileMember(String id) {
        return profileMembers.stream().filter(profileMember -> profileMember.id.equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public List<Minion> getUnlockedMinions() {
        return minionList;
    }

    public static class ProfileMember extends com.bhonnso.hypixelapi.JSONObject {

        private String id;
        private SlayerData slayerData;
        private JSONArray minionData;

        public ProfileMember(JSONObject data, String id) {
            super(data);
            this.id = id;
            this.slayerData = data.has("slayer_bosses") ?
                new SlayerData(data.getJSONObject("slayer_bosses")) :
                SlayerData.empty();
            this.minionData = data.has("crafted_generators") ?
                    data.getJSONArray("crafted_generators") :
                    new JSONArray("[]");
        }

        private List<String> getMinionData() {
            List<String> minionData = new ArrayList<>();
            this.minionData.forEach(minion -> {
                minionData.add((String)minion);
            });
            return minionData;
        }

        public String getId() {
            return id;
        }

        public SlayerData getSlayerData() {
            return slayerData;
        }
    }

}
