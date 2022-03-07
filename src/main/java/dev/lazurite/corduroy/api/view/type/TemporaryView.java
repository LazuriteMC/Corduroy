package dev.lazurite.corduroy.api.view.type;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.mixin.ClientLevelMixin;

/**
 * This allows a {@link View} to only temporarily
 * exist on the {@link ViewStack}. When its age has
 * exceeded its lifespan, it is automatically popped
 * from the {@link ViewStack}.
 *
 * @since 1.0.0
 * @see ClientLevelMixin
 * @see ViewStack
 */
public interface TemporaryView extends View {
    default void onExit() {
        ViewStack.getInstance().pop();
    }

    int getLifeSpan();
    void setAge(int age);
    int getAge();
}
