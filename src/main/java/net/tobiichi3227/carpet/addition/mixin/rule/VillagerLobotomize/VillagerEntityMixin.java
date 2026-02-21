package net.tobiichi3227.carpet.addition.mixin.rule.VillagerLobotomize;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import net.tobiichi3227.carpet.addition.utils.Lobotomizable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin implements Lobotomizable {
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

    @Inject(method = "mobTick", at = @At("HEAD"), cancellable = true)
    private void skipBrainTick(CallbackInfo ci) {
        if (CarpetTobiichi3227AdditionSettings.villagerLobotomize && this.lobotomized) {
            ci.cancel();
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
