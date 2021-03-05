package dev.lazurite.corduroy.api.view;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.type.FreeView;
import dev.lazurite.corduroy.api.view.type.SubjectView;
import dev.lazurite.corduroy.impl.ViewContainer;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

/**
 * A basic {@link View} object can be pushed onto the {@link ViewStack} indirectly
 * through the use of {@link ViewContainer}s. What this allows you to do is apply
 * custom behavior to the camera being used on-screen and creates a much more
 * flexible way to control what the player of Minecraft sees.
 *
 * @since 1.0.0
 * @see SubjectView
 * @see FreeView
 */
public interface View {
    Vec3d getPosition();
    Quaternion getOrientation();

    default boolean shouldRenderHand() {
        return false;
    }

    default boolean shouldRenderPlayer() {
        return true;
    }

    default boolean shouldRenderHud() {
        return false;
    }

    default boolean shouldBobView() {
        return false;
    }
}
