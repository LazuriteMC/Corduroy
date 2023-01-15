package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    /**
     * Resets the {@link ViewStack} if the local player dies.
     */
    @Inject(method = "kill", at = @At("HEAD"))
    public void kill$HEAD(CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof LocalPlayer) {
            ViewStack.getInstance().clear();
        }
    }

}
