package dev.lazurite.corduroy.examplemod.views;

import com.mojang.math.Axis;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.api.view.type.TickingView;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.Random;

/**
 * A simple view which positions itself at a given
 * location and rotation.
 * @see View
 */
public class PointView implements TickingView {
    private final Quaternionf rotation;
    private final Vec3 position;
    private final int distance;

    public PointView(int distance) {
        this.distance = distance;
        this.position = getStartingPosition();
        this.rotation = getTargetDirection();
    }

    @Override
    public Vec3 getPosition() {
        return this.position;
    }

    @Override
    public boolean shouldRenderTarget() {
        return true;
    }

    @Override
    public boolean shouldRenderHud() {
        return false;
    }

    @Override
    public boolean shouldPlayerControl() {
        return true;
    }

    @Override
    public Quaternionf getRotation() {
        return this.rotation;
    }

    @Override
    public void tick() {
        final var targetDirection = getTargetDirection();
        this.rotation.set(targetDirection.x(), targetDirection.y(), targetDirection.z(), targetDirection.w());

        if (getPosition().subtract(getCamera().getEntity().position()).length() > distance * 3.0f) {
            ViewStack.getInstance().pop();
            ViewStack.getInstance().push(new PointView(distance));
        }
    }

    public Vec3 getStartingPosition() {
        final var random = new Random();
        return Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().position().add(
                random.nextInt(distance) - distance / 2.0f,
                random.nextInt(distance) - distance / 2.0f,
                random.nextInt(distance) - distance / 2.0f);
    }

    public Quaternionf getTargetDirection() {
        final var orientation = new Quaternionf(0, 0, 0, 1);
        final var target = getCamera().getEntity().position();
        final var delta = getPosition().subtract(target);

        final var y = (float) Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90;
        final var x = (float) Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));

        orientation.mul(Axis.YN.rotationDegrees(y));
        orientation.mul(Axis.XN.rotationDegrees(x));

        return orientation;
    }
}
