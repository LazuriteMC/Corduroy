package dev.lazurite.corduroy.testmod.views;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.lazurite.corduroy.api.view.type.TickingView;
import net.minecraft.world.phys.Vec3;

public class TopDownView implements TickingView {
    private Quaternion orientation;
    private Vec3 position;
    private float height;

    public TopDownView(float height) {
        this.height = height;
        this.tick();
    }

    @Override
    public void tick() {
        final var entity = getCamera().getEntity();
        this.position = entity.position().add(new Vec3(0, height, 0));
        this.orientation = Vector3f.YN.rotationDegrees(entity.getYRot() + 180);
        this.orientation.mul(Vector3f.XN.rotationDegrees(90));
    }

    public float getHeight() {
        return this.height;
    }

    @Override
    public boolean shouldRenderTarget() {
        return true;
    }

    @Override
    public Vec3 getPosition() {
        return this.position;
    }

    @Override
    public Quaternion getRotation() {
        return this.orientation;
    }
}
