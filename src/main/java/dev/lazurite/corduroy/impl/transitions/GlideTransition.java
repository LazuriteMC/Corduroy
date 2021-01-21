package dev.lazurite.corduroy.impl.transitions;

import dev.lazurite.corduroy.api.transition.Transition;
import dev.lazurite.corduroy.api.view.View;

public class GlideTransition implements Transition {
    private final View v1;
    private final View v2;
    private int age;

    public GlideTransition(View v1, View v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public void render() {

    }

    @Override
    public void tick() {
        ++age;


    }

    @Override
    public int getAge() {
        return this.age;
    }
}
