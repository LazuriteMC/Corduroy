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
    @Inject(method = "kill", at = @At("HEAD"))
    public void kill_HEAD(CallbackInfo info) {
        final var entity = (LivingEntity) (Object) this;

        /* If you die, go back to the main player view. */
        if (entity instanceof LocalPlayer) {
            ViewStack.getInstance().clear();
        }
    }
}
