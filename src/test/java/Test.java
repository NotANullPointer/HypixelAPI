import com.bhonnso.hypixelapi.HypixelAPI;

import java.time.LocalDate;

public class Test {

    public static void main(String[] args) {
        HypixelAPI api = HypixelAPI.getAPI(":eyes:");
        api.getPlayerByName("Hanako_Kun").whenComplete((hypixelPlayer, throwable) -> {
           if(throwable != null) {
               throwable.printStackTrace();
           }
           String id = hypixelPlayer.getUuid();
           api.getSkyblockAPI().getProfiles(hypixelPlayer.getSkyblockProfiles()).whenComplete((skyblockProfiles, throwable1) -> {
               if(throwable1 != null) {
                   throwable1.printStackTrace();
               }
               skyblockProfiles.forEach(skyblockProfile -> {
                   skyblockProfile.getProfileMember(id).getSkills().forEach(skill ->
                           System.out.println(String.format("%s: LV %d, %d/%d",
                               skill.getSkillType().name(),
                               skill.getSkillLevel().getLevel(),
                               skill.getXp() - skill.getSkillLevel().getCumulativeXp(),
                               skill.getSkillLevel().nextLevel().getXp())));
               });
           });
        });
    }

}
