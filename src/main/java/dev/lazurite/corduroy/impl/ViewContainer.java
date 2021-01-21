package dev.lazurite.corduroy.impl;

import dev.lazurite.corduroy.api.view.View;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
public class ViewContainer extends Camera {
    private final View view;
    private Vec3d prevPosition;
    private Quaternion prevOrientation;

    public ViewContainer(View view) {
        this.view = view;
        this.prevPosition = view.getPosition();
        this.prevOrientation = view.getOrientation();
    }

    public void tick() {
        this.prevOrientation = view.getOrientation();
        this.prevPosition = view.getPosition();
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        double x = MathHelper.lerp(tickDelta, prevPosition.x, getView().getPosition().x);
        double y = MathHelper.lerp(tickDelta, prevPosition.y, getView().getPosition().y);
        double z = MathHelper.lerp(tickDelta, prevPosition.z, getView().getPosition().z);
        setPos(x, y, z);
    }

    public View getView() {
        return this.view;
    }

    public Quaternion getPrevOrientation() {
        return prevOrientation;
    }

    @Override
    public Entity getFocusedEntity() {
        return MinecraftClient.getInstance().player;
    }
}
