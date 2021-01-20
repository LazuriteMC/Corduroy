package dev.lazurite.corduroy.api.camera;

import dev.lazurite.corduroy.api.ViewStack;

public interface Locked {
    default void unlock() {
        ViewStack.getInstance().unlock();
    }
}
