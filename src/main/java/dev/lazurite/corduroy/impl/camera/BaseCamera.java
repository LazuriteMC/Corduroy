package dev.lazurite.corduroy.impl.camera;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

public abstract class BaseCamera extends Camera {
    protected final MinecraftClient client;

    public BaseCamera() {
//        super();
        this.client = MinecraftClient.getInstance();
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        this.tick();
    }

    public abstract void tick();
    public abstract Entity getFocusedEntity();
    public abstract boolean shouldRenderPlayer();
    public abstract boolean shouldRenderHand();
    public abstract double getFieldOfView();
}
