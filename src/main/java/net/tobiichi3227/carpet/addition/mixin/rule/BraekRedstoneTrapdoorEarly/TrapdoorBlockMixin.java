package net.tobiichi3227.carpet.addition.mixin.rule.BraekRedstoneTrapdoorEarly;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin extends HorizontalFacingBlock {

    @Shadow
    @Final
    public static BooleanProperty POWERED;

    @Shadow
    @Final
    public static BooleanProperty OPEN;

    @Shadow
    @Final
    public static BooleanProperty WATERLOGGED;

    @Shadow
    protected abstract void playToggleSound(@Nullable PlayerEntity player, World world, BlockPos pos, boolean open);

    protected TrapdoorBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        }
        boolean bl = world.isReceivingRedstonePower(pos);
        if (bl != state.get(POWERED)) {
            boolean open = (Boolean) state.get(OPEN) != bl;
            if (bl && open && CarpetTobiichi3227AdditionSettings.breakRedstoneTrapdoorEarly) {
                // in this case, first check for the redstone on top
                BlockPos abovePos = pos.up();
                if (world.getBlockState(abovePos).getBlock() instanceof RedstoneWireBlock) {
                    world.setBlockState(abovePos, Blocks.AIR.getDefaultState(), Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS);
                    Block.dropStack(world, abovePos, new ItemStack(Items.REDSTONE));
                    // now check that this didn't change our state
                    if (world.getBlockState(pos) != state) {
                        // our state was changed, so we can't propagate this update
                        return;
                    }
                }
            }

            if (open) {
                state = (BlockState) state.with(OPEN, bl);
                this.playToggleSound(null, world, pos, bl);
            }

            world.setBlockState(pos, (BlockState) state.with(POWERED, bl), Block.NOTIFY_LISTENERS);
            if (state.get(WATERLOGGED).booleanValue()) {
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
        }
    }
}
