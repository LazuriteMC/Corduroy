package dev.lazurite.corduroy.impl.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Camera mainCamera;

    /**
     * Rotates the screen according to the orientation of the camera.
     * @see View
     */
    @Inject(method = "renderLevel", at = @At("HEAD"))
    public void renderLevel_HEAD(float f, long l, PoseStack poseStack, CallbackInfo info) {
        ViewStack.getInstance().peek().ifPresent(view -> {
            final var q = new Quaternionf(mainCamera.rotation());
            q.mul(Axis.YP.rotationDegrees(180));
            q.set(q.x(), -q.y(), q.z(), -q.w());

            final var newMat = new Matrix4f().set(q);
            newMat.transpose();
            poseStack.last().pose().mul(newMat);
        });
    }
}
