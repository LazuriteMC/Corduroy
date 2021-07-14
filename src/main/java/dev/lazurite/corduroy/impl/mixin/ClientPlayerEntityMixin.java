package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.view.type.SubjectView;
import dev.lazurite.corduroy.impl.ViewContainer;
import dev.lazurite.toolbox.render.Viewable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements Viewable {
    @Override
    public boolean shouldRenderPlayer() {
        return MinecraftClient.getInstance().gameRenderer.camera instanceof ViewContainer container &&
                container.getView().shouldRenderPlayer();
    }

    @Override
    public boolean shouldRenderSelf() {
        return MinecraftClient.getInstance().gameRenderer.camera instanceof ViewContainer container &&
                container.getView() instanceof SubjectView subjectView &&
                subjectView.shouldRenderSubject();
    }
}
