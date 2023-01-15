package dev.lazurite.corduroy.impl.util;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Some handy quaternion functions.
 */
public class QuaternionUtil {

    public static float getYaw(Quaternionf quat) {
        return -1 * (float) Math.toDegrees(toEulerAngles(quat).z());
    }

    public static float getPitch(Quaternionf quat) {
        return (float) Math.toDegrees(toEulerAngles(quat).y());
    }

    public static Quaternionf slerp(Quaternionf q1, Quaternionf q2, float t) {
        q1.normalize();
        q2.normalize();

        if (q1.x() == q2.x() && q1.y() == q2.y() && q1.z() == q2.z() && q1.w() == q2.w()) {
            return new Quaternionf(q1.x(), q1.y(), q1.z(), q1.w());
        }

        var result = (q1.x() * q2.x()) + (q1.y() * q2.y()) + (q1.z() * q2.z()) + (q1.w() * q2.w());

        if (result < 0.0f) {
            q2.set(-q2.x(), -q2.y(), -q2.z(), -q2.w());
            result = -result;
        }

        var scale0 = 1 - t;
        var scale1 = t;

        if ((1 - result) > 0.1f) {
            final var theta = (float) Math.acos(result);
            final var invSinTheta = 1f / (float) Math.sin(theta);

            scale0 = (float) Math.sin((1 - t) * theta) * invSinTheta;
            scale1 = (float) Math.sin((t * theta)) * invSinTheta;
        }

        final var out = new Quaternionf(
                (scale0 * q1.x()) + (scale1 * q2.x()),
                (scale0 * q1.y()) + (scale1 * q2.y()),
                (scale0 * q1.z()) + (scale1 * q2.z()),
                (scale0 * q1.w()) + (scale1 * q2.w()));

        out.normalize();
        return out;
    }

    public static Vector3f toEulerAngles(Quaternionf quat) {
        final var q = new Quaternionf(0, 0, 0, 1);
        q.set(quat.x(), quat.y(), quat.z(), quat.w());

        var i = 0.0f;
        var j = 0.0f;
        var k = 0.0f;

        // roll (x-axis rotation)
        final var sinr_cosp = 2 * (q.w() * q.x() + q.y() * q.z());
        final var cosr_cosp = 1 - 2 * (q.x() * q.x() + q.y() * q.y());
        i = (float) Math.atan2(sinr_cosp, cosr_cosp);

        // pitch (y-axis rotation)
        final var sinp = 2 * (q.w() * q.y() - q.z() * q.x());
        if (Math.abs(sinp) >= 1) j = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        else j = (float) Math.asin(sinp);

        // yaw (z-axis rotation)
        final var siny_cosp = 2 * (q.w() * q.z() + q.x() * q.y());
        final var cosy_cosp = 1 - 2 * (q.y() * q.y() + q.z() * q.z());
        k = (float) Math.atan2(siny_cosp, cosy_cosp);

        return new Vector3f(i, j ,k);
    }

}
