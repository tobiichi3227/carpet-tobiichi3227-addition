package net.tobiichi3227.carpet.addition.mixin.rule.IllusionerSpawnAtRaid;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import net.tobiichi3227.carpet.addition.helpers.rule.RaidMember;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Shadow
    private int wavesSpawned;

    @Shadow
    private float totalHealth;

    @Shadow
    private Optional<BlockPos> preCalculatedRavagerSpawnLocation;

    @Shadow
    @Final
    private int waveCount;

    @Shadow
    @Final
    private ServerWorld world;

    @Shadow
    @Final
    private Random random;

    @Shadow
    protected abstract boolean isSpawningExtraWave();

    @Shadow
    public abstract void setWaveCaptain(int wave, RaiderEntity entity);

    @Shadow
    public abstract void addRaider(int wave, RaiderEntity raider, @Nullable BlockPos pos, boolean existing);

    @Shadow
    public abstract int getMaxWaves(Difficulty difficulty);

    @Shadow
    public abstract void updateBar();

    @Shadow
    protected abstract void markDirty();

    @Unique
    private int getCount(RaidMember member, int wave, boolean extra) {
        return extra ? member.countInWave[this.waveCount] : member.countInWave[wave];
    }

    @Unique
    private int getBonusCount(RaidMember member, Random random, int wave, LocalDifficulty localDifficulty, boolean extra) {
        int i;
        Difficulty difficulty = localDifficulty.getGlobalDifficulty();
        boolean bl = difficulty == Difficulty.EASY;
        boolean bl2 = difficulty == Difficulty.NORMAL;
        switch (member) {
            case ILLUSIONER -> {
                if (!bl && !bl2 && wave >= 7) {
                    i = 1;
                    break;
                }
                return 0;
            }
            case WITCH -> {
                if (!bl && wave > 2 && wave != 4) {
                    i = 1;
                    break;
                }
                return 0;
            }
            case PILLAGER, VINDICATOR -> {
                if (bl) {
                    i = random.nextInt(2);
                    break;
                }
                if (bl2) {
                    i = 1;
                    break;
                }
                i = 2;
            }
            case RAVAGER -> {
                i = !bl && extra ? 1 : 0;
            }
            default -> {
                return 0;
            }
        }
        return i > 0 ? random.nextInt(i + 1) : 0;
    }

    private boolean spawnRavagerRaider(int wave, int ravagerSpawnCnt, BlockPos spawnPos, RaiderEntity raiderEntity) {
        RaiderEntity ravagerEntity = null;
        if (wave == this.getMaxWaves(Difficulty.NORMAL)) {
            // pillager will spawn in normal mode
            ravagerEntity = (RaiderEntity) EntityType.PILLAGER.create(this.world);

        } else if (wave >= this.getMaxWaves(Difficulty.HARD)) {
            if (ravagerSpawnCnt == 0) {
                ravagerEntity = (RaiderEntity) EntityType.EVOKER.create(this.world);
            } else {
                ravagerEntity = (RaiderEntity) EntityType.VINDICATOR.create(this.world);
            }
        } else {
            return false;
        }

        this.addRaider(wave, ravagerEntity, spawnPos, false);
        ravagerEntity.refreshPositionAndAngles(spawnPos, 0.0f, 0.0f);
        ravagerEntity.startRiding(raiderEntity);
        return true;
    }

    private int getSpawnCount(RaidMember member, int wave, LocalDifficulty difficulty, boolean extra) {
        // getCount means base number getBonusCount means random number by chance
        return this.getCount(member, wave, extra) + this.getBonusCount(member, this.random, wave, difficulty, extra);
    }

    private boolean spawnRaiders(int wave, int count, BlockPos pos, RaidMember member, boolean captainSet) {
        int ravagerCnt = 0;

        for (int i = 0; i < count; i++) {
            RaiderEntity raider = member.type.create(this.world);
            if (raider == null) continue;

            if (raider.getType() == EntityType.ILLUSIONER) {
                raider.setGlowing(true);
            }

            if (!captainSet && raider.canLead()) {
                raider.setPatrolLeader(true);
                this.setWaveCaptain(wave, raider);
                captainSet = true;
            }

            this.addRaider(wave, raider, pos, false);

            if (member.type == EntityType.RAVAGER && spawnRavagerRaider(wave, ravagerCnt, pos, raider)) {
                ravagerCnt++;
            }
        }

        return captainSet;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/raid/Raid;spawnNextWave(Lnet/minecraft/util/math/BlockPos;)V"))
    private void spawnNextWave(Raid instance, BlockPos spawnPos) {
        int wave = this.wavesSpawned + 1;
        this.totalHealth = 0.0f;

        boolean isSpawningExtra = this.isSpawningExtraWave();
        LocalDifficulty localDiff = this.world.getLocalDifficulty(spawnPos);
        RaidMember[] members = CarpetTobiichi3227AdditionSettings.illusionerSpawnAtRaid
            ? RaidMember.MEMBER_WITH_ILLUSIONER
            : RaidMember.MEMBER_WITHOUT_ILLUSIONER;

        boolean captainSet = false;
        for (RaidMember member : members) {
            int spawnCount = getSpawnCount(member, wave, localDiff, isSpawningExtra);
            captainSet = spawnRaiders(wave, spawnCount, spawnPos, member, captainSet);
        }

        this.preCalculatedRavagerSpawnLocation = Optional.empty();
        this.wavesSpawned++;
        this.updateBar();
        this.markDirty();
    }

}
