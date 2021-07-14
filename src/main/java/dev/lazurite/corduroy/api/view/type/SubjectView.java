package dev.lazurite.corduroy.api.view.type;

import dev.lazurite.corduroy.api.view.View;
import net.minecraft.entity.Entity;

/**
 * A view that is tethered to a "subject" {@link Entity}.
 *
 * @since 1.0.0
 * @see View
 */
public interface SubjectView extends View {
    Entity getSubject();
    boolean shouldRenderSubject();
}
