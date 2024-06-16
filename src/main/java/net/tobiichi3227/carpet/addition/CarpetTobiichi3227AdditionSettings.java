package net.tobiichi3227.carpet.addition;


import carpet.api.settings.Rule;

public class CarpetTobiichi3227AdditionSettings {
    public static final String TOBIICHI3227 = "tobiichi3227";
    public static final String TOBIICHI3227_FIX = "tobiichi3227_fix";
    public static final String CARPET_MOD = "carpet_mod";

    @Rule(categories = {TOBIICHI3227})
    public static boolean villagerCanBeLeashed = false;

    @Rule(categories = {TOBIICHI3227})
    public static boolean illusionerSpawnAtRaid = false;

    @Rule(categories = {TOBIICHI3227})
    public static boolean creeperBurnInDaylight = false;

    @Rule(categories = {TOBIICHI3227})
    public static boolean playerSit = false;

    @Rule(categories = {TOBIICHI3227})
    public static boolean breakRedstoneTrapdoorEarly = true;

    @Rule(categories = {TOBIICHI3227})
    public static boolean largeEnderChest = false;

    @Rule(categories = {TOBIICHI3227, TOBIICHI3227_FIX})
    public static boolean shulkerBoxCCEFix = true;

    @Rule(categories = {TOBIICHI3227})
    public static boolean evokerUseUndyOfTotem = false;
}
