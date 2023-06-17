package net.tobiichi3227.carpet.addition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import net.minecraft.server.MinecraftServer;
import net.tobiichi3227.carpet.addition.utils.CarpetTobiichi3227Translations;

import java.util.Map;

public class CarpetTobiichi3227AdditionServer implements CarpetExtension {
    private static final CarpetTobiichi3227AdditionServer INSTANCE = new CarpetTobiichi3227AdditionServer();

    public static MinecraftServer minecraftServer;

    @Override
    public String version() {
        return CarpetTobiichi3227AdditionMod.MODID;
    }

    public static CarpetTobiichi3227AdditionServer getInstance() {
        return INSTANCE;
    }

    public static void init() {
        CarpetServer.manageExtension(INSTANCE);
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetTobiichi3227AdditionSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetTobiichi3227Translations.getTranslationFromResourcePath(lang);
    }
}
