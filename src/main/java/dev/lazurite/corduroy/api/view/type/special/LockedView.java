package dev.lazurite.corduroy.api.view.type.special;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;

/**
 * When this interface is implemented into a {@link View},
 * the {@link ViewStack} is locked until {@link ViewStack#unlock}
 * is called.
 *
 * @since 1.0.0
 * @see ViewStack#isLocked
 */
public interface LockedView {
    default void unlock() {
        ViewStack.getInstance().unlock();
        ViewStack.getInstance().pop();
    }
}
