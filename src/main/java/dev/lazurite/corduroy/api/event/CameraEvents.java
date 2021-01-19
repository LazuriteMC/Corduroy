package dev.lazurite.corduroy.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;

public final class CameraEvents {
    public static final Event<ViewStackPush> VIEW_STACK_PUSH = EventFactory.createArrayBacked(ViewStackPush.class, (callbacks) -> (camera) -> {
        for (ViewStackPush event : callbacks) {
            event.onPush(camera);
        }
    });

    public static final Event<ViewStackPop> VIEW_STACK_POP = EventFactory.createArrayBacked(ViewStackPop.class, (callbacks) -> (camera) -> {
        for (ViewStackPop event : callbacks) {
            event.onPop(camera);
        }
    });

    private CameraEvents() {
    }

    @FunctionalInterface
    public interface ViewStackPush {
        void onPush(Camera camera);
    }

    @FunctionalInterface
    public interface ViewStackPop {
        void onPop(Camera camera);
    }
}
