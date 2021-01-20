package dev.lazurite.corduroy.api.camera;

import dev.lazurite.corduroy.api.ViewStack;

public interface Temporary {
    default void finish() {
        ViewStack.getInstance().pop();
    }

    void setLifeSpan(int ticks);
    int getLifeSpan();
    void setAge(int age);
    int getAge();
}
