package dev.lazurite.corduroy.impl;

import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.util.math.QuaternionHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
public class ViewContainer extends Camera {
    private final View view;
    public Vec3d prevPosition;
    public Quaternion prevOrientation;

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

    @Override
    public Entity getFocusedEntity() {
        return new CowEntity(EntityType.COW, MinecraftClient.getInstance().world);
    }

    @Override
    public Quaternion getRotation() {
        return getView().getOrientation();
    }

    @Override
    public float getYaw() {
        return QuaternionHelper.getYaw(getView().getOrientation());
    }

    @Override
    public float getPitch() {
        return QuaternionHelper.getPitch(getView().getOrientation());
    }
}
