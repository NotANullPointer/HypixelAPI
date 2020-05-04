package com.bhonnso.hypixelapi.games.skyblock.profile;

import com.bhonnso.hypixelapi.APIUtils;
import com.bhonnso.hypixelapi.games.skyblock.profile.collections.CollectionType;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.Minion;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.MinionTier;
import com.bhonnso.hypixelapi.games.skyblock.profile.minions.MinionType;
import com.bhonnso.hypixelapi.games.skyblock.profile.skills.Skill;
import com.bhonnso.hypixelapi.games.skyblock.profile.skills.SkillType;
import com.bhonnso.hypixelapi.games.skyblock.profile.slayers.SlayerData;
import com.google.common.base.Preconditions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class SkyblockProfile extends com.bhonnso.hypixelapi.JSONObject {

    private String profileId;
    private List<ProfileMember> profileMembers = new ArrayList<>();
    private List<Minion> minionList = new ArrayList<>();
    private List<com.bhonnso.hypixelapi.games.skyblock.profile.collections.Collection> collectionList = new ArrayList<>();

    public SkyblockProfile(JSONObject data) {
        super(data);
        data = data.getJSONObject("profile");
        this.profileId = data.getString("profile_id");
        JSONObject profilesData = data.getJSONObject("members");
        profilesData.keySet().forEach(k ->
            profileMembers.add(new ProfileMember(profilesData.getJSONObject(k), k, this)));
    }

    public SkyblockProfile loadMinions() {
        minionList = profileMembers.parallelStream()
                .map(ProfileMember::getMinionData)
                .flatMap(Collection::parallelStream)
                .map(APIUtils::toMinionData)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())))
                .entrySet()
                .parallelStream()
                .map(e -> new Minion(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return this;
    }

    public SkyblockProfile loadCollections() {
        Map<CollectionType, Integer> values =
                profileMembers.parallelStream()
                .map(ProfileMember::getCollectionValues)
                .flatMap(m -> m.entrySet().parallelStream())
                .map(APIUtils::toCollectionValue)
                .filter(e -> Objects.nonNull(e.getKey()))
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.summingInt(e -> e))));
        collectionList = profileMembers.parallelStream()
                .map(ProfileMember::getCollectionTiersData)
                .flatMap(Collection::parallelStream)
                .distinct()
                .map(APIUtils::toCollectionData)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())))
                .entrySet()
                .parallelStream()
                .map(e -> new com.bhonnso.hypixelapi.games.skyblock.profile.collections.Collection(
                        e.getKey(), e.getValue(), values.get(e.getKey())
                ))
                .collect(Collectors.toList());
        return this;
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
     * @return An optional containing the profile member with the given id
     */
    public Optional<ProfileMember> getProfileMember(String id) {
        Preconditions.checkNotNull(id);
        return profileMembers.stream().filter(profileMember -> profileMember.id.equalsIgnoreCase(id)).findFirst();
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
     * @return An optional containing the minion with the given type
     */
    public Optional<Minion> getUnlockedMinion(MinionType minionType) {
        return minionList.stream().filter(minion -> minion.getMinionType() == minionType).findFirst();
    }

    /**
     *
     * @return A list of unlocked collections of this profile
     */
    public List<com.bhonnso.hypixelapi.games.skyblock.profile.collections.Collection> getUnlockedCollections() {
        return collectionList;
    }

    /**
     *
     * @param collectionType The type of the collection
     * @return An optional containing the collection with the given type
     */
    public Optional<com.bhonnso.hypixelapi.games.skyblock.profile.collections.Collection> getUnlockedCollection(CollectionType collectionType) {
        return collectionList.stream().filter(collection -> collection.getCollectionType() == collectionType).findFirst();
    }



    public static class ProfileMember extends com.bhonnso.hypixelapi.JSONObject {

        private String id;
        private SlayerData slayerData;
        private JSONArray minionData;
        private JSONArray collectionData;
        private JSONObject collectionValues;
        private List<Skill> skills;
        private SkyblockProfile parentProfile;
        private boolean skillsApi = true;
        private boolean collectionsApi = true;

        public ProfileMember(JSONObject data, String id, SkyblockProfile parentProfile) {
            super(data);
            this.parentProfile = parentProfile;
            this.id = id;
            this.slayerData = data.has("slayer_bosses") ?
                new SlayerData(data.getJSONObject("slayer_bosses")) :
                SlayerData.empty();
            this.minionData = data.has("crafted_generators") ?
                    data.getJSONArray("crafted_generators") :
                    new JSONArray("[]");
            if(data.has("collection")) {
                this.collectionData = data.getJSONArray("unlocked_coll_tiers");
                this.collectionValues = data.getJSONObject("collection");
            } else {
                collectionsApi = false;
                this.collectionData = new JSONArray("[]");
                this.collectionValues = new JSONObject("{}");
            }
            this.skills = new ArrayList<>();
            if(data.has("experience_skill_combat")) {
                Arrays.stream(SkillType.values()).forEach(skillType -> registerSkill(skillType, data));
            } else {
                skillsApi = false;
            }
        }

        public SkyblockProfile getParentProfile() {
            return parentProfile;
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
            this.minionData.forEach(minion -> minionData.add((String)minion));
            return minionData;
        }

        private List<String> getCollectionTiersData() {
            List<String> collectionData = new ArrayList<>();
            this.collectionData.forEach(collection ->
                collectionData.add((String)collection)
            );
            return collectionData;
        }

        private Map<String, Integer> getCollectionValues() {
            Map<String, Integer> collectionValues = new HashMap<>();
            this.collectionValues.keySet().forEach(k ->
                    collectionValues.put(k, this.collectionValues.getInt(k))
            );
            return collectionValues;
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
         * @return An optional containing the skill with the given type
         */
        public Optional<Skill> getSkill(SkillType skillType) {
            Preconditions.checkNotNull(skillType);
            return skills.stream().filter(skill -> skill.getSkillType() == skillType).findFirst();
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
