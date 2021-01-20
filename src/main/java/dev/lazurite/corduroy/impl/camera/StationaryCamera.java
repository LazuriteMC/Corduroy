package dev.lazurite.corduroy.impl.camera;

import dev.lazurite.corduroy.api.camera.FreeCamera;
import dev.lazurite.corduroy.impl.entity.DummyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class StationaryCamera extends BaseCamera implements FreeCamera {
    private final DummyEntity dummy = new DummyEntity();
    private Vec3d position;
    private Quaternion rotation;

    public StationaryCamera(Vec3d position, Quaternion rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    @Override
    public void tick() {
        System.out.println("am alive!!");
    }

    @Override
    public void setPosition(Vec3d position) {
        this.position = position;
    }

    @Override
    public Vec3d getPosition() {
        return position;
    }

    @Override
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    @Override
    public void setYaw(float yaw) {

    }

    @Override
    public void setPitch(float pitch) {

    }

    @Override
    public Entity getFocusedEntity() {
        return dummy;
    }

    @Override
    public boolean shouldRenderPlayer() {
        return true;
    }

    @Override
    public boolean shouldRenderHand() {
        return false;
    }

    @Override
    public double getFieldOfView() {
        return 100;
    }
}
