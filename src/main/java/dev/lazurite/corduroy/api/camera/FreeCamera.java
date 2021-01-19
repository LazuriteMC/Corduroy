package dev.lazurite.corduroy.api.camera;

import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public interface FreeCamera {
    void setPosition(Vec3d position);
    Vec3d getPosition();

    void setRotation(Quaternion rotation);
    Quaternion getRotation();

    void setYaw(float yaw);
    float getYaw();

    void setPitch(float pitch);
    float getPitch();
}
