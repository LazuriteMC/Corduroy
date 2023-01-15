package dev.lazurite.corduroy.examplemod.views;

import com.mojang.math.Axis;
import dev.lazurite.corduroy.api.View;
import net.minecraft.world.phys.Vec3;

public class TopDownView extends View implements View.Ticking {

    private final float height;

    public TopDownView(float height) {
        this.height = height;
        this.tick();
    }

    @Override
    public void tick() {
        final var entity = getCamera().getEntity();
        this.position = entity.position().add(new Vec3(0, this.height, 0));
        this.rotation = Axis.YN.rotationDegrees(entity.getYRot() + 180);
        this.rotation.mul(Axis.XN.rotationDegrees(90));
    }

    @Override
    public boolean shouldRenderTarget() {
        return true;
    }

    @Override
    public boolean shouldPlayerControl() {
        return true;
    }

}
