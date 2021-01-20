package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        View view = ViewStack.getInstance().peek();

        if (view != null) {
            if (!view.shouldRenderHud()) {
                info.cancel();
            }
        }
    }
}
