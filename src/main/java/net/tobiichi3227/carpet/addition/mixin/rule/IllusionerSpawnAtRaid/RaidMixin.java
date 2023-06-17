package net.tobiichi3227.carpet.addition.mixin.rule.IllusionerSpawnAtRaid;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionMod;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import net.tobiichi3227.carpet.addition.helpers.rule.RaidMember;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    private int getCount(RaidMember member, int wave, boolean extra) {
        return extra ? member.countInWave[this.waveCount] : member.countInWave[wave];
    }

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

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/raid/Raid;spawnNextWave(Lnet/minecraft/util/math/BlockPos;)V"))
    private void spawnNextWave(Raid instance, BlockPos pos) {
        CarpetTobiichi3227AdditionMod.LOGGER.info("current wave: %d".formatted(wavesSpawned + 1));
        boolean isCaptainSet = false;
        int i = this.wavesSpawned + 1;
        this.totalHealth = 0.0f;
        LocalDifficulty localDifficulty = this.world.getLocalDifficulty(pos);
        boolean bl2 = this.isSpawningExtraWave();

        for (RaidMember member : (CarpetTobiichi3227AdditionSettings.illusionerSpawnAtRaid ? RaidMember.VALUES_WITH_ILLUSIONER : RaidMember.VALUES)) {
            RaiderEntity raiderEntity;

            //getCount means base number getBonusCount means random number by chance
            int spawnCnt = this.getCount(member, i, bl2) + this.getBonusCount(member, this.random, i, localDifficulty, bl2);
            int ravagerSpawnCnt = 0;
            for (int l = 0; l < spawnCnt && (raiderEntity = member.type.create(this.world)) != null; ++l) {
                if (raiderEntity.getType() == EntityType.ILLUSIONER) {
                    raiderEntity.setGlowing(true);
                }

                if (!isCaptainSet && raiderEntity.canLead()) {
                    //set patrol captain
                    raiderEntity.setPatrolLeader(true);
                    this.setWaveCaptain(i, raiderEntity);
                    isCaptainSet = true;
                }

                // spawn raider
                this.addRaider(i, raiderEntity, pos, false);

                // spawn passenger at ravager
                if (member.type != EntityType.RAVAGER) {
                    continue;
                } else {
                    RaiderEntity ravagerEntity = null;
                    if (i == this.getMaxWaves(Difficulty.NORMAL)) {
                        // pillager will spawn in normal mode
                        ravagerEntity = EntityType.PILLAGER.create(this.world);
                    } else if (i >= this.getMaxWaves(Difficulty.HARD)) {
                        ravagerEntity = ravagerSpawnCnt == 0 ? (RaiderEntity) EntityType.EVOKER.create(this.world) : (RaiderEntity) EntityType.VINDICATOR.create(this.world);
                    }

                    ++ravagerSpawnCnt;
                    if (ravagerEntity == null) {
                        continue;
                    } else {
                        this.addRaider(i, ravagerEntity, pos, false);
                        ravagerEntity.refreshPositionAndAngles(pos, 0.0f, 0.0f);
                        ravagerEntity.startRiding(raiderEntity);
                    }
                }
            }
        }

        this.preCalculatedRavagerSpawnLocation = Optional.empty();
        ++this.wavesSpawned;

        this.updateBar();
        this.markDirty();

    }
}
