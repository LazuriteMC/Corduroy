package dev.lazurite.corduroy.api.view.type;

import net.minecraft.entity.Entity;

public interface SubjectView<E extends Entity> {
    E getSubject();
}
