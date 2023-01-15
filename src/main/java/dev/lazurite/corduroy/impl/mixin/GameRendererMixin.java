package dev.lazurite.corduroy.impl.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.View;
import net.minecraft.client.Camera;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow public Camera mainCamera;

    /**
     * Rotates the screen according to the orientation of the camera.
     */
    @Inject(method = "renderLevel", at = @At("HEAD"))
    public void renderLevel$HEAD(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        ViewStack.getInstance().peek().ifPresent(view -> {
            var rotation = new Quaternionf(this.mainCamera.rotation());
            rotation.mul(Axis.YP.rotationDegrees(180));
            rotation.set(rotation.x(), -rotation.y(), rotation.z(), -rotation.w());

            var mat = rotation.get(new Matrix4f());
            mat.transpose();
            poseStack.last().pose().mul(mat);
        });
    }

    /**
     * Cancels player hand rendering.
     * @see View#shouldRenderHand
     */
    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    public void renderHand$HEAD(PoseStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        ViewStack.getInstance().peek().filter(view -> !view.shouldRenderHand()).ifPresent(view -> ci.cancel());
    }

    /**
     * Cancels view bobbing rendering during movement.
     * @see View#shouldBobView
     */
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void bobView$HEAD(PoseStack stack, float f, CallbackInfo ci) {
        ViewStack.getInstance().peek().filter(view -> !view.shouldBobView()).ifPresent(view -> ci.cancel());
    }

    /**
     * Cancels view bobbing rendering during damage.
     * @see View#shouldBobView
     */
    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void bobHurt$HEAD(PoseStack stack, float f, CallbackInfo ci) {
        ViewStack.getInstance().peek().filter(view -> !view.shouldBobView()).ifPresent(view -> ci.cancel());
    }

    /**
     * @see View#shouldPlayerControl
     */
    @Inject(method = "tickFov", at = @At("HEAD"), cancellable = true)
    private void tickFov$HEAD(CallbackInfo ci) {
        ViewStack.getInstance().peek().filter(view -> !view.shouldPlayerControl()).ifPresent(view -> ci.cancel());
    }

    /**
     * @see View#shouldFOVChangeOnMovement
     */
    @Redirect(
            method = "tickFov",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/AbstractClientPlayer;getFieldOfViewModifier()F"
            )
    )
    private float tickFov$HEAD(AbstractClientPlayer player) {
        return ViewStack.getInstance().peek()
                .filter(view -> !view.shouldFOVChangeOnMovement())
                .map(view -> 1.0f)
                .orElse(player.getFieldOfViewModifier());
    }

    @Redirect(
            method = "renderLevel",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 2
            )
    )
    public void renderLevel$mulPose$Pitch(PoseStack stack, Quaternionf quaternion) {
        if (ViewStack.getInstance().peek().isEmpty()) {
            stack.mulPose(quaternion);
        }
    }

    @Redirect(
            method = "renderLevel",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 3
            )
    )
    public void renderLevel$mulPose$Yaw(PoseStack stack, Quaternionf quaternion) {
        ViewStack.getInstance().peek().ifPresentOrElse(
                view -> stack.mulPose(Axis.YN.rotationDegrees(180)),
                () -> stack.mulPose(quaternion));
    }

}
