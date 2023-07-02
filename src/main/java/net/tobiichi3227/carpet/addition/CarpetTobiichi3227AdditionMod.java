package net.tobiichi3227.carpet.addition;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarpetTobiichi3227AdditionMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("carpet-tobiichi3227-addition");

    public static final String MODID = "carpettobiichi3227additionmod";
    public static final String MODNAME = "Carpet Tobiichi3227 Addition";

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        CarpetTobiichi3227AdditionServer.init();
        CarpetTobiichi3227AdditionSettings.setStackableItem();
    }


}