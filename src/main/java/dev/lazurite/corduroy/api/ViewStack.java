package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.impl.ViewStackImpl;
import net.minecraft.client.Camera;

import java.util.Optional;

/**
 * This is the main stack on which {@link View} objects can be pushed.
 * @since 1.0.0
 * @see View
 */
public interface ViewStack {

    static ViewStack getInstance() {
        return ViewStackImpl.INSTANCE;
    }

    /**
     * Returns whether the current {@link View} is a {@link View.Locked} locked to the stack.
     */
    boolean isLocked();

    /**
     * Unlocks the {@link ViewStack} after a {@link View.Locked} was pushed.
     */
    void unlock();

    /**
     * Pushes the given {@link View} onto the stack.
     */
    void push(View view);

    /**
     * Pops the current {@link View} from the stack.
     * Returns an empty {@link Optional} if the stack is empty.
     */
    Optional<View> pop();

    /**
     * Returns the {@link View} currently at the top of the stack.
     * Returns an empty {@link Optional} if the stack is empty.
     */
    Optional<View> peek();

    /**
     * Finds a specific {@link View} within the stack.
     * Returns an empty {@link Optional} if the stack is empty.
     */
    Optional<View> find(View view);

    /**
     * Removes every {@link View} from the stack and returns the camera to the original {@link Camera} object.
     */
    void clear();

}
