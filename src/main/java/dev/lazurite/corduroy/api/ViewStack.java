package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.api.event.CameraEvents;
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
        super.push(camera);
        CameraEvents.VIEW_STACK_PUSH.invoker().onPush(camera);

        if (MinecraftClient.getInstance().gameRenderer != null) {
            ((GameRendererAccess) MinecraftClient.getInstance().gameRenderer).setCamera(camera);
        }

        return camera;
    }

    @Override
    public Camera pop() {
        Camera camera = super.pop();
        CameraEvents.VIEW_STACK_POP.invoker().onPop(camera);

        ((GameRendererAccess) MinecraftClient.getInstance().gameRenderer).setCamera(camera);
        return camera;
    }
}
