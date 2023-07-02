package net.tobiichi3227.carpet.addition.mixin.rule.EvokerUseUndyOfTotem;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.world.World;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EvokerEntity.class)
public abstract class EvokerEntityMixin extends SpellcastingIllagerEntity {
    private boolean isUsedTotem = false;

    protected EvokerEntityMixin(EntityType<? extends SpellcastingIllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (CarpetTobiichi3227AdditionSettings.evokerUseUndyOfTotem && !this.isUsedTotem && this.random.nextFloat() > 0.5) {
            this.setHealth(1.0f);
            this.clearStatusEffects();
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            this.getWorld().sendEntityStatus(this, EntityStatuses.USE_TOTEM_OF_UNDYING);
            isUsedTotem = true;
            return;
        }

        super.onDeath(damageSource);
    }
}
