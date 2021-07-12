package dev.lazurite.corduroy.testmod.views.subject;

import dev.lazurite.corduroy.api.view.type.special.TickingView;
import dev.lazurite.corduroy.api.view.type.SubjectView;
import dev.lazurite.corduroy.impl.util.QuaternionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

/**
 * This {@link SubjectView} looks straight down at a given {@link Entity}.
 * @see SubjectView
 */
public class TopDownView implements SubjectView, TickingView {
    private final Entity subject;
    private Quaternion orientation;
    private Vec3d position;
    private float height;

    public TopDownView(Entity subject, float height) {
        this.subject = subject;
        this.height = height;
        this.position = subject.getPos().add(new Vec3d(0, height, 0));
        this.orientation = QuaternionHelper.rotateX(QuaternionHelper.rotateY(new Quaternion(0, 0, 0, 1), -subject.getYaw()+ 180), 90);
    }

    @Override
    public void tick() {
        this.position = subject.getPos().add(new Vec3d(0, height, 0));
        this.orientation = QuaternionHelper.rotateX(QuaternionHelper.rotateY(new Quaternion(0, 0, 0, 1), -subject.getYaw() + 180), 90);
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return this.height;
    }

    @Override
    public Entity getSubject() {
        return this.subject;
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public Quaternion getRotation() {
        return this.orientation;
    }
}
