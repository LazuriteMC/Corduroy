package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.impl.ViewContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
public class PreventativeMixin {

    @Mixin(GameRenderer.class)
    public static class GameRendererMixin {
        @Shadow @Final private MinecraftClient client;
        @Shadow public Camera camera;

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
                if (!((ViewContainer) camera).getView().shouldPlayerControl()) {
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
                stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            }
        }
    }

    @Mixin(InGameHud.class)
    public static class InGameHudMixin {
        @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
        protected void renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo info) {
            ViewStack.getInstance().peek().ifPresent(view -> {
                if (!view.shouldRenderHud()) {
                    info.cancel();
                }
            });
        }

        @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
        private void renderCrosshair(MatrixStack matrices, CallbackInfo info) {
            ViewStack.getInstance().peek().ifPresent(view -> {
                if (!view.shouldRenderHud()) {
                    info.cancel();
                }
            });
        }
    }

    @Mixin(Mouse.class)
    public static class MouseMixin {
        @Redirect(
                method = "updateMouse",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
                )
        )
        public void changeLookDirection(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
            var view = ViewStack.getInstance().peek();

            if (view.isPresent() && view.get().shouldPlayerControl()) {
                return;
            }

            player.changeLookDirection(cursorDeltaX, cursorDeltaY);
        }

        @Redirect(
                method = "onMouseButton",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V"
                )
        )
        public void setKeyPressed(InputUtil.Key key, boolean pressed) {
            var view = ViewStack.getInstance().peek();

            if (view.isPresent() && view.get().shouldPlayerControl()) {
                return;
            }

            KeyBinding.setKeyPressed(key, pressed);
        }

        @Redirect(
                method = "onMouseButton",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/option/KeyBinding;onKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;)V"
                )
        )
        public void onKeyPressed(InputUtil.Key key) {
            var view = ViewStack.getInstance().peek();

            if (view.isPresent() && view.get().shouldPlayerControl()) {
                return;
            }

            KeyBinding.onKeyPressed(key);
        }
    }
}
