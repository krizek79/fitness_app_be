package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomHelper {

    public static int getRandomInt(int min, int max) {
        if (min > max) throw new IllegalArgumentException("min must be <= max");
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double getRandomDouble(double min, double max) {
        if (min >= max) throw new IllegalArgumentException("min must be < max");
        return ThreadLocalRandom.current().nextDouble(min, max);
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
