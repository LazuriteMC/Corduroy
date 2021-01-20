package dev.lazurite.corduroy.impl.views;

import dev.lazurite.corduroy.api.view.FreeView;
import dev.lazurite.corduroy.api.view.special.TemporaryView;
import dev.lazurite.corduroy.api.view.special.TickableView;
import dev.lazurite.corduroy.impl.math.QuaternionHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class LineView implements FreeView, TickableView, TemporaryView {
    private final Vec3d startPosition;
    private final Vec3d endPosition;
    private final Quaternion startOrientation;
    private final Quaternion endOrientation;
    private final int lifeSpan;

    private Vec3d position;
    private Vec3d prevPosition;
    private Quaternion orientation;
    private Quaternion prevOrientation;
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
        this.prevPosition = startPosition;
        this.orientation = startOrientation;
        this.prevOrientation = startOrientation;
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
        prevPosition = position;
        prevOrientation = orientation;

        float delta = getAge() / (float) getLifeSpan();
        double x = MathHelper.lerp(delta, startPosition.x, endPosition.x);
        double y = MathHelper.lerp(delta, startPosition.y, endPosition.y);
        double z = MathHelper.lerp(delta, startPosition.z, endPosition.z);
        setPosition(new Vec3d(x, y, z));
        setOrientation(QuaternionHelper.slerp(startOrientation, endOrientation, delta));
    }

    @Override
    public void setPosition(Vec3d position) {
        this.position = position;
    }

    @Override
    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public Vec3d getPreviousPosition() {
        return this.prevPosition;
    }

    @Override
    public Quaternion getOrientation() {
        return this.orientation;
    }

    @Override
    public Quaternion getPreviousOrientation() {
        return this.prevOrientation;
    }

    @Override
    public boolean shouldRenderHand() {
        return false;
    }

    @Override
    public boolean shouldRenderPlayer() {
        return true;
    }

    @Override
    public boolean shouldRenderHud() {
        return false;
    }
}
