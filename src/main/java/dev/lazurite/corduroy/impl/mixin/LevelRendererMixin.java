package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Redirect(
            method = "setupRender",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getX()D"
            )
    )
    public double setupRender$getX(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return ViewStack.getInstance().peek().get().getPosition(1.0f).x();
        }

        return player.getX();
    }

    @Redirect(
            method = "setupRender",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getY()D"
            )
    )
    public double setupRender$getY(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return ViewStack.getInstance().peek().get().getPosition(1.0f).y();
        }

        return player.getY();
    }

    @Redirect(
            method = "setupRender",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getZ()D"
            )
    )
    public double setupRender$getZ(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return ViewStack.getInstance().peek().get().getPosition(1.0f).z();
        }

        return player.getZ();
    }

}
