package dev.lazurite.corduroy.impl.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.View;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHandler.class)
class MouseHandlerMixin {

    /**
     * Cancels player rotation via mouse.
     * @see View#shouldPlayerControl
     */
    @Redirect(
            method = "turnPlayer",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
            )
    )
    public void turnPlayer$turn(LocalPlayer player, double cursorDeltaX, double cursorDeltaY) {
        if (ViewStack.getInstance().peek().filter(view -> !view.shouldPlayerControl()).isEmpty()) {
            player.turn(cursorDeltaX, cursorDeltaY);
        }
    }

    /**
     * Cancels mouse clicking.
     * @see View#shouldPlayerControl
     */
    @Redirect(
            method = "onPress",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V"
            )
    )
    public void onPress$set(InputConstants.Key key, boolean pressed) {
        if (ViewStack.getInstance().peek().filter(view -> !view.shouldPlayerControl()).isEmpty()) {
            KeyMapping.set(key, pressed);
        }
    }

    /**
     * Cancels mouse clicking.
     * @see View#shouldPlayerControl
     */
    @Redirect(
            method = "onPress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V"
            )
    )
    public void onPress$click(InputConstants.Key key) {
        if (ViewStack.getInstance().peek().filter(view -> !view.shouldPlayerControl()).isEmpty()) {
            KeyMapping.click(key);
        }
    }

}
