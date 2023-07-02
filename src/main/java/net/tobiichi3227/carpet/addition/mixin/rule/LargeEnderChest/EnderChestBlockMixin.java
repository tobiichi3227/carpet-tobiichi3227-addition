package net.tobiichi3227.carpet.addition.mixin.rule.LargeEnderChest;

import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.OptionalInt;

import static carpet.CarpetSettings.language;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {
    @Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;"), require = 0)
    private OptionalInt onUse(PlayerEntity playerEntity, NamedScreenHandlerFactory factory) {
        return openHandledScreen(playerEntity);
    }

    private OptionalInt openHandledScreen(PlayerEntity playerEntity) {
        if (CarpetTobiichi3227AdditionSettings.largeEnderChest) {
            if (language.equals("zh_tw")) {
                return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("終界箱")));
            } else {
                return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("Ender Chest")));
            }
        } else {
            if (language.equals("zh_tw")) {
                return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("終界箱")));
            } else {
                return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("Ender Chest")));
            }
        }

    }
}
