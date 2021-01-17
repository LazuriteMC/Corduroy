package dev.lazurite.corduroy.impl.camera;

import dev.lazurite.corduroy.impl.entity.DummyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class StationaryCamera extends BetterCamera {
    private final DummyEntity dummyEntity;

    public StationaryCamera(Vec3d position, Quaternion rotation) {
        super();
        this.dummyEntity = new DummyEntity(client.world);
        this.setPos(position);
        this.getRotation().set(rotation.getX() ,rotation.getY(), rotation.getZ(), rotation.getW());
        this.setShouldRenderHand(false);
        this.setShouldRenderPlayer(true);
    }

    @Override
    public Entity getFocusedEntity() {
        return this.dummyEntity;
    }
}
