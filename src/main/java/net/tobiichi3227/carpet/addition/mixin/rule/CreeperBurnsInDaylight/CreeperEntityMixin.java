package net.tobiichi3227.carpet.addition.mixin.rule.CreeperBurnsInDaylight;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin extends HostileEntity {
    public CreeperEntityMixin(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tickMovement() {
        if (CarpetTobiichi3227AdditionSettings.creeperBurnInDaylight) {
            if (this.isAlive()) {
                if (this.isAffectedByDaylight()) {
                    this.setOnFireFor(8);
                }
            }
        }

        super.tickMovement();
    }
}
