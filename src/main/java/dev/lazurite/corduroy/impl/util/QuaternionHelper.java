package dev.lazurite.corduroy.impl.util;

import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class QuaternionHelper {
    public static Quaternion rotateX(Quaternion quat, double deg) {
        double radHalfAngle = Math.toRadians(deg) / 2.0;
        quat.hamiltonProduct(new Quaternion((float) Math.sin(radHalfAngle), 0, 0, (float) Math.cos(radHalfAngle)));
        return quat;
    }

    public static Quaternion rotateY(Quaternion quat, double deg) {
        double radHalfAngle = Math.toRadians(deg) / 2.0;
        quat.hamiltonProduct(new Quaternion(0, (float) Math.sin(radHalfAngle), 0, (float) Math.cos(radHalfAngle)));
        return quat;
    }

    public static Quaternion rotateZ(Quaternion quat, double deg) {
        double radHalfAngle = Math.toRadians(deg) / 2.0;
        quat.hamiltonProduct(new Quaternion(0, 0, (float) Math.sin(radHalfAngle), (float) Math.cos(radHalfAngle)));
        return quat;
    }

    public static Vec3f toEulerAngles(Quaternion quat) {
        Quaternion q = new Quaternion(Quaternion.IDENTITY);
        q.set(quat.getZ(), quat.getX(), quat.getY(), quat.getW());

        float x;
        float y;
        float z;

        // roll (x-axis rotation)
        double sinr_cosp = 2 * (q.getW() * q.getX() + q.getY() * q.getZ());
        double cosr_cosp = 1 - 2 * (q.getX() * q.getX() + q.getY() * q.getY());
        x = (float) Math.atan2(sinr_cosp, cosr_cosp);

        // pitch (y-axis rotation)
        double sinp = 2 * (q.getW() * q.getY() - q.getZ() * q.getX());
        if (Math.abs(sinp) >= 1) {
            y = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        } else {
            y = (float) Math.asin(sinp);
        }

        // yaw (z-axis rotation)
        double siny_cosp = 2 * (q.getW() * q.getZ() + q.getX() * q.getY());
        double cosy_cosp = 1 - 2 * (q.getY() * q.getY() + q.getZ() * q.getZ());
        z = (float) Math.atan2(siny_cosp, cosy_cosp);

        return new Vec3f(x, y, z);
    }

    public static float getYaw(Quaternion quat) {
        return -1 * (float) Math.toDegrees(QuaternionHelper.toEulerAngles(quat).getZ());
    }

    public static float getPitch(Quaternion quat) {
        return (float) Math.toDegrees(QuaternionHelper.toEulerAngles(quat).getY());
    }

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
