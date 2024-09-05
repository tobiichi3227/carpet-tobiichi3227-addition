package net.tobiichi3227.carpet.addition.mixin.rule.VillagerCanBeLeashed;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Npc;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.village.Merchant;
import net.minecraft.world.World;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends PassiveEntity implements Npc, Merchant {
    protected MerchantEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

//    @Inject(method = "canBeLeashedBy", at = @At("RETURN"), cancellable = true)
//    private void village


    @Override
            public boolean canBeLeashed() {
        return !this.isLeashed() && CarpetTobiichi3227AdditionSettings.villagerCanBeLeashed;
    }
}
