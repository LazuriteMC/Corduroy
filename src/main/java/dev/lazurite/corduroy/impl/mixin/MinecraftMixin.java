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
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getBlockX()I"
            )
    )
    public final int tick$getBlockX(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return (int) ViewStack.getInstance().peek().get().getPosition(1.0f).x();
        }

        return player.getBlockX();
    }

    @Redirect(
            method = "tick",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getBlockY()I"
            )
    )
    public final int tick$getBlockY(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return (int) ViewStack.getInstance().peek().get().getPosition(1.0f).y();
        }

        return player.getBlockY();
    }

    @Redirect(
            method = "tick",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getBlockZ()I"
            )
    )
    public final int tick$getBlockZ(LocalPlayer player) {
        if (ViewStack.getInstance().peek().isPresent()) {
            return (int) ViewStack.getInstance().peek().get().getPosition(1.0f).z();
        }

        return player.getBlockZ();
    }

    @ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;animateTick(III)V"))
    public void doRandomBlockDisplayTicks(Args args) {
        ViewStack.getInstance().peek().ifPresent(view -> {
            args.set(0, (int) view.getPosition(1.0f).x());
            args.set(1, (int) view.getPosition(1.0f).y());
            args.set(2, (int) view.getPosition(1.0f).z());
        });
    }

}
