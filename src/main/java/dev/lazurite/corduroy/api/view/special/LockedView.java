package dev.lazurite.corduroy.api.view.special;

import dev.lazurite.corduroy.api.ViewStack;

public interface LockedView {
    default void unlock() {
        ViewStack.getInstance().unlock();
    }
}
