package net.tobiichi3227.carpet.addition.mixin.rule.ShulkerBoxCCEFix;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin extends BlockWithEntity {
    protected ShulkerBoxBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        if (CarpetTobiichi3227AdditionSettings.shulkerBoxCCEFix) {
            return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
        }

        return ScreenHandler.calculateComparatorOutput((Inventory) (Object) world.getBlockEntity(pos));
    }
}
