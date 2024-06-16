package net.tobiichi3227.carpet.addition.mixin.rule;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Items.class)
public class ItemsMixin {
    @ModifyArg(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/EnderPearlItem;<init>(Lnet/minecraft/item/Item$Settings;)V"),
            index = 0)
    private static Item.Settings modifyEnderPearl(Item.Settings settings) {
        return new Item.Settings().maxCount(64);
    }

    @ModifyArg(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/EggItem;<init>(Lnet/minecraft/item/Item$Settings;)V"),
            index = 0)
    private static Item.Settings modifyEgg(Item.Settings settings) {
        return new Item.Settings().maxCount(64);
    }

    @ModifyArg(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/HoneyBottleItem;<init>(Lnet/minecraft/item/Item$Settings;)V"),
            index = 0)
    private static Item.Settings modifyHoneyBottle(Item.Settings settings) {
        return new Item.Settings().maxCount(64);
    }

    @ModifyArg(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/SnowballItem;<init>(Lnet/minecraft/item/Item$Settings;)V"),
            index = 0)
    private static Item.Settings modifySnowball(Item.Settings settings) {
        return new Item.Settings().maxCount(64);
    }
}