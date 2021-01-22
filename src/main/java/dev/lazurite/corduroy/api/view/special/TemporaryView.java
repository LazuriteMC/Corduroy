package dev.lazurite.corduroy.api.view.special;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.mixin.ClientWorldMixin;

/**
 * This allows a {@link View} to only temporarily
 * exist on the {@link ViewStack}. When its age has
 * exceeded its lifespan, it is automatically popped
 * from the {@link ViewStack}.
 * @see ClientWorldMixin
 * @see ViewStack
 */
public interface TemporaryView {
    default void finish() {
        ViewStack.getInstance().pop();
    }

    int getLifeSpan();
    void setAge(int age);
    int getAge();
}
