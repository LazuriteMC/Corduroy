package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.mixin.access.GameRendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

public class GameView {
    public static final GameView INSTANCE = new GameView();

    private GameView() {
    }

    public void setCamera(Camera camera) {
        ((GameRendererAccess) MinecraftClient.getInstance().gameRenderer).setCamera(camera);
    }

    public Camera getCamera() {
        return MinecraftClient.getInstance().gameRenderer.getCamera();
    }
}
