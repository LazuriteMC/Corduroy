package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Redirect(
            method = "setupRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getX()D"
            )
    )
    public final double setupRender_getX(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return ViewStack.getInstance().peek().get().getPosition().x();
        }

        return Minecraft.getInstance().getCameraEntity().getX();
    }

    @Redirect(
            method = "setupRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getY()D"
            )
    )
    public final double setupRender_getY(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return ViewStack.getInstance().peek().get().getPosition().y();
        }

        return Minecraft.getInstance().getCameraEntity().getZ();
    }

    @Redirect(
            method = "setupRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getZ()D"
            )
    )
    public final double setupRender_getZ(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return ViewStack.getInstance().peek().get().getPosition().z();
        }

        return Minecraft.getInstance().getCameraEntity().getZ();
    }
}
