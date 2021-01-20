package dev.lazurite.corduroy.cameras;

import dev.lazurite.corduroy.cameras.base.FreeCamera;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class PointCamera extends FreeCamera {
    private final Quaternion orientation;

    public PointCamera(Vec3d position, Quaternion orientation) {
        this.setPos(position);
        this.orientation = orientation;
    }

    @Override
    public void tick() {

    }

    @Override
    public Quaternion getOrientation() {
        return this.orientation;
    }

    public void setPitch(float pitch) {
        this.orientation.hamiltonProduct(Vector3f.NEGATIVE_X.getDegreesQuaternion(pitch));
    }

    public void setYaw(float yaw) {
        this.orientation.hamiltonProduct(Vector3f.NEGATIVE_Y.getDegreesQuaternion(yaw));
    }

    public void setRoll(float roll) {
        this.orientation.hamiltonProduct(Vector3f.NEGATIVE_Z.getDegreesQuaternion(roll));
    }
}
