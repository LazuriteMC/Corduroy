package dev.lazurite.corduroy.impl.mixin.render;

import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.api.view.type.SubjectView;
import dev.lazurite.corduroy.impl.ViewContainer;
import dev.lazurite.corduroy.impl.util.QuaternionHelper;
import dev.lazurite.corduroy.impl.mixin.access.GameRendererAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;
    @Unique private boolean bobView;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftClient client, BufferBuilderStorage bufferBuilders, CallbackInfo info) {
        this.bobView = client.options.bobView;
    }

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
                    ordinal = 3
            )
    )
    public Entity getFocusedEntity(Camera camera) {
        if (camera instanceof ViewContainer) {
            if (((ViewContainer) camera).getView().shouldRenderPlayer()) {
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
        if (camera instanceof ViewContainer) {
            ViewContainer container = (ViewContainer) camera;

            /* Matrices fix screen rotation */
            Quaternion q = QuaternionHelper.slerp(container.prevOrientation, container.getView().getOrientation(), tickDelta);
            QuaternionHelper.rotateY(q, 180);
            q.set(q.getX(), -q.getY(), q.getZ(), -q.getW());
            Matrix4f newMat = new Matrix4f(q);
            newMat.transpose();
            matrices.peek().getModel().multiply(newMat);

            /* Apply boolean flags */
            ((GameRendererAccess) client.gameRenderer).setRenderHand(container.getView().shouldRenderHand());
            client.options.bobView = container.getView() instanceof SubjectView && ((SubjectView) container.getView()).shouldBobView();
//            client.options.setPerspective(Perspective.FIRST_PERSON);
        } else {
            ((GameRendererAccess) client.gameRenderer).setRenderHand(true);
            client.options.bobView = bobView;
        }
    }
}
