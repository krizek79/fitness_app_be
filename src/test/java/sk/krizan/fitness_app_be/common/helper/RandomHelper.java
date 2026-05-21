package sk.krizan.fitness_app_be.common.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomHelper {

    public static int getRandomInt(int min, int max) {
        if (min > max) throw new IllegalArgumentException("min must be <= max");
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double getRandomDouble(double min, double max) {
        if (min >= max) throw new IllegalArgumentException("min must be < max");
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static BigDecimal getRandomBigDecimal(
            BigDecimal min,
            BigDecimal max,
            int scale
    ) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("min and max must not be null");
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be <= max");
        }

        if (scale < 0) {
            throw new IllegalArgumentException("scale must be >= 0");
        }

        BigDecimal scaledMin = min.setScale(scale, RoundingMode.CEILING);
        BigDecimal scaledMax = max.setScale(scale, RoundingMode.FLOOR);

        BigDecimal range = scaledMax.subtract(scaledMin);

        if (range.signum() < 0) {
            throw new IllegalArgumentException("No values possible in given range for the specified scale");
        }

        BigDecimal randomFraction = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble());
        BigDecimal randomValue = scaledMin.add(range.multiply(randomFraction));

        return randomValue.setScale(scale, RoundingMode.DOWN);
    }

    public static Duration getRandomDuration(Duration min, Duration max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }

        long minSeconds = min.getSeconds();
        long maxSeconds = max.getSeconds();
        long randomSeconds = ThreadLocalRandom.current().nextLong(minSeconds, maxSeconds + 1);

        int minNanos = min.getNano();
        int maxNanos = max.getNano();
        int randomNanos = ThreadLocalRandom.current().nextInt(0, 1_000_000_000);

        if (minSeconds == maxSeconds) {
            randomNanos = ThreadLocalRandom.current().nextInt(minNanos, maxNanos + 1);
        }

        return Duration.ofSeconds(randomSeconds).plusNanos(randomNanos);
    }
}
