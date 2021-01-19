package dev.lazurite.corduroy.impl.camera;

import net.minecraft.entity.Entity;

public class ThirdPersonCamera extends CorduroyCamera {
    public ThirdPersonCamera() {

    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public Entity getFocusedEntity() {
        return null;
    }
}
