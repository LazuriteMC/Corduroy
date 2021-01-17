package dev.lazurite.corduroy.api.camera;

import dev.lazurite.corduroy.impl.camera.builder.StationaryCameraBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public interface CameraBuilder {
    static CameraBuilder createStationaryCamera() {
        return new StationaryCameraBuilder();
    }

    CameraBuilder setPosition(Vec3d position);
    CameraBuilder setPosition(double x, double y, double z);
    CameraBuilder setRotation(Quaternion rotation);
    CameraBuilder setPitch(float pitch);
    CameraBuilder setYaw(float yaw);
    Camera build();
}