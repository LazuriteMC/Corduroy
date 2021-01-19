package dev.lazurite.corduroy.impl.camera.builder;

import dev.lazurite.corduroy.api.camera.CameraBuilder;
import dev.lazurite.corduroy.impl.camera.DetachedCamera;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class StationaryCameraBuilder implements CameraBuilder {
    private Quaternion rotation = Quaternion.IDENTITY;
    private Vec3d position = Vec3d.ZERO;

    @Override
    public CameraBuilder setPosition(Vec3d position) {
        this.position = position;
        return this;
    }

    @Override
    public CameraBuilder setPosition(double x, double y, double z) {
        this.position = new Vec3d(x, y, z);
        return this;
    }

    @Override
    public CameraBuilder setRotation(Quaternion rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    public CameraBuilder setPitch(float pitch) {
        this.rotation = Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F - pitch);
        return this;
    }

    @Override
    public CameraBuilder setYaw(float yaw) {
        this.rotation = Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - yaw);
        return null;
    }

    @Override
    public Camera build() {
        return new DetachedCamera(position, rotation);
    }
}
