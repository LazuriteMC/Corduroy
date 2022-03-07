package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.api.view.type.LockedView;

import java.util.Optional;
import java.util.Stack;

/**
 * This is the main stack on which {@link View} objects can be pushed.
 * @since 1.0.0
 * @see View
 */
public final class ViewStack {
    private final static ViewStack instance = new ViewStack();
    private final Stack<View> stack = new Stack<>();
    private boolean lock;

    public static ViewStack getInstance() {
        return instance;
    }

    public void lock() {
        this.lock = true;
    }

    public void unlock() {
        this.lock = false;
    }

    public boolean isLocked() {
        return this.lock;
    }

    public void push(View view) {
        if (isLocked()) {
            return;
        }

        stack.push(view);

        if (view instanceof LockedView) {
            this.lock();
        }
    }

    public Optional<View> pop() {
        if (stack.size() > 0 && !isLocked()) { // there's at least one container present
            final var view = stack.pop();
            return Optional.of(view);
        }

        return Optional.empty();
    }

    public Optional<View> peek() {
        if (stack.size() > 0) {
            return Optional.of(stack.peek());
        }

        return Optional.empty();
    }

    public View find(View viewToFind) {
        for (var view : stack) {
            if (view.equals(viewToFind)) {
                return view;
            }
        }

        return null;
    }

    public void clear() {
        stack.clear();
    }
}
