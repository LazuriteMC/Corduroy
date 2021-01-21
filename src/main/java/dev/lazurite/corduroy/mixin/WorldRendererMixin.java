package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.ViewContainer;
import dev.lazurite.corduroy.impl.math.QuaternionHelper;
import dev.lazurite.corduroy.mixin.access.GameRendererAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    /**
     * Rotates the screen according to the orientation of the camera.
     * @see ViewContainer
     * @see View
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
        if (camera instanceof ViewContainer) {
            ViewContainer container = (ViewContainer) camera;
            matrices.multiply(QuaternionHelper.slerp(container.getPrevOrientation(), container.getView().getOrientation(), tickDelta));
            ((GameRendererAccess) client.gameRenderer).setRenderHand(container.getView().shouldRenderHand());
        } else {
            ((GameRendererAccess) client.gameRenderer).setRenderHand(true);
        }
    }

//    /**
//     * This allows for the player to render whenever the current camera specifies.
//     * @see ViewCamera
//     * @see View
//     */
//    @Redirect(
//        method = "render",
//        at = @At(
//                value = "INVOKE",
//                target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;",
//                ordinal = 3
//        )
//    )
//    public Entity getFocusedEntity(Camera camera) {
//        if (camera instanceof ViewCamera) {
//            if (((ViewCamera) camera).getView().shouldRenderPlayer()) {
//                System.out.println("RENDER PLAYER");
//                return client.player;
//            }
//        }
//
//        System.out.println("DONT RENDER PLAYER");
//        return camera.getFocusedEntity();
//    }
}
