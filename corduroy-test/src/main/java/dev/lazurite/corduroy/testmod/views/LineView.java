package dev.lazurite.corduroy.testmod.views;

import com.mojang.math.Quaternion;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.api.view.type.TemporaryView;
import dev.lazurite.corduroy.api.view.type.TickingView;
import dev.lazurite.corduroy.impl.util.QuaternionUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * A view that travels in a straight line for a given amount of ticks.
 * Position and orientation are both interpolated throughout it's
 * lifespan. When the view has finished, it automatically pops itself
 * from the {@link ViewStack}.
 * @see ViewStack
 * @see View
 */
public class LineView implements TickingView, TemporaryView {
    private final Vec3 startPosition;
    private final Vec3 endPosition;
    private final Quaternion startOrientation;
    private final Quaternion endOrientation;
    private final int lifeSpan;

    private Vec3 position;
    private Quaternion rotation;
    private int age;

    public LineView(Vec3 startPosition, Vec3 endPosition, int lifeSpan) {
        this(startPosition, endPosition, Quaternion.ONE, Quaternion.ONE, lifeSpan);
    }

    public LineView(Vec3 startPosition, Vec3 endPosition, Quaternion startOrientation, Quaternion endOrientation, int lifeSpan) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.startOrientation = startOrientation;
        this.endOrientation = endOrientation;
        this.lifeSpan = lifeSpan;

        this.position = startPosition;
        this.rotation = startOrientation;
    }

    @Override
    public int getLifeSpan() {
        return this.lifeSpan;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public void tick() {
        final var delta = getAge() / (float) getLifeSpan();
        final var x = Mth.lerp(delta, startPosition.x, endPosition.x);
        final var y = Mth.lerp(delta, startPosition.y, endPosition.y);
        final var z = Mth.lerp(delta, startPosition.z, endPosition.z);
        this.position = new Vec3(x, y, z);
        this.rotation = QuaternionUtil.slerp(startOrientation, endOrientation, delta);
    }

    @Override
    public Vec3 getPosition() {
        return this.position;
    }

    @Override
    public Quaternion getRotation() {
        return this.rotation;
    }
}
