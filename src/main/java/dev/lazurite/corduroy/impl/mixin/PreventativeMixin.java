package dev.lazurite.corduroy.impl.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class PreventativeMixin {

    @Mixin(GameRenderer.class)
    public static class GameRendererMixin {
        /**
         * @see View#shouldRenderHand
         */
        @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
        public void renderHand_HEAD(PoseStack matrices, Camera camera, float tickDelta, CallbackInfo info) {
            ViewStack.getInstance().peek().ifPresent(view -> {
                if (!view.shouldRenderHand()) {
                    info.cancel();
                }
            });
        }

        /**
         * @see View#shouldBobView
         */
        @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
        public void bobView(PoseStack stack, float f, CallbackInfo info) {
            ViewStack.getInstance().peek().ifPresent(view -> {
                if (!view.shouldBobView()) {
                    info.cancel();
                }
            });
        }

        /**
         * @see View#shouldBobView
         */
        @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
        private void bobViewWhenHurt(PoseStack stack, float f, CallbackInfo info) {
            ViewStack.getInstance().peek().ifPresent(view -> {
                if (!view.shouldBobView()) {
                    info.cancel();
                }
            });
        }

        @Inject(method = "tickFov", at = @At("HEAD"), cancellable = true)
        private void tickFov_HEAD(CallbackInfo info) {
            ViewStack.getInstance().peek().ifPresent(view -> {
                if (!view.shouldPlayerControl()) {
                    info.cancel();
                }
            });
        }

        @Redirect(
                method = "renderLevel",
                at = @At(
                        value = "INVOKE",
                        target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lcom/mojang/math/Quaternion;)V",
                        ordinal = 2
                )
        )
        public void renderLevel_mulPose_Pitch(PoseStack stack, Quaternion quaternion) {
            if (ViewStack.getInstance().peek().isEmpty()) {
                stack.mulPose(quaternion);
            }
        }

        @Redirect(
                method = "renderLevel",
                at = @At(
                        value = "INVOKE",
                        target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lcom/mojang/math/Quaternion;)V",
                        ordinal = 3
                )
        )
        public void renderLevel_mulPose_Yaw(PoseStack stack, Quaternion quaternion) {
            ViewStack.getInstance().peek().ifPresentOrElse(
                    view -> stack.mulPose(Vector3f.YN.rotationDegrees(180)),
                    () -> stack.mulPose(quaternion));
        }
    }

    @Mixin(Gui.class)
    public static class InGameHudMixin {
        @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
        protected void renderHotbar_HEAD(float tickDelta, PoseStack matrices, CallbackInfo info) {
            ViewStack.getInstance().peek().ifPresent(view -> {
                if (!view.shouldRenderHud()) {
                    info.cancel();
                }
            });
        }

        @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
        private void renderCrosshair_HEAD(PoseStack matrices, CallbackInfo info) {
            ViewStack.getInstance().peek().ifPresent(view -> {
                if (!view.shouldRenderHud()) {
                    info.cancel();
                }
            });
        }
    }

    @Mixin(MouseHandler.class)
    public static class MouseMixin {
        @Redirect(
                method = "turnPlayer",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
                )
        )
        public void turnPlayer_turn(LocalPlayer player, double cursorDeltaX, double cursorDeltaY) {
            var view = ViewStack.getInstance().peek();

            if (view.isPresent() && view.get().shouldPlayerControl()) {
                return;
            }

            player.turn(cursorDeltaX, cursorDeltaY);
        }

        @Redirect(
                method = "onPress",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/KeyMapping;set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V"
                )
        )
        public void onPress_set(InputConstants.Key key, boolean pressed) {
            if (ViewStack.getInstance().peek().filter(view -> !view.shouldPlayerControl()).isEmpty()) {
                KeyMapping.set(key, pressed);
            }
        }
    }
}
