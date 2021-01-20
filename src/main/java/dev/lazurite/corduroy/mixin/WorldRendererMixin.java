package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.cameras.base.BaseCamera;
import dev.lazurite.corduroy.mixin.access.GameRendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
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
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    /**
     * This makes sure that the player's hand renders when appropriate.
     * @see BaseCamera
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
        if (camera instanceof BaseCamera) {
            BaseCamera base = (BaseCamera) camera;
            ((GameRendererAccess) gameRenderer).setRenderHand(base.shouldRenderHand());
            matrices.multiply(base.getOrientation());
        }
    }

    /**
     * This allows for the player to render whenever the current camera specifies.
     * @see BaseCamera
     */
    @Redirect(
        method = "render",
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;",
                ordinal = 3
        )
    )
    public Entity getFocusedEntity(Camera camera) {
        if (camera instanceof BaseCamera) {
            if (((BaseCamera) camera).shouldRenderPlayer()) {
                return client.player;
            }
        }

        return camera.getFocusedEntity();
    }
}
