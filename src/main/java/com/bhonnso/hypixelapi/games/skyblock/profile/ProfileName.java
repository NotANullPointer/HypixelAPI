package com.bhonnso.hypixelapi.games.skyblock.profile;

public class ProfileName {

    private String name;
    private String uuid;

    public ProfileName(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
