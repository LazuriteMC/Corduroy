package dev.lazurite.corduroy.api.event;

import dev.lazurite.corduroy.api.view.View;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ViewEvents {
    public static final Event<ViewStackPush> VIEW_STACK_PUSH = EventFactory.createArrayBacked(ViewStackPush.class, (callbacks) -> (View) -> {
        for (ViewStackPush event : callbacks) {
            event.onPush(View);
        }
    });

    public static final Event<ViewStackPop> VIEW_STACK_POP = EventFactory.createArrayBacked(ViewStackPop.class, (callbacks) -> (View) -> {
        for (ViewStackPop event : callbacks) {
            event.onPop(View);
        }
    });

    private ViewEvents() {
    }

    @FunctionalInterface
    public interface ViewStackPush {
        void onPush(View View);
    }

    @FunctionalInterface
    public interface ViewStackPop {
        void onPop(View View);
    }
}
