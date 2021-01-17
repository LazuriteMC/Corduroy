package dev.lazurite.corduroy.mixin.access;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccess {
    @Accessor("pitch")
    float getPitch();

    @Accessor("yaw")
    float getYaw();

    @Accessor("focusedEntity")
    Entity getFocusedEntity();
}
