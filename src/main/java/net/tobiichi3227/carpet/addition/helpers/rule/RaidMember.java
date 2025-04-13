package net.tobiichi3227.carpet.addition.helpers.rule;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;

import java.util.Arrays;

public enum RaidMember {
    VINDICATOR(EntityType.VINDICATOR, new int[]{0, 0, 2, 0, 1, 4, 2, 5}),
    EVOKER(EntityType.EVOKER, new int[]{0, 0, 0, 0, 0, 1, 1, 2}),
    PILLAGER(EntityType.PILLAGER, new int[]{0, 4, 3, 3, 4, 4, 4, 2}),
    WITCH(EntityType.WITCH, new int[]{0, 0, 0, 0, 3, 0, 0, 1}),
    RAVAGER(EntityType.RAVAGER, new int[]{0, 0, 0, 1, 0, 1, 0, 2}),

    ILLUSIONER(EntityType.ILLUSIONER, new int[]{0, 0, 0, 0, 0, 0, 1, 2});

    public static final RaidMember[] MEMBER_WITHOUT_ILLUSIONER = (Arrays.stream(values()).filter(
        raidMember -> raidMember.type != EntityType.ILLUSIONER
    ).toArray(RaidMember[]::new));
    public static final RaidMember[] MEMBER_WITH_ILLUSIONER = values();
    public final EntityType<? extends RaiderEntity> type;
    public final int[] countInWave;

    private RaidMember(EntityType type, int[] countInWave) {
        this.type = type;
        this.countInWave = countInWave;
    }
}
