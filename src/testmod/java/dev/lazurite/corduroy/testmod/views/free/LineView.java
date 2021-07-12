package dev.lazurite.corduroy.testmod.views.free;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.api.view.type.special.TemporaryView;
import dev.lazurite.corduroy.api.view.type.special.TickingView;
import dev.lazurite.corduroy.impl.util.QuaternionHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

/**
 * A view that travels in a straight line for a given amount of ticks.
 * Position and orientation are both interpolated throughout it's
 * lifespan. When the view has finished, it automatically pops itself
 * from the {@link ViewStack}.
 * @see ViewStack
 * @see View
 */
@Environment(EnvType.CLIENT)
public class LineView implements View, TickingView, TemporaryView {
    private final Vec3d startPosition;
    private final Vec3d endPosition;
    private final Quaternion startOrientation;
    private final Quaternion endOrientation;
    private final int lifeSpan;

    private Vec3d position;
    private Quaternion rotation;
    private int age;

    public LineView(Vec3d startPosition, Vec3d endPosition, int lifeSpan) {
        this(startPosition, endPosition, Quaternion.IDENTITY, Quaternion.IDENTITY, lifeSpan);
    }

    public LineView(Vec3d startPosition, Vec3d endPosition, Quaternion startOrientation, Quaternion endOrientation, int lifeSpan) {
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
        float delta = getAge() / (float) getLifeSpan();
        double x = MathHelper.lerp(delta, startPosition.x, endPosition.x);
        double y = MathHelper.lerp(delta, startPosition.y, endPosition.y);
        double z = MathHelper.lerp(delta, startPosition.z, endPosition.z);
        this.position = new Vec3d(x, y, z);
        this.rotation = QuaternionHelper.slerp(startOrientation, endOrientation, delta);
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public Quaternion getRotation() {
        return this.rotation;
    }
}
