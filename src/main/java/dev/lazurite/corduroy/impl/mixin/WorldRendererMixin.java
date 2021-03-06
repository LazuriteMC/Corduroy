package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.ViewContainer;
import dev.lazurite.toolbox.math.QuaternionHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    /**
     * Allows the player to be rendered outside of third-person mode.
     * @param camera the camera (probably a {@link ViewContainer})
     * @return the camera's focused entity
     */
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;",
                    ordinal = 0
            )
    )
    public Entity getFocusedEntity(Camera camera) {
        if (camera instanceof ViewContainer container) {
            if (container.getView().shouldRenderPlayer()) {
                return client.player;
            }
        }

        return camera.getFocusedEntity();
    }

    /**
     * Rotates the screen according to the orientation of the camera.
     * @see ViewContainer
     * @see View
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
        if (camera instanceof ViewContainer container) {
            /* Matrices fix screen rotation */
            var q = QuaternionHelper.slerp(container.prevOrientation, container.getView().getRotation(), tickDelta);
            QuaternionHelper.rotateY(q, 180);
            q.set(q.getX(), -q.getY(), q.getZ(), -q.getW());

            var newMat = new Matrix4f(q);
            newMat.transpose();
            matrices.peek().getModel().multiply(newMat);
        }
    }
}
