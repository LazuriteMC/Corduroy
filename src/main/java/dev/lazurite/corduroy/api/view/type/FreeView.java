package dev.lazurite.corduroy.api.view.type;

import dev.lazurite.corduroy.api.view.View;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public interface FreeView extends View {
    void setPosition(Vec3d position);
    void setOrientation(Quaternion orientation);
}
