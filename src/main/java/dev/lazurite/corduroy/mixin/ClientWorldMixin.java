package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.type.special.TemporaryView;
import dev.lazurite.corduroy.api.view.type.special.TickingView;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.ViewContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
@Environment(EnvType.CLIENT)
public abstract class ClientWorldMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow public abstract void doRandomBlockDisplayTicks(int x, int y, int z);

    @Inject(method = "tickEntities", at = @At("HEAD"))
    public void tickViews(CallbackInfo info) {
        View view = ViewStack.getInstance().peek();

        /* Tick the view container */
        if (client.gameRenderer.getCamera() instanceof ViewContainer) {
            ((ViewContainer) client.gameRenderer.getCamera()).tick();
        }

        /* TemporaryView Aging */
        if (view instanceof TemporaryView) {
            TemporaryView temp = (TemporaryView) view;
            temp.setAge(temp.getAge() + 1);

            if (temp.getAge() > temp.getLifeSpan()) {
                temp.finish();
            }
        }

        /* TickingView Ticking */
        if (view instanceof TickingView) {
            ((TickingView) view).tick();
        }
    }


    @Inject(at = @At("HEAD"), method = "doRandomBlockDisplayTicks", cancellable = true)
    public void blockDisplayTicks(int xCenter, int yCenter, int zCenter, CallbackInfo info) {
        Camera cam = client.gameRenderer.getCamera();
        int camX = (int) cam.getPos().getX();
        int camY = (int) cam.getPos().getY();
        int camZ = (int) cam.getPos().getZ();

        if (camX != xCenter && camY != yCenter && camZ != zCenter) {
            doRandomBlockDisplayTicks(camX, camY, camZ);
        }
    }
}
