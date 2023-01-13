package dev.lazurite.corduroy.examplemod.views;

import com.mojang.math.Axis;
import dev.lazurite.corduroy.api.view.type.TemporaryView;
import dev.lazurite.corduroy.api.view.type.TickingView;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class GlideInView implements TemporaryView, TickingView {
    private final Vec3 startPosition;
    private Quaternionf rotation;
    private Vec3 position;
    private int duration;
    private int age;

    public GlideInView(double distance, double distanceY, int duration) {
        final var entity = getCamera().getEntity();

        this.startPosition = entity.position().add(new Vec3(-Math.sin(Math.toRadians(entity.getXRot() + 180)) * distance, distanceY, Math.cos(Math.toRadians(entity.getXRot() + 180)) * distance));
        this.duration = duration;
        this.position = startPosition;

        final var deltaPos = entity.position().subtract(startPosition);
        final var yaw = (float) Math.toDegrees(Math.atan2(deltaPos.z, deltaPos.x)) - 90;
        final var pitch = (float) Math.toDegrees(Math.atan2(deltaPos.y, Math.sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z)));
        this.rotation = Axis.YP.rotationDegrees(-yaw + 180);
        this.rotation.mul(Axis.XP.rotationDegrees(pitch));
    }

    @Override
    public void tick() {
        final var entity = getCamera().getEntity();
        final var delta = getAge() / (float) getLifeSpan();
        final var x = Mth.lerp(delta, startPosition.x, entity.getX());
        final var y = Mth.lerp(delta, startPosition.y, entity.getY() + entity.getEyeHeight());
        final var z = Mth.lerp(delta, startPosition.z, entity.getZ());
        this.position = new Vec3(x, y, z);

        final var deltaPos = entity.position().subtract(startPosition);
        final var yaw = (float) Math.toDegrees(Math.atan2(deltaPos.z, deltaPos.x)) - 90;
        final var pitch = (float) Math.toDegrees(Math.atan2(deltaPos.y, Math.sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z)));
        this.rotation = Axis.YP.rotationDegrees(-yaw + 180);
        this.rotation.mul(Axis.XP.rotationDegrees(pitch));
    }

    @Override
    public Vec3 getPosition() {
        return this.position;
    }

    @Override
    public Quaternionf getRotation() {
        return this.rotation;
    }

    @Override
    public boolean shouldRenderTarget() {
        return true;
    }

    @Override
    public boolean shouldPlayerControl() {
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
