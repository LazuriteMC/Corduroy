package dev.lazurite.corduroy.examplemod.views;

import com.mojang.math.Axis;
import dev.lazurite.corduroy.api.View;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GlideInView extends View implements View.Temporary, View.Ticking {

    private final int duration;
    private final Vec3 startPosition;
    private int age;

    public GlideInView(double distance, double distanceY, int duration) {
        var entity = getCamera().getEntity();
        this.startPosition = entity.position().add(new Vec3(
                -Math.sin(Math.toRadians(entity.getXRot() + 180)) * distance,
                distanceY,
                Math.cos(Math.toRadians(entity.getXRot() + 180)) * distance)
        );
        this.duration = duration;
        this.tick();
    }

    @Override
    public void tick() {
        // Update position
        final var entity = getCamera().getEntity();
        final var delta = getAge() / (float) getDuration();
        final var x = Mth.lerp(delta, startPosition.x, entity.getX());
        final var y = Mth.lerp(delta, startPosition.y, entity.getY() + entity.getEyeHeight());
        final var z = Mth.lerp(delta, startPosition.z, entity.getZ());
        this.position = new Vec3(x, y, z);

        // Update rotation
        final var deltaPos = entity.position().subtract(startPosition);
        final var yaw = (float) Math.toDegrees(Math.atan2(deltaPos.z, deltaPos.x)) - 90;
        final var pitch = (float) Math.toDegrees(Math.atan2(deltaPos.y, Math.sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z)));
        this.rotation = Axis.YP.rotationDegrees(-yaw + 180);
        this.rotation.mul(Axis.XP.rotationDegrees(pitch));
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
    public int getDuration() {
        return this.duration;
    }

    @Override
    public void age() {
        this.age++;
    }

    @Override
    public int getAge() {
        return this.age;
    }

}
