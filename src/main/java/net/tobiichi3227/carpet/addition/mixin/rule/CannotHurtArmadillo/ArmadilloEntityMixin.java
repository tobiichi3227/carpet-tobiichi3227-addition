package net.tobiichi3227.carpet.addition.mixin.rule.CannotHurtArmadillo;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmadilloEntity.class)
public class ArmadilloEntityMixin extends AnimalEntity {
    protected ArmadilloEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return source.isOf(DamageTypes.OUT_OF_WORLD);
    }
}
