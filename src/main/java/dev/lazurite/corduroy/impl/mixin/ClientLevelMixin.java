package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.type.TemporaryView;
import dev.lazurite.corduroy.api.view.type.TickingView;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
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
}
