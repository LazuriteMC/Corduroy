package dev.lazurite.corduroy.api.view.type;

import dev.lazurite.corduroy.api.view.View;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

/**
 * A {@link View} that is not tethered to an entity.
 *
 * @since 1.0.0
 * @see View
 */
public interface FreeView extends View {
    void setPosition(Vec3d position);
    void setOrientation(Quaternion orientation);
}
