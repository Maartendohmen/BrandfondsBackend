package nl.brandfonds.Brandfonds.utils;

import lombok.experimental.UtilityClass;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@UtilityClass
public class ImageManipulation {

    public static BufferedImage resizeAndCompressImage(MultipartFile file) throws IOException {
        String shortType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .outputFormat(shortType)
                .outputQuality(1)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return ImageIO.read(inputStream);
    }

    public static BufferedImage resizeAndCompressImage(MultipartFile file, int width, int height) throws IOException {
        String shortType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(width, height)
                .outputFormat(shortType)
                .outputQuality(1)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return ImageIO.read(inputStream);
    }
}
