package dev.lazurite.corduroy.api.view.special;

import dev.lazurite.corduroy.api.ViewStack;

public interface TemporaryView {
    default void finish() {
        ViewStack.getInstance().pop();
    }

    int getLifeSpan();
    void setAge(int age);
    int getAge();
}
