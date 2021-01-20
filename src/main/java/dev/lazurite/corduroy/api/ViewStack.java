package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.api.event.ViewEvents;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.api.view.special.LockedView;
import dev.lazurite.corduroy.impl.ViewCamera;
import dev.lazurite.corduroy.mixin.access.GameRendererAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;

import java.util.EmptyStackException;
import java.util.Stack;

@Environment(EnvType.CLIENT)
public final class ViewStack extends Stack<View> {
    private static ViewStack instance;
    private Camera camera;
    private boolean lock;

    private ViewStack() {
    }

    public static ViewStack getInstance() {
        return instance;
    }

    public static void create(Camera camera) {
        instance = new ViewStack();
        instance.camera = camera;
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
    public View push(View view) {
        if (!isLocked()) {
            super.push(view);
            ViewEvents.VIEW_STACK_PUSH.invoker().onPush(view);
            setCamera(view);

            if (view instanceof LockedView) {
                this.lock();
            }
        }

        return view;
    }

    @Override
    public View pop() {
        View view;

        if (!isLocked()) {
            view = super.pop();
            ViewEvents.VIEW_STACK_POP.invoker().onPop(view);
            setCamera(peek());
        } else {
            view = peek();
        }

        return view;
    }

    @Override
    public View peek() {
        try {
            return super.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    private void setCamera(View view) {
        GameRenderer gameRenderer = MinecraftClient.getInstance().gameRenderer;

        if (gameRenderer != null) {
            if (view == null) {
                ((GameRendererAccess) gameRenderer).setCamera(camera);
            } else {
                ((GameRendererAccess) gameRenderer).setCamera(new ViewCamera(view));
            }
        }
    }
}
