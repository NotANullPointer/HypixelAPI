import com.bhonnso.hypixelapi.HypixelAPI;
import com.bhonnso.hypixelapi.games.skyblock.profile.collections.Collection;

import java.util.TreeSet;

public class Test {

    public static void main(String[] args) {
        HypixelAPI api = HypixelAPI.getAPI(args[0]);
        api.getSkyblockAPI().loadCollections().whenComplete((n, t) -> {
            api.getPlayerByName("Hanako_Kun").whenComplete((hypixelPlayerOpt, throwable) -> {
               if(throwable != null) {
                   throwable.printStackTrace();
               }
               hypixelPlayerOpt.ifPresent(hypixelPlayer -> {
                   String id = hypixelPlayer.getUuid();
                   api.getSkyblockAPI().getProfiles(hypixelPlayer.getSkyblockProfiles()).whenComplete((skyblockProfiles, throwable1) -> {
                       if (throwable1 != null) {
                           throwable1.printStackTrace();
                       }
                       skyblockProfiles.get(2).loadMinions().loadCollections();
                       skyblockProfiles.get(2).getUnlockedMinions().forEach(System.out::println);
                       TreeSet<Collection> collections = new TreeSet<>(skyblockProfiles.get(0).getUnlockedCollections());
                       collections.descendingSet().forEach(System.out::println);
                       skyblockProfiles.get(2).getProfileMember(hypixelPlayer.getUuid()).ifPresent(profileMember -> {
                           profileMember.getSkills().forEach(System.out::println);
                       });
                   });
               });
            });
        });
    }

}
