package dev.lazurite.corduroy.api.view;

import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public interface FreeView extends View {
    void setPosition(Vec3d position);
    void setOrientation(Quaternion orientation);
}
