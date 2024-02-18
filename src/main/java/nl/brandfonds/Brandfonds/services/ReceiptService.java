package nl.brandfonds.Brandfonds.services;

import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import nl.brandfonds.Brandfonds.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static nl.brandfonds.Brandfonds.utils.ImageManipulation.resizeAndCompressImage;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Value("${fileSaveLocation}")
    private String fileSaveLocation;

    public void saveReceiptFile(MultipartFile file, String description, LocalDate paidDate, Float paidAmount) throws IOException {

        String filename = file.getOriginalFilename();
        String fileType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
        String filePath = getReceiptFilePath(filename);

        receiptRepository.getByName(filename).ifPresent((foundName) -> {
            throw new AlreadyExistException.FileAlreadyExistException(foundName.getFileName());
        });

        BufferedImage bufferedImageResource = resizeAndCompressImage(file);
        java.io.File outputFile = new java.io.File(filePath);
        ImageIO.write(bufferedImageResource, fileType, outputFile);

        // cleanup temp file
        File tempToDeleteFile = new File("compressed_image." + fileType);
        tempToDeleteFile.delete();

        Receipt dbFile = new Receipt(filename, description, paidDate, paidAmount);
        receiptRepository.save(dbFile);
    }


    public void delete(Receipt receipt) {
        receiptRepository.delete(receipt);
    }

    public List<Receipt> getAll() {
        return receiptRepository.findAll();
    }

    public Receipt getReceiptByName(String name) {
        Optional<Receipt> dbReceipt = receiptRepository.getByName(name);

        dbReceipt.orElseThrow(() -> new NotFoundException.FileNotFoundException(fileSaveLocation, name));

        return dbReceipt.get();
    }

    public String getEncodedReceiptFileByName(String name) throws IOException {
        Optional<Receipt> dbReceipt = receiptRepository.getByName(name);

        dbReceipt.orElseThrow(() -> new NotFoundException.FileNotFoundException(fileSaveLocation, name));
        String filePath = getReceiptFilePath(dbReceipt.get().getFileName());

        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);

        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] getRawReceiptFileByName(String name) throws IOException {
        Optional<Receipt> dbReceipt = receiptRepository.getByName(name);

        dbReceipt.orElseThrow(() -> new NotFoundException.FileNotFoundException(fileSaveLocation, name));
        String filePath = getReceiptFilePath(dbReceipt.get().getFileName());

        Path path = Paths.get(filePath);

        return Files.readAllBytes(path);
    }

    private String getReceiptFilePath(String fileName) {
        var fileSeparator = System.getProperty("file.separator");
        var folderName = "receipts";

        return String.format("%s%s%s%s", fileSaveLocation, folderName, fileSeparator, fileName);
    }
}
