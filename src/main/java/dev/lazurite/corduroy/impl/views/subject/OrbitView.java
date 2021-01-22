package dev.lazurite.corduroy.impl.views.subject;

import dev.lazurite.corduroy.api.view.type.special.TickingView;
import dev.lazurite.corduroy.api.view.type.SubjectView;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

/**
 * This {@link SubjectView} rotates around a given {@link Entity}.
 * @see SubjectView
 */
public class OrbitView implements SubjectView, TickingView {
    private final Entity subject;
    private Quaternion orientation;
    private Vec3d position;

    public OrbitView(Entity subject) {
        this.subject = subject;
        this.position = subject.getPos();
        this.orientation = Vector3f.NEGATIVE_X.getDegreesQuaternion(90);
    }

    @Override
    public void tick() {
        this.position = subject.getPos().add(new Vec3d(0, 5, 0));
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
