package dev.lazurite.corduroy.impl;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.View;
import net.minecraft.client.Minecraft;

import java.util.Optional;
import java.util.Stack;

/**
 * @see ViewStack
 */
public class ViewStackImpl implements ViewStack {

    public final static ViewStackImpl INSTANCE = new ViewStackImpl();

    private final Stack<View> stack = new Stack<>();
    private boolean lock;

    @Override
    public boolean isLocked() {
        return this.lock;
    }

    @Override
    public void unlock() {
        this.lock = false;
    }

    @Override
    public void push(View view) {
        if (this.isLocked()) return;

        this.handleCorduroyCamera(false);
        this.stack.push(view);

        if (view instanceof View.Locked) {
            this.lock = true;
        }
    }

    @Override
    public Optional<View> pop() {
        if (this.isLocked()) {
            return Optional.empty();
        }

        this.handleCorduroyCamera(true);

        return this.stack.size() > 0 ?
                Optional.of(this.stack.pop()) :
                Optional.empty();
    }

    @Override
    public Optional<View> peek() {
        return this.stack.size() > 0 ?
                Optional.of(this.stack.peek()) :
                Optional.empty();
    }

    @Override
    public Optional<View> find(View view) {
        return this.stack.stream()
                .filter(viewOnStack -> viewOnStack.equals(view))
                .findAny();
    }

    public void clear() {
        this.stack.clear();
    }

    private void handleCorduroyCamera(boolean pop) {
        var camera = Minecraft.getInstance().gameRenderer.mainCamera;

        if (pop && this.stack.size() == 1) {
            Minecraft.getInstance().gameRenderer.mainCamera = ((CorduroyCamera) camera).getParent();
        }

        if (!pop && !(camera instanceof CorduroyCamera)) {
            Minecraft.getInstance().gameRenderer.mainCamera = new CorduroyCamera(camera);
        }
    }

}
