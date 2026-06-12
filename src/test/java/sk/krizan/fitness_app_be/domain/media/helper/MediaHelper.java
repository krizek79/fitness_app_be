package sk.krizan.fitness_app_be.domain.media.helper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MediaHelper {

    public static MockMultipartFile createMockImage(String contentType, int sizeInBytes) {
        return new MockMultipartFile(
                "file",
                "test-image.jpg",
                contentType,
                new byte[sizeInBytes]
        );
    }

    public static MockMultipartFile createEmptyFile() {
        return new MockMultipartFile("file", new byte[0]);
    }

}
