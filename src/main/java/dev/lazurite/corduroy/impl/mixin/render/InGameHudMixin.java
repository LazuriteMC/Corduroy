package dev.lazurite.corduroy.impl.mixin.render;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public class InGameHudMixin {
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    protected void renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo info) {
        View view = ViewStack.getInstance().peek();

        if (view != null) {
            if (!view.shouldRenderHud()) {
                info.cancel();
            }
        }
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void renderCrosshair(MatrixStack matrices, CallbackInfo info) {
        View view = ViewStack.getInstance().peek();

        if (view != null) {
            if (!view.shouldRenderHud()) {
                info.cancel();
            }
        }
    }
}
