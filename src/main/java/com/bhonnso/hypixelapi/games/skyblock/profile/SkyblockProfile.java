package com.bhonnso.hypixelapi.games.skyblock.profile;

import com.bhonnso.hypixelapi.games.skyblock.profile.minions.Minion;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.MinionType;
import com.bhonnso.hypixelapi.games.skyblock.profile.skills.Skill;
import com.bhonnso.hypixelapi.games.skyblock.profile.skills.SkillType;
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

    /**
     *
     * @return A list of this profile's profile members
     */
    public List<ProfileMember> getProfileMembers() {
        return profileMembers;
    }

    /**
     *
     * @param id The id of the profile member
     * @return The profile member with the given id, null if there's no profile member with the id
     */
    public ProfileMember getProfileMember(String id) {
        return profileMembers.stream().filter(profileMember -> profileMember.id.equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    /**
     *
     * @return A list of unlocked minions of this profile
     */
    public List<Minion> getUnlockedMinions() {
        return minionList;
    }

    /**
     *
     * @param minionType The type of the minion
     * @return The minion with the given type, null if there's no minion with the type
     */
    public Minion getUnlockedMinion(MinionType minionType) {
        return minionList.stream().filter(minion -> minion.getMinionType() == minionType).findFirst().orElse(null);
    }

    public static class ProfileMember extends com.bhonnso.hypixelapi.JSONObject {

        private String id;
        private SlayerData slayerData;
        private JSONArray minionData;
        private List<Skill> skills;
        private boolean skillsApi = true;

        public ProfileMember(JSONObject data, String id) {
            super(data);
            this.id = id;
            this.slayerData = data.has("slayer_bosses") ?
                new SlayerData(data.getJSONObject("slayer_bosses")) :
                SlayerData.empty();
            this.minionData = data.has("crafted_generators") ?
                    data.getJSONArray("crafted_generators") :
                    new JSONArray("[]");
            this.skills = new ArrayList<>();
            if(data.has("experience_skill_combat")) {
                Arrays.stream(SkillType.values()).forEach(skillType -> registerSkill(skillType, data));
            } else {
                skillsApi = false;
            }
        }

        private void registerSkill(SkillType skillType, JSONObject data) {
            String key = String.format("experience_skill_%s", skillType.name().toLowerCase());
            int xp = 0;
            if(data.has(key)) {
                xp = (int) Math.floor(data.getDouble(key));
            }
            skills.add(new Skill(xp, skillType));
        }

        private List<String> getMinionData() {
            List<String> minionData = new ArrayList<>();
            this.minionData.forEach(minion -> {
                minionData.add((String)minion);
            });
            return minionData;
        }

        public boolean isSkillsAPIEnabled() {
            return skillsApi;
        }

        /**
         *
         * @return A list of this profile member's skills
         */
        public List<Skill> getSkills() {
            return skills;
        }

        /**
         *
         * @param skillType The type of the skill
         * @return The skill with the given type, null if there's no skill with the type
         */
        public Skill getSkill(SkillType skillType) {
            return skills.stream().filter(skill -> skill.getSkillType() == skillType).findFirst().orElse(null);
        }

        /**
         *
         * @return The id of this profile member
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @return The slayer data of this profile member
         */
        public SlayerData getSlayerData() {
            return slayerData;
        }
    }

}
