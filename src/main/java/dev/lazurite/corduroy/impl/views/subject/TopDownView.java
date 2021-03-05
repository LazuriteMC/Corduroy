package dev.lazurite.corduroy.impl.views.subject;

import dev.lazurite.corduroy.api.view.type.special.TickingView;
import dev.lazurite.corduroy.api.view.type.SubjectView;
import dev.lazurite.corduroy.impl.util.math.QuaternionHelper;
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

    public TopDownView(Entity subject) { // TODO configurable height
        this.subject = subject;
        this.position = subject.getPos().add(new Vec3d(0, 5, 0));
        this.orientation = QuaternionHelper.rotateX(QuaternionHelper.rotateY(new Quaternion(0, 0, 0, 1), -subject.yaw), -90);
    }

    @Override
    public void tick() {
        this.position = subject.getPos().add(new Vec3d(0, 5, 0));
        this.orientation = QuaternionHelper.rotateX(QuaternionHelper.rotateY(new Quaternion(0, 0, 0, 1), -subject.yaw), -90);
    }

    @Override
    public Entity getSubject() {
        return this.subject;
    }

    @Override
    public boolean shouldBobView() {
        return true;
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public Quaternion getOrientation() {
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
