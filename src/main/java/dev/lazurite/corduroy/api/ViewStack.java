package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.api.event.ViewEvents;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.api.view.special.LockedView;
import dev.lazurite.corduroy.impl.ViewContainer;
import dev.lazurite.corduroy.mixin.access.GameRendererAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

import java.util.Stack;

@Environment(EnvType.CLIENT)
public final class ViewStack {
    private static ViewStack instance;
    private final Stack<Camera> stack = new Stack<>();
    private MinecraftClient client;
    private boolean lock;

    private ViewStack(MinecraftClient client, Camera camera) {
        this.client = client;
        this.stack.push(camera);
    }

    public static ViewStack getInstance() {
        return instance;
    }

    public static void create(MinecraftClient client, Camera camera) {
        instance = new ViewStack(client, camera);
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

    public View push(View view) {
        if (!isLocked()) {
            ViewContainer container = new ViewContainer(view);
            stack.push(container);
            ViewEvents.VIEW_STACK_PUSH.invoker().onPush(view);
            ((GameRendererAccess) client.gameRenderer).setCamera(container);

            if (view instanceof LockedView) {
                this.lock();
            }
        }

        return view;
    }

    public View pop() {
        if (stack.size() > 1 && !isLocked()) { // there's at least one view container present
            ViewContainer container = (ViewContainer) stack.pop();
            ViewEvents.VIEW_STACK_POP.invoker().onPop(container.getView());
            ((GameRendererAccess) client.gameRenderer).setCamera(stack.peek());
            return container.getView();
        }

        return null;
    }

    public View peek() {
        if (stack.size() > 1) {
            return ((ViewContainer) stack.peek()).getView();
        }

        return null;
    }
}
