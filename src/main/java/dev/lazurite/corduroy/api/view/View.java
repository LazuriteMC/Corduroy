package dev.lazurite.corduroy.api.view;

import dev.lazurite.corduroy.api.ViewStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

/**
 * A basic {@link View} object can be pushed onto the {@link ViewStack}.
 * What this allows you to do is apply custom behavior to the camera
 * being used on-screen and creates a much more flexible way to control
 * what the player sees.
 *
 * @since 1.0.0
 */
public interface View {
    Vec3 getPosition();
    Quaternionf getRotation();

    default Camera getCamera() {
        return Minecraft.getInstance().gameRenderer.getMainCamera();
    }

    default boolean shouldRenderTarget() {
        return false;
    }

    default boolean shouldRenderHand() {
        return false;
    }

    default boolean shouldRenderHud() {
        return false;
    }

    default boolean shouldBobView() {
        return false;
    }

    default boolean shouldPlayerControl() {
        return false;
    }
}
