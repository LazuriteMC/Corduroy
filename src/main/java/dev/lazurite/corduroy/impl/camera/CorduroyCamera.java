package dev.lazurite.corduroy.impl.camera;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

public abstract class CorduroyCamera extends Camera {
    protected final MinecraftClient client;
    private boolean shouldRenderPlayer = false;
    private boolean shouldRenderHand = true;
    private double fov;

    public CorduroyCamera() {
        super();
        this.client = MinecraftClient.getInstance();
    }

    public void tick() {
        client.gameRenderer.renderHand = shouldRenderHand;
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        this.tick();
//        super.update(area, focusedEntity, thirdPerson, inverseView, tickDelta);
    }

    public abstract Entity getFocusedEntity();

    public void setShouldRenderPlayer(boolean shouldRenderPlayer) {
        this.shouldRenderPlayer = shouldRenderPlayer;
    }

    public void setShouldRenderHand(boolean shouldRenderHand) {
        this.shouldRenderHand = shouldRenderHand;
    }

    public boolean shouldRenderPlayer() {
        return this.shouldRenderPlayer;
    }

    public boolean shouldRenderHand() {
        return this.shouldRenderHand;
    }
}
