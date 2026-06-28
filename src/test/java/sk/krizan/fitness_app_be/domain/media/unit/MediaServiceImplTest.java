package sk.krizan.fitness_app_be.domain.media.unit;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.media.MediaServiceImpl;
import sk.krizan.fitness_app_be.domain.media.helper.MediaHelper;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaServiceImplTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private MediaServiceImpl mediaService;

    private final int MAX_SIZE_MB = 2;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mediaService, "imageMaxSize", MAX_SIZE_MB);
        ReflectionTestUtils.setField(mediaService, "basePath", "local");
        ReflectionTestUtils.setField(mediaService, "subPath", "");
    }

    @Test
    void uploadFile_Success() throws IOException {
        MultipartFile file = MediaHelper.createMockImage("image/jpeg", 1024);
        String expectedUrl = "https://cloudinary.com/image.jpg";

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap()))
                .thenReturn(Map.of("secure_url", expectedUrl));

        String result = mediaService.uploadFile(file, "folder");

        assertThat(result).isEqualTo(expectedUrl);
        verify(uploader, times(1)).upload(any(byte[].class), anyMap());
    }

    @Test
    @DisplayName("Should throw BAD_REQUEST when file is empty")
    void uploadFile_EmptyFile_ThrowsException() {
        MultipartFile file = MediaHelper.createEmptyFile();

        assertThatThrownBy(() -> mediaService.uploadFile(file, "folder"))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue(ApplicationException.Fields.httpStatus, HttpStatus.BAD_REQUEST)
                .hasMessage("File is empty");

        verifyNoInteractions(cloudinary);
    }

    @Test
    void uploadFile_InvalidFormat_ThrowsException() {
        MultipartFile file = MediaHelper.createMockImage("application/pdf", 100);

        assertThatThrownBy(() -> mediaService.uploadFile(file, "folder"))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue(ApplicationException.Fields.httpStatus, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .hasMessage("Only JPEG and PNG images are allowed");

        verifyNoInteractions(cloudinary);
    }

    @Test
    void uploadFile_TooLarge_ThrowsException() {
        int threeMb = (MAX_SIZE_MB + 1) * 1024 * 1024;
        MultipartFile file = MediaHelper.createMockImage("image/png", threeMb);

        assertThatThrownBy(() -> mediaService.uploadFile(file, "folder"))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue(ApplicationException.Fields.httpStatus, HttpStatus.PAYLOAD_TOO_LARGE)
                .hasMessage("File size must be under " + MAX_SIZE_MB + " MB");

        verifyNoInteractions(cloudinary);
    }

    @Test
    void uploadFile_CloudinaryError_ThrowsException() throws IOException {
        MultipartFile file = MediaHelper.createMockImage("image/jpeg", 100);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("Disk error"));

        assertThatThrownBy(() -> mediaService.uploadFile(file, "folder"))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue(ApplicationException.Fields.httpStatus, HttpStatus.INTERNAL_SERVER_ERROR)
                .hasMessage("Upload failed");
    }

    @Test
    void deleteFile_Success() throws IOException {
        String url = "https://res.cloudinary.com/demo/image/upload/local/exercise/42/image.jpg";

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(any(String.class), anyMap())).thenReturn(Map.of("result", "ok"));

        mediaService.deleteFile(url);

        verify(uploader, times(1)).destroy(org.mockito.ArgumentMatchers.eq("local/exercise/42/image"), anyMap());
    }

    @Test
    void deleteFile_WithVersionSegment_Success() throws IOException {
        String url = "https://res.cloudinary.com/demo/image/upload/v1234567890/local/exercise/42/image.jpg";

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(any(String.class), anyMap())).thenReturn(Map.of("result", "ok"));

        mediaService.deleteFile(url);

        verify(uploader, times(1)).destroy(org.mockito.ArgumentMatchers.eq("local/exercise/42/image"), anyMap());
    }

    @Test
    void deleteFile_InvalidUrl_ThrowsException() {
        String invalidUrl = "https://example.com/not-a-cloudinary-url/image.jpg";

        assertThatThrownBy(() -> mediaService.deleteFile(invalidUrl))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue(ApplicationException.Fields.httpStatus, HttpStatus.BAD_REQUEST)
                .hasMessage("Invalid Cloudinary URL");

        verifyNoInteractions(cloudinary);
    }

    @Test
    void deleteFile_CloudinaryError_ThrowsException() throws IOException {
        String url = "https://res.cloudinary.com/demo/image/upload/local/exercise/42/image.jpg";

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(any(String.class), anyMap())).thenThrow(new IOException("Network error"));

        assertThatThrownBy(() -> mediaService.deleteFile(url))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue(ApplicationException.Fields.httpStatus, HttpStatus.INTERNAL_SERVER_ERROR)
                .hasMessage("Delete failed");
    }

}