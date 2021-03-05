package dev.lazurite.corduroy.api.view.type.special;

import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.mixin.ClientWorldMixin;

/**
 * Any {@link View} that implements this interface
 * will be ticked every world tick.
 *
 * @since 1.0.0
 * @see ClientWorldMixin
 */
public interface TickingView {
    void tick();
}
