package dev.lazurite.corduroy.impl.math;

import net.minecraft.util.math.Quaternion;

public class QuaternionHelper {
    /**
     * Lerp, but for spherical stuffs (hence Slerp).
     * @param q1 the first {@link Quaternion} to slerp
     * @param q2 the second {@link Quaternion} to slerp
     * @param t the delta time
     * @return the slerped {@link Quaternion}
     */
    public static Quaternion slerp(Quaternion q1, Quaternion q2, float t) {
        q1.normalize();
        q2.normalize();

        if (q1.getX() == q2.getX() && q1.getY() == q2.getY() && q1.getZ() == q2.getZ() && q1.getW() == q2.getW()) {
            return new Quaternion(q1);
        }

        float result = (q1.getX() * q2.getX()) + (q1.getY() * q2.getY()) + (q1.getZ() * q2.getZ()) + (q1.getW() * q2.getW());

        if (result < 0.0f) {
            q2.set(-q2.getX(), -q2.getY(), -q2.getZ(), -q2.getW());
            result = -result;
        }

        float scale0 = 1 - t;
        float scale1 = t;

        if ((1 - result) > 0.1f) {
            float theta = (float) Math.acos(result);
            float invSinTheta = 1f / (float) Math.sin(theta);

            scale0 = (float) Math.sin((1 - t) * theta) * invSinTheta;
            scale1 = (float) Math.sin((t * theta)) * invSinTheta;
        }

        Quaternion out = new Quaternion(
                (scale0 * q1.getX()) + (scale1 * q2.getX()),
                (scale0 * q1.getY()) + (scale1 * q2.getY()),
                (scale0 * q1.getZ()) + (scale1 * q2.getZ()),
                (scale0 * q1.getW()) + (scale1 * q2.getW()));
        out.normalize();
        return out;
    }
}
