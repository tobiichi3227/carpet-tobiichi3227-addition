package net.tobiichi3227.carpet.addition.mixin.rule.VillagerLobotomize;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import net.tobiichi3227.carpet.addition.utils.Lobotomizable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin implements Lobotomizable {
    @Shadow
    public abstract boolean needsRestock();

    @Shadow
    public abstract boolean shouldRestock(ServerWorld world);

    @Shadow
    public abstract void restock();

    @Unique
    private boolean lobotomized = false;

    @Override
    public boolean isLobotomized() {
        return this.lobotomized;
    }

    @Override
    public void setLobotomized(boolean lobotomized) {
        this.lobotomized = lobotomized;
    }

    @Redirect(
        method = "mobTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ai/brain/Brain;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V"
        )
    )
    private void redirectBrainTick(Brain<VillagerEntity> brain, ServerWorld world, LivingEntity entity) {
        if (!CarpetTobiichi3227AdditionSettings.villagerLobotomize || !this.lobotomized) {
            brain.tick(world, (VillagerEntity) entity);
        }
    }

    @Inject(method = "mobTick", at = @At("TAIL"))
    private void onMobTickTail(ServerWorld world, CallbackInfo ci) {
        if (!CarpetTobiichi3227AdditionSettings.villagerLobotomize || !this.lobotomized) {
            return;
        }
        if (this.shouldRestock(world) && this.needsRestock()) {
            this.restock();
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readLobotomized(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("Lobotomized")) {
            this.lobotomized = nbt.getBoolean("Lobotomized");
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeLobotomized(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Lobotomized", this.lobotomized);
    }
}
