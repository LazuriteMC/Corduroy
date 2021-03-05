package dev.lazurite.corduroy.impl.mixin.render;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.impl.ViewContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
@Environment(EnvType.CLIENT)
public class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private Camera camera;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftClient client, ResourceManager resourceManager, BufferBuilderStorage bufferBuilderStorage, CallbackInfo info) {
        ViewStack.create(client, camera);
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    public void renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo info) {
        if (camera instanceof ViewContainer) {
            if (!((ViewContainer) camera).getView().shouldRenderHand()) {
                info.cancel();
            }
        }
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void bobView(MatrixStack stack, float f, CallbackInfo info) {
        if (camera instanceof ViewContainer) {
            if (!((ViewContainer) camera).getView().shouldBobView()) {
                info.cancel();
            }
        }
    }

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void bobViewWhenHurt(MatrixStack stack, float f, CallbackInfo info) {
        if (camera instanceof ViewContainer) {
            if (!((ViewContainer) camera).getView().shouldBobView()) {
                info.cancel();
            }
        }
    }

    @Inject(method = "updateMovementFovMultiplier", at = @At("HEAD"), cancellable = true)
    private void updateMovementFovMultiplier(CallbackInfo info) {
        if (camera instanceof ViewContainer) {
            if (!((ViewContainer) camera).getView().shouldMovementChangeFOV()) {
                info.cancel();
            }
        }
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
        if (!(client.gameRenderer.getCamera() instanceof ViewContainer)) {
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
        if (!(client.gameRenderer.getCamera() instanceof ViewContainer)) {
            stack.multiply(quaternion);
        } else {
            stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        }
    }
}
