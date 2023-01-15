package dev.lazurite.corduroy.examplemod.views;

import com.mojang.math.Axis;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.View;
import net.minecraft.client.Minecraft;
import org.joml.Quaternionf;

import java.util.Random;

/**
 * A simple view which positions itself at a given
 * location and rotation.
 * @see View
 */
public class PointView extends View implements View.Ticking {

    private final int distance;

    public PointView(int distance) {
        this.distance = distance;
        var random = new Random();
        this.position = Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().position().add(
                random.nextInt(distance) - distance / 2.0f,
                random.nextInt(distance) - distance / 2.0f,
                random.nextInt(distance) - distance / 2.0f);
    }

    @Override
    public boolean shouldRenderTarget() {
        return true;
    }

    @Override
    public boolean shouldRenderHud() {
        return false;
    }

    @Override
    public boolean shouldPlayerControl() {
        return true;
    }

    @Override
    public void tick() {
        if (this.getPosition(1.0f).subtract(getCamera().getEntity().position()).length() > distance * 3.0f) {
            ViewStack.getInstance().pop();
            ViewStack.getInstance().push(new PointView(distance));
        }

        this.rotation = new Quaternionf(0, 0, 0, 1);
        var target = getCamera().getEntity().position();
        var delta = this.position.subtract(target);
        var y = (float) Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90;
        var x = (float) Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));
        this.rotation.mul(Axis.YN.rotationDegrees(y));
        this.rotation.mul(Axis.XN.rotationDegrees(x));
    }

}
