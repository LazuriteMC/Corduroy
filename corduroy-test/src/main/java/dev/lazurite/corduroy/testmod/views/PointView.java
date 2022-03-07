package dev.lazurite.corduroy.testmod.views;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.api.view.type.TickingView;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

/**
 * A simple view which positions itself at a given
 * location and rotation.
 * @see View
 */
public class PointView implements TickingView {
    private final Quaternion rotation;
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
    public Quaternion getRotation() {
        return this.rotation;
    }

    @Override
    public void tick() {
        final var targetDirection = getTargetDirection();
        this.rotation.set(targetDirection.i(), targetDirection.j(), targetDirection.k(), targetDirection.r());

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

    public Quaternion getTargetDirection() {
        final var orientation = new Quaternion(Quaternion.ONE);
        final var target = getCamera().getEntity().position();
        final var delta = getPosition().subtract(target);

        final var y = (float) Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90;
        final var x = (float) Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));

        orientation.mul(Vector3f.YN.rotationDegrees(y));
        orientation.mul(Vector3f.XN.rotationDegrees(x));

        return orientation;
    }
}
