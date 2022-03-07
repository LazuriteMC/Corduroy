package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.type.TemporaryView;
import dev.lazurite.corduroy.api.view.type.TickingView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow public abstract void animateTick(int i, int j, int k);

    @Inject(method = "tickEntities", at = @At("HEAD"))
    public void tickEntities_HEAD(CallbackInfo info) {
        ViewStack.getInstance().peek().ifPresent(view -> {
            /* TemporaryView Aging */
            if (view instanceof TemporaryView temporaryView) {
                temporaryView.setAge(temporaryView.getAge() + 1);

                if (temporaryView.getAge() > temporaryView.getLifeSpan()) {
                    temporaryView.onExit();
                }
            }

            /* TickingView Ticking */
            if (view instanceof TickingView tickingView) {
                tickingView.tick();
            }
        });
    }

//    @Inject(at = @At("HEAD"), method = "animateTick")
//    public void animateTick_HEAD(int xCenter, int yCenter, int zCenter, CallbackInfo info) {
//        final var camera = minecraft.gameRenderer.getMainCamera();
//        int camX = (int) camera.getPosition().x();
//        int camY = (int) camera.getPosition().y();
//        int camZ = (int) camera.getPosition().z();
//
//        if (camX != xCenter && camY != yCenter && camZ != zCenter) {
//            animateTick(camX, camY, camZ);
//        }
//    }
}
