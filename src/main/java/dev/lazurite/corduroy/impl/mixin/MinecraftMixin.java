package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getBlockX()I"
            )
    )
    public final int tick_getBlockX(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return (int) ViewStack.getInstance().peek().get().getPosition().x();
        }

        return Minecraft.getInstance().getCameraEntity().blockPosition().getX();
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getBlockY()I"
            )
    )
    public final int tick_getBlockY(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return (int) ViewStack.getInstance().peek().get().getPosition().y();
        }

        return Minecraft.getInstance().getCameraEntity().blockPosition().getY();
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getBlockZ()I"
            )
    )
    public final int tick_getBlockZ(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return (int) ViewStack.getInstance().peek().get().getPosition().z();
        }

        return Minecraft.getInstance().getCameraEntity().blockPosition().getZ();
    }

    @ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;animateTick(III)V"))
    public void doRandomBlockDisplayTicks(Args args) {
        ViewStack.getInstance().peek().ifPresent(view -> {
            args.set(0, (int) view.getPosition().x());
            args.set(0, (int) view.getPosition().y());
            args.set(0, (int) view.getPosition().z());
        });
    }
}
