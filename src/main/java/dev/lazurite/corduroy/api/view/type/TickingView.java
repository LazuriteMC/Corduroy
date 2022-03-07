package dev.lazurite.corduroy.api.view.type;

import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.mixin.ClientLevelMixin;

/**
 * Any {@link View} that implements this interface
 * will be ticked every world tick.
 *
 * @since 1.0.0
 * @see ClientLevelMixin
 */
public interface TickingView extends View {
    void tick();
}
