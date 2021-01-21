package dev.lazurite.corduroy.api.view;

import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public interface View {
    Vec3d getPosition();
    Quaternion getOrientation();

    boolean shouldRenderHand();
    boolean shouldRenderPlayer();
    boolean shouldRenderHud();
}
