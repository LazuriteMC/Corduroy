package dev.lazurite.corduroy.api.camera;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public interface CameraBuilder {
    static CameraBuilder create(CameraType type) {
        return type.getFactory().get();
    }

    CameraBuilder setPosition(Vec3d position);
    CameraBuilder setPosition(double x, double y, double z);
    CameraBuilder setRotation(Quaternion rotation);
    CameraBuilder setPitch(float pitch);
    CameraBuilder setYaw(float yaw);
    Camera build();
}