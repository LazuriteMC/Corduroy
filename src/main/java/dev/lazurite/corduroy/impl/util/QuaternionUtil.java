package dev.lazurite.corduroy.impl.util;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

public class QuaternionUtil {
    public static float getYaw(Quaternion quat) {
        return -1 * (float) Math.toDegrees(toEulerAngles(quat).z());
    }

    public static float getPitch(Quaternion quat) {
        return (float) Math.toDegrees(toEulerAngles(quat).y());
    }

    public static float getRoll(Quaternion quat) {
        return (float) Math.toDegrees(toEulerAngles(quat).x());
    }

    public static Quaternion slerp(Quaternion q1, Quaternion q2, float t) {
        q1.normalize();
        q2.normalize();

        if (q1.i() == q2.i() && q1.j() == q2.j() && q1.k() == q2.k() && q1.r() == q2.r()) {
            return new Quaternion(q1.i(), q1.j(), q1.k(), q1.r());
        }

        var result = (q1.i() * q2.i()) + (q1.j() * q2.j()) + (q1.k() * q2.k()) + (q1.r() * q2.r());

        if (result < 0.0f) {
            q2.set(-q2.i(), -q2.j(), -q2.k(), -q2.r());
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

        final var out = new Quaternion(
                (scale0 * q1.i()) + (scale1 * q2.i()),
                (scale0 * q1.j()) + (scale1 * q2.j()),
                (scale0 * q1.k()) + (scale1 * q2.k()),
                (scale0 * q1.r()) + (scale1 * q2.r()));

        out.normalize();
        return out;
    }

    public static Vector3f toEulerAngles(Quaternion quat) {
        final var q = new Quaternion(Quaternion.ONE);
        q.set(quat.i(), quat.j(), quat.k(), quat.r());

        var i = 0.0f;
        var j = 0.0f;
        var k = 0.0f;

        // roll (x-axis rotation)
        final var sinr_cosp = 2 * (q.r() * q.i() + q.j() * q.k());
        final var cosr_cosp = 1 - 2 * (q.i() * q.i() + q.j() * q.j());
        i = (float) Math.atan2(sinr_cosp, cosr_cosp);

        // pitch (y-axis rotation)
        final var sinp = 2 * (q.r() * q.j() - q.k() * q.i());
        if (Math.abs(sinp) >= 1) j = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        else j = (float) Math.asin(sinp);

        // yaw (z-axis rotation)
        final var siny_cosp = 2 * (q.r() * q.k() + q.i() * q.j());
        final var cosy_cosp = 1 - 2 * (q.j() * q.j() + q.k() * q.k());
        k = (float) Math.atan2(siny_cosp, cosy_cosp);

        return new Vector3f(i, j ,k);
    }
}
