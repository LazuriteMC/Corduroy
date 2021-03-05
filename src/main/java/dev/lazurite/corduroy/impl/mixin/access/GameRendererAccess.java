package dev.lazurite.corduroy.impl.mixin.access;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
@Environment(EnvType.CLIENT)
public interface GameRendererAccess {
    @Accessor("camera")
    void setCamera(Camera camera);

    @Accessor("renderHand")
    void setRenderHand(boolean renderHand);

    @Accessor("renderHand")
    boolean getRenderHand();
}
