import com.bhonnso.hypixelapi.HypixelAPI;

public class Test {

    public static void main(String[] args) {
        HypixelAPI api = HypixelAPI.getAPI("d9cc69c4-3e5a-47c1-8482-871366e0bd7d");
        api.getSkyblockAPI().loadCollections().whenComplete((n, t) -> {
            api.getPlayerByName("Hanako_Kun").whenComplete((hypixelPlayer, throwable) -> {
               if(throwable != null) {
                   throwable.printStackTrace();
               }
               String id = hypixelPlayer.getUuid();
               api.getSkyblockAPI().getProfiles(hypixelPlayer.getSkyblockProfiles()).whenComplete((skyblockProfiles, throwable1) -> {
                   if(throwable1 != null) {
                       throwable1.printStackTrace();
                   }
                   skyblockProfiles.get(2).loadMinions().loadCollections();
                   skyblockProfiles.get(2).getUnlockedMinions().forEach(System.out::println);
                   skyblockProfiles.get(2).getUnlockedCollections().forEach(System.out::println);
               });
            });
        });
    }

}
