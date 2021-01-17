package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.impl.storage.CameraStorage;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Camera.class)
public class CameraMixin implements CameraStorage {
    @Unique private boolean shouldRenderPlayer;

    @Unique @Override
    public void setShouldRenderPlayer(boolean shouldRenderPlayer) {
        this.shouldRenderPlayer = shouldRenderPlayer;
    }

    @Unique @Override
    public boolean shouldRenderPlayer() {
        return this.shouldRenderPlayer;
    }
}
