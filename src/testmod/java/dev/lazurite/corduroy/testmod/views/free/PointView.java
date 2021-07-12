package dev.lazurite.corduroy.testmod.views.free;

import dev.lazurite.corduroy.api.view.View;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

/**
 * A simple view which positions itself at a given
 * location and rotation.
 * @see View
 */
@Environment(EnvType.CLIENT)
public class PointView implements View {
    private final Quaternion orientation;
    private final Vec3d position;

    public PointView(Vec3d position, Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public Quaternion getRotation() {
        return this.orientation;
    }
}
