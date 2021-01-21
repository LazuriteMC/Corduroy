package dev.lazurite.corduroy.api.transition;

public interface Transition {
    void render();
    void tick();
    int getAge();
}
