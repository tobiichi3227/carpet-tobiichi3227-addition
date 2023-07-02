package net.tobiichi3227.carpet.addition.mixin.rule.LargeEnderChest;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleInventory.class)
public interface SimpleInventoryAccessor {
    @Accessor("size")
    @Mutable
    void setSize(int size);

    @Accessor("stacks")
    @Mutable
    void setStacks(DefaultedList<ItemStack> stacks);
}
