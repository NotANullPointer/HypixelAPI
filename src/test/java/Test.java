import com.bhonnso.hypixelapi.HypixelAPI;

public class Test {

    public static void main(String[] args) {
        HypixelAPI api = HypixelAPI.getAPI(":eyes:");
        api.getPlayerByName("Hanako_Kun").whenComplete((hypixelPlayer, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            assert hypixelPlayer != null;
            assert hypixelPlayer.getSkyblockProfiles() != null;
            api.getSkyblockAPI().getProfiles(hypixelPlayer.getSkyblockProfiles()).whenComplete((skyblockProfiles, throwable1) -> {
                if(throwable1 != null) {
                    throwable1.printStackTrace();
                }
                skyblockProfiles.forEach(skyblockProfile -> {
                    skyblockProfile.getUnlockedMinions().forEach(System.out::println);
                    System.out.println("__________________________________");
                });
            });
        });
        api.getGuildByName("Svea").whenComplete((guild, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            guild.getMembers().forEach(member -> {
                System.out.println(member.getUuid() + " " + member.getRank());
            });
        });
    }

}
