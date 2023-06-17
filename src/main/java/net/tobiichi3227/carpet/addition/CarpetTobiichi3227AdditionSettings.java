package net.tobiichi3227.carpet.addition;


import carpet.api.settings.Rule;

public class CarpetTobiichi3227AdditionSettings {
    public static final String TOBIICHI3227 = "TOBIICHI3227";
    public static final String CARPET_MOD = "carpet_mod";

    @Rule(categories = {TOBIICHI3227})
    public static boolean villagerCanBeLeashed = false;

    @Rule(categories = {TOBIICHI3227})
    public static boolean illusionerSpawnAtRaid = false;

    @Rule(categories = {TOBIICHI3227})
    public static boolean creeperBurnInDaylight = false;
}
