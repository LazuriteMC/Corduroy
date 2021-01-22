package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.type.special.TemporaryView;
import dev.lazurite.corduroy.api.view.type.special.TickingView;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.ViewContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
@Environment(EnvType.CLIENT)
public class ClientWorldMixin {
    @Inject(method = "tickEntities", at = @At("HEAD"))
    public void tickViews(CallbackInfo info) {
        View view = ViewStack.getInstance().peek();

        /* Tick the view container */
        if (MinecraftClient.getInstance().gameRenderer.getCamera() instanceof ViewContainer) {
            ((ViewContainer) MinecraftClient.getInstance().gameRenderer.getCamera()).tick();
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
}
