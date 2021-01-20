package dev.lazurite.corduroy.impl;

import dev.lazurite.corduroy.api.view.View;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
public class ViewCamera extends Camera {
    private final View view;

    public ViewCamera(View view) {
        this.view = view;
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        double x = MathHelper.lerp(tickDelta, getView().getPreviousPosition().x, getView().getPosition().x);
        double y = MathHelper.lerp(tickDelta, getView().getPreviousPosition().y, getView().getPosition().y);
        double z = MathHelper.lerp(tickDelta, getView().getPreviousPosition().z, getView().getPosition().z);
        setPos(x, y, z);
    }

    public View getView() {
        return this.view;
    }

    @Override
    public Entity getFocusedEntity() {
        return MinecraftClient.getInstance().player;
    }
}
