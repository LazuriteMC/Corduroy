package dev.lazurite.corduroy.api.view.special;

import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.mixin.ClientWorldMixin;

/**
 * Any {@link View} that implements this interface
 * will be ticked every world tick.
 * @see ClientWorldMixin
 */
public interface TickingView {
    void tick();
}
