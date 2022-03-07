package dev.lazurite.corduroy.testmod.views;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.lazurite.corduroy.api.view.type.TemporaryView;
import dev.lazurite.corduroy.api.view.type.TickingView;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GlideInView implements TemporaryView, TickingView {
    private final Vec3 startPosition;
    private Quaternion rotation;
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
        this.rotation = Vector3f.YP.rotationDegrees(-yaw + 180);
        this.rotation.mul(Vector3f.XP.rotationDegrees(pitch));
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
        this.rotation = Vector3f.YP.rotationDegrees(-yaw + 180);
        this.rotation.mul(Vector3f.XP.rotationDegrees(pitch));
    }

    @Override
    public Vec3 getPosition() {
        return this.position;
    }

    @Override
    public Quaternion getRotation() {
        return this.rotation;
    }

    @Override
    public boolean shouldRenderTarget() {
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
