package net.tobiichi3227.carpet.addition.mixin.rule.EditableSign;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.tobiichi3227.carpet.addition.CarpetTobiichi3227AdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin {
    @Shadow
    private boolean editable;

    @Inject(method = "onActivate", at = @At("HEAD"))
    private void reopenEditSignScreen(ServerPlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetTobiichi3227AdditionSettings.editableSign && playerEntity.getAbilities().allowModifyWorld && playerEntity.getActiveHand() == Hand.MAIN_HAND && playerEntity.isSneaking()) {
            this.editable = true;
            playerEntity.openEditSignScreen((SignBlockEntity) (Object) this);
        }
    }
}
