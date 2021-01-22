package dev.lazurite.corduroy.api.event;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Events relating to {@link View} objects. Most of the events here
 * also closely relate to the {@link ViewStack}. Specifically, there
 * is a push and a pop event.
 *
 * @since 1.0.0
 * @see ViewStack
 */
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
