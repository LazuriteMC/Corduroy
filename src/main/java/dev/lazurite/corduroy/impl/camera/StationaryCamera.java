package dev.lazurite.corduroy.impl.camera;

import dev.lazurite.corduroy.impl.entity.DummyEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

public class StationaryCamera extends Camera {
    private final MinecraftClient client;
    private final DummyEntity dummyEntity;

    public StationaryCamera(Vec3d position, Quaternion rotation) {
        super();
        this.client = MinecraftClient.getInstance();
        this.dummyEntity = new DummyEntity(client.world);
        this.setPos(position);
        this.getRotation().set(rotation.getX() ,rotation.getY(), rotation.getZ(), rotation.getW());
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {

    }

    @Override
    public Entity getFocusedEntity() {
        return this.dummyEntity;
    }
}
