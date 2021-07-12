package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.type.SubjectView;
import dev.lazurite.corduroy.impl.ViewContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Environment(EnvType.CLIENT)
public class LivingEntityMixin {
    @Inject(method = "kill", at = @At("HEAD"))
    public void kill(CallbackInfo info) {
        var camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        var entity = (LivingEntity) (Object) this;

        /* If you die, go back to the main player view. */
        if (entity instanceof ClientPlayerEntity) {
            ViewStack.getInstance().clear();

        /* If your camera is targeting this entity and it dies... */
        } else if (camera instanceof ViewContainer container) {
            if (container.getView() instanceof SubjectView view) {
                if (view.getSubject().equals(entity)) {
                    ViewStack.getInstance().pop();
                }
            }
        }
    }
}
