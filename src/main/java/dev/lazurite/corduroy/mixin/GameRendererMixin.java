package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
@Environment(EnvType.CLIENT)
public class GameRendererMixin {
    @Shadow @Final private Camera camera;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftClient client, ResourceManager resourceManager, BufferBuilderStorage bufferBuilderStorage, CallbackInfo info) {
        ViewStack.create(client, camera);
    }
}
