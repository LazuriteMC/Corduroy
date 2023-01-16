package dev.lazurite.corduroy.impl;

import dev.lazurite.corduroy.api.View;
import dev.lazurite.corduroy.impl.util.QuaternionUtil;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.joml.Quaternionf;

public class CorduroyCamera extends Camera {

    private final Camera parentCamera;

    public CorduroyCamera(Camera parentCamera) {
        this.parentCamera = parentCamera;
        this.entity = parentCamera.getEntity();
    }

    public Camera getParent() {
        return this.parentCamera;
    }

    public View getView() {
        // we can do this because a CorduroyCamera will never be used if there isn't a view on the stack.
        return ViewStackImpl.INSTANCE.peek().get();
    }

    @Override
    public void setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f) {
        this.initialized = true;
        this.level = blockGetter;
        this.entity = entity;

        var view = this.getView();
        this.position = view.getPosition(f);
        this.blockPosition.set(this.position.x, this.position.y, this.position.z);
        this.rotation = new Quaternionf(view.getRotation(f));
        this.forwards.set(0.0F, 0.0F, 1.0F);
        this.forwards.rotate(this.rotation);
        this.up.set(0.0F, 1.0F, 0.0F);
        this.up.rotate(this.rotation);
        this.left.set(1.0F, 0.0F, 0.0F);
        this.left.rotate(this.rotation);
        this.xRot = QuaternionUtil.getYaw(this.rotation);
        this.yRot = QuaternionUtil.getPitch(this.rotation);

        view.onRender();
    }

    @Override
    public void tick() {
        var view = this.getView();

        /* Interpolate the view's position and rotation */
        view.updatePrevious();

        /* Temporary View */
        if (view instanceof View.Temporary temporaryView) {
            temporaryView.age();

            if (temporaryView.getAge() > temporaryView.getDuration()) {
                temporaryView.onExit();
            }
        }

        /* Ticking View */
        if (view instanceof View.Ticking tickingView) {
            tickingView.tick();
        }
    }

    @Override
    public boolean isDetached() {
        return this.getView().shouldRenderTarget();
    }

}
