package dev.lazurite.corduroy.testmod.views.subject;

import dev.lazurite.corduroy.api.view.type.SubjectView;
import dev.lazurite.corduroy.api.view.type.special.TemporaryView;
import dev.lazurite.corduroy.api.view.type.special.TickingView;
import dev.lazurite.toolbox.math.QuaternionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class GlideInView implements SubjectView, TemporaryView, TickingView {
    private final Entity subject;
    private final Vec3d startPosition;

    private Quaternion rotation;
    private Vec3d position;
    private int duration;
    private int age;

    public GlideInView(Entity subject, double distance, double distanceY, int duration) {
        this.subject = subject;
        this.startPosition = subject.getPos().add(new Vec3d(-Math.sin(Math.toRadians(subject.getYaw() + 180)) * distance, distanceY, Math.cos(Math.toRadians(subject.getYaw() + 180)) * distance));
        this.duration = duration;
        this.position = startPosition;

        var deltaPos = subject.getPos().subtract(startPosition);
        var yaw = (float) Math.toDegrees(Math.atan2(deltaPos.z, deltaPos.x)) - 90;
        var pitch = (float) Math.toDegrees(Math.atan2(deltaPos.y, Math.sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z)));
        this.rotation = QuaternionHelper.rotateX(QuaternionHelper.rotateY(new Quaternion(0, 0, 0, 1), -yaw + 180), -pitch);
    }

    @Override
    public void tick() {
        float delta = getAge() / (float) getLifeSpan();
        double x = MathHelper.lerp(delta, startPosition.x, subject.getX());
        double y = MathHelper.lerp(delta, startPosition.y, subject.getY() + subject.getStandingEyeHeight());
        double z = MathHelper.lerp(delta, startPosition.z, subject.getZ());
        this.position = new Vec3d(x, y, z);

        Vec3d deltaPos = subject.getPos().subtract(startPosition);
        float yaw = (float) Math.toDegrees(Math.atan2(deltaPos.z, deltaPos.x)) - 90;
        float pitch = (float) Math.toDegrees(Math.atan2(deltaPos.y, Math.sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z)));
        this.rotation = QuaternionHelper.rotateX(QuaternionHelper.rotateY(new Quaternion(0, 0, 0, 1), -yaw + 180), -pitch);
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public Quaternion getRotation() {
        return this.rotation;
    }

    @Override
    public Entity getSubject() {
        return this.subject;
    }

    @Override
    public boolean shouldRenderSubject() {
        return true;
    }

    @Override
    public int getLifeSpan() {
        return this.duration;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int getAge() {
        return this.age;
    }
}
