package net.tobiichi3227.carpet.addition.mixin.rule.BreakRedstoneTrapdoorEarly;

import net.minecraft.block.*;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin {
    @Shadow
    protected abstract boolean canRunOnTop(BlockView world, BlockPos pos, BlockState floor);

    private boolean connectsTo_M(BlockState state) {
        return this.connectsTo_M(state, null);
    }

    private boolean connectsTo_M(BlockState state, @Nullable Direction dir) {
        if (state.isOf(Blocks.REDSTONE_WIRE)) {
            return true;
        }
        if (state.isOf(Blocks.REPEATER)) {
            Direction direction = state.get(RepeaterBlock.FACING);
            return direction == dir || direction.getOpposite() == dir;
        }
        if (state.isOf(Blocks.OBSERVER)) {
            return dir == state.get(ObserverBlock.FACING);
        }
        return state.emitsRedstonePower() && dir != null;
    }

    @Inject(method = "getRenderConnectionType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Lnet/minecraft/block/enums/WireConnection;", at = @At("HEAD"), cancellable = true)
    private void getRenderConnectionType(BlockView world, BlockPos pos, Direction direction, boolean bl, CallbackInfoReturnable<WireConnection> cir) {
        //1.19.4
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        boolean flag = false;
        if (CarpetTobiichi3227AdditionSettings.breakRedstoneTrapdoorEarly) {
            flag = this.canRunOnTop(world, blockPos, blockState) || blockState.getBlock() instanceof TrapdoorBlock;
        } else {
            flag = this.canRunOnTop(world, blockPos, blockState);
        }

        if (bl && flag && connectsTo_M(world.getBlockState(blockPos.up()))) {
            if (blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
                cir.setReturnValue(WireConnection.UP);
                return;
            }
            cir.setReturnValue(WireConnection.SIDE);
            return;
        }
        if (connectsTo_M(blockState, direction) || !blockState.isSolidBlock(world, blockPos) && connectsTo_M(world.getBlockState(blockPos.down()))) {
            cir.setReturnValue(WireConnection.SIDE);
            return;
        }
        cir.setReturnValue(WireConnection.NONE);
        return;
    }
}
