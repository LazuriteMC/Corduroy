package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.mixin.access.GameRendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

import java.util.Stack;

public final class ViewStack extends Stack<Camera> {
    public static ViewStack INSTANCE;

    private ViewStack() {
    }

    public static void init(Camera camera) {
        INSTANCE = new ViewStack();
        INSTANCE.push(camera);
    }

    @Override
    public Camera push(Camera camera) {
        if (MinecraftClient.getInstance().gameRenderer != null) {
            ((GameRendererAccess) MinecraftClient.getInstance().gameRenderer).setCamera(camera);
        }

        return super.push(camera);
    }

    @Override
    public Camera pop() {
        Camera pop = super.pop();
        ((GameRendererAccess) MinecraftClient.getInstance().gameRenderer).setCamera(pop);
        return pop;
    }
}
