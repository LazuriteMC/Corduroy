package dev.lazurite.corduroy.impl.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.View;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    /**
     * Cancels hotbar rendering.
     * @see View#shouldRenderHud
     */
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    protected void renderHotbar$HEAD(float tickDelta, PoseStack matrices, CallbackInfo ci) {
        ViewStack.getInstance().peek().filter(view -> !view.shouldRenderHud()).ifPresent(view -> ci.cancel());
    }

    /**
     * Cancels crosshair rendering.
     * @see View#shouldRenderHud
     */
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void renderCrosshair$HEAD(PoseStack matrices, CallbackInfo ci) {
        ViewStack.getInstance().peek().filter(view -> !view.shouldRenderHud()).ifPresent(view -> ci.cancel());
    }

}
