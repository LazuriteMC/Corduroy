package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.special.TemporaryView;
import dev.lazurite.corduroy.api.view.special.TickableView;
import dev.lazurite.corduroy.api.view.View;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
@Environment(EnvType.CLIENT)
public class ClientWorldMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "tickEntities", at = @At("HEAD"))
    public void tickViews(CallbackInfo info) {
        View view = ViewStack.getInstance().peek();

        /* Temporary-View Aging */
        if (view instanceof TemporaryView) {
            TemporaryView temp = (TemporaryView) view;
            temp.setAge(temp.getAge() + 1);

            if (temp.getAge() > temp.getLifeSpan()) {
                temp.finish();
            }
        }

        /* Tickable-View Ticking */
        if (view instanceof TickableView) {
            ((TickableView) view).tick();
        }
    }
}
