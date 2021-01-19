package dev.lazurite.corduroy.impl.camera;

import net.minecraft.entity.Entity;

public class FirstPersonCamera extends CorduroyCamera {
    private Entity entity;

    public FirstPersonCamera(Entity entity) {
        this.entity = entity;
    }

    public void tick() {

    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getFocusedEntity() {
        return entity;
    }
}
