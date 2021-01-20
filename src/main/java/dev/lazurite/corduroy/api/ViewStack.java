package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.api.camera.Locked;
import dev.lazurite.corduroy.api.event.CameraEvents;
import dev.lazurite.corduroy.mixin.access.GameRendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

import java.util.Stack;

public final class ViewStack extends Stack<Camera> {
    private static ViewStack instance;
    private boolean lock;

    private ViewStack() {
    }

    public static ViewStack getInstance() {
        return instance;
    }

    public static void create(Camera camera) {
        instance = new ViewStack();
        instance.push(camera);
    }

    public void lock() {
        this.lock = true;
    }

    public void unlock() {
        this.lock = false;
    }

    public boolean isLocked() {
        return this.lock;
    }

    @Override
    public Camera push(Camera camera) {
        if (!isLocked()) {
            super.push(camera);
            CameraEvents.VIEW_STACK_PUSH.invoker().onPush(camera);

            if (camera instanceof Locked) {
                this.lock();
            }

            if (MinecraftClient.getInstance().gameRenderer != null) {
                ((GameRendererAccess) MinecraftClient.getInstance().gameRenderer).setCamera(camera);
            }
        }

        return camera;
    }

    @Override
    public Camera pop() {
        Camera camera;

        if (size() > 1 && !isLocked()) {
            camera = super.pop();
            CameraEvents.VIEW_STACK_POP.invoker().onPop(camera);

            ((GameRendererAccess) MinecraftClient.getInstance().gameRenderer).setCamera(peek());
        } else {
            camera = peek();
        }

        return camera;
    }
}
