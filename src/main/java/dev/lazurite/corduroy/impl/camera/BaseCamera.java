package dev.lazurite.corduroy.impl.camera;

import dev.lazurite.corduroy.mixin.access.GameRendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

public abstract class BaseCamera extends Camera {
    protected final MinecraftClient client;
    private boolean renderPlayer = false;
    private boolean renderHand = true;
    private double fov;

    public BaseCamera() {
        super();
        this.client = MinecraftClient.getInstance();
    }

    public void tick() {
        ((GameRendererAccess) client.gameRenderer).setRenderHand(renderHand);
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        this.tick();
//        super.update(area, focusedEntity, thirdPerson, inverseView, tickDelta);
    }

    public abstract Entity getFocusedEntity();

    public void setRenderPlayer(boolean shouldRenderPlayer) {
        this.renderPlayer = shouldRenderPlayer;
    }

    public void setRenderHand(boolean renderHand) {
        this.renderHand = renderHand;
    }

    public boolean shouldRenderPlayer() {
        return this.renderPlayer;
    }

    public boolean shouldRenderHand() {
        return this.renderHand;
    }
}
