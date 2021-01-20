package dev.lazurite.corduroy.impl.views;

import dev.lazurite.corduroy.api.view.FreeView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class PointView implements FreeView {
    private final Quaternion orientation;
    private final Vec3d position;

    public PointView(Vec3d position, Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    @Override
    public void setPosition(Vec3d position) {
    }

    @Override
    public void setOrientation(Quaternion orientation) {
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public Vec3d getPreviousPosition() {
        return this.position;
    }

    @Override
    public Quaternion getOrientation() {
        return this.orientation;
    }

    @Override
    public Quaternion getPreviousOrientation() {
        return this.orientation;
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
