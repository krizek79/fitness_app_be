package sk.krizan.fitness_app_be.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicIdGenerator {

    private static final String ALPHABET = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SEGMENT_LENGTH = 4;

    public static String generate() {
        StringBuilder builder = new StringBuilder(2 * SEGMENT_LENGTH + 1);
        appendSegment(builder);
        builder.append('-');
        appendSegment(builder);
        return builder.toString();
    }

    private static void appendSegment(StringBuilder builder) {
        for (int i = 0; i < SEGMENT_LENGTH; i++) {
            builder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
    }

}
