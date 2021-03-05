package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.impl.ViewContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
@Environment(EnvType.CLIENT)
public class GameRendererMixin {
    @Shadow @Final private Camera camera;
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftClient client, ResourceManager resourceManager, BufferBuilderStorage bufferBuilderStorage, CallbackInfo info) {
        ViewStack.create(client, camera);
    }

    @Redirect(
        method = "renderWorld",
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V",
                ordinal = 2
        )
    )
    public void pitch(MatrixStack stack, Quaternion quaternion) {
        if (!(MinecraftClient.getInstance().gameRenderer.getCamera() instanceof ViewContainer)) {
            stack.multiply(quaternion);
        }
    }

    @Redirect(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V",
                    ordinal = 3
            )
    )
    public void yaw(MatrixStack stack, Quaternion quaternion) {
        if (!(MinecraftClient.getInstance().gameRenderer.getCamera() instanceof ViewContainer)) {
            stack.multiply(quaternion);
        }
    }
}
