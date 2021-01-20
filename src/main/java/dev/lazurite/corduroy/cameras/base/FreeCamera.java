package dev.lazurite.corduroy.cameras.base;

import net.minecraft.entity.Entity;

public abstract class FreeCamera extends BaseCamera {
    @Override
    public Entity getFocusedEntity() {
        return client.player;
    }

    @Override
    public boolean shouldRenderPlayer() {
        return true;
    }

    @Override
    public boolean shouldRenderHand() {
        return false;
    }

    @Override
    public boolean shouldRenderHud() {
        return false;
    }
}
