package dev.lazurite.corduroy.impl.camera;

import net.minecraft.entity.Entity;

public abstract class EntityCamera extends CorduroyCamera {
    private Entity entity;

    public EntityCamera(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getFocusedEntity() {
        return this.entity;
    }
}
