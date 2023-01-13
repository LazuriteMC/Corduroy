package dev.lazurite.corduroy.examplemod.views;

import com.mojang.math.Axis;
import dev.lazurite.corduroy.api.view.type.TickingView;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class TopDownView implements TickingView {
    private Quaternionf orientation;
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
        this.orientation = Axis.YN.rotationDegrees(entity.getYRot() + 180);
        this.orientation.mul(Axis.XN.rotationDegrees(90));
    }

    public float getHeight() {
        return this.height;
    }

    @Override
    public boolean shouldRenderTarget() {
        return true;
    }

    @Override
    public boolean shouldPlayerControl() {
        return true;
    }

    @Override
    public Vec3 getPosition() {
        return this.position;
    }

    @Override
    public Quaternionf getRotation() {
        return this.orientation;
    }
}
