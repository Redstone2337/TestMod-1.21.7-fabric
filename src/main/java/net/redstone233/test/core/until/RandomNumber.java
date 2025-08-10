package net.redstone233.test.core.until;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 轻量级随机数工具库。
 * 对外只暴露静态方法，任何地方都可以：
 *    int    i = RandomKit.nextInt(10, 99);
 *    float  f = RandomKit.nextFloat(0f, 1f);
 *    double d = RandomKit.nextDouble(-5.5, 5.5);
 */
public final class RandomNumber {

    /* ---------- 整数 ---------- */

    /** 返回 [min, max] 之间的随机整数，两端都包含 */
    public static int nextInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must <= max");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /** 返回 [0, max] 之间的随机整数 */
    public static int nextInt(int max) {
        return nextInt(0, max);
    }

    /* ---------- 单精度浮点 ---------- */

    /** 返回 [min, max) 之间的随机 float（左闭右开） */
    public static float nextFloat(float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must < max");
        }
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }

    /** 返回 [0, max) 之间的随机 float */
    public static float nextFloat(float max) {
        return nextFloat(0f, max);
    }

    /* ---------- 双精度浮点 ---------- */

    /** 返回 [min, max) 之间的随机 double（左闭右开）*/
    public static double nextDouble(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must < max");
        }
        return min + ThreadLocalRandom.current().nextDouble() * (max - min);
    }

    /** 返回 [0, max) 之间的随机 double */
    public static double nextDouble(double max) {
        return nextDouble(0.0, max);
    }

    /* ---------- 工具类禁止实例化 ---------- */
    private RandomNumber() {
        throw new AssertionError("No RandomKit instances for you!");
    }
}