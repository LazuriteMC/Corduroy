package dev.lazurite.corduroy.impl.camera;

import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class LineCamera extends DetachedCamera {
    public LineCamera(Vec3d startPosition, Vec3d endPosition, Quaternion startRotation, Quaternion endRotation) {
        super(startPosition, startRotation);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
