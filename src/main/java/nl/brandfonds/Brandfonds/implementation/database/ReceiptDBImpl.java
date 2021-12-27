package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IReceiptService;
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
import java.util.*;

import static nl.brandfonds.Brandfonds.utils.ImageManipulation.resizeAndCompressImage;

@Service
public class ReceiptDBImpl implements IReceiptService {

    @Value("${file.savelocation}")
    private String fileSaveLocation;

    @Autowired
    ReceiptRepository receiptRepository;

    @Override
    public void saveReceiptFile(MultipartFile file, String description, Date paidDate, Long paidAmount) throws IOException, AlreadyExistException {

        String filename = file.getOriginalFilename();
        String fileType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
        String filePath = getReceiptFilePath(filename);

        if (receiptRepository.getByName(filename).isPresent()) {
            throw new AlreadyExistException("Er bestaat al een bestand met dezelfde naam op de server");
        }

        BufferedImage bufferedImageResource = resizeAndCompressImage(file);
        java.io.File outputFile = new java.io.File(filePath);
        ImageIO.write(bufferedImageResource, fileType, outputFile);

        // cleanup temp file
        File tempToDeleteFile = new File("compressed_image." + fileType);
        tempToDeleteFile.delete();

        Receipt dbFile = new Receipt(filename, description, paidDate, paidAmount);
        receiptRepository.save(dbFile);
    }

    @Override
    public void delete(Receipt receipt) {
        receiptRepository.delete(receipt);
    }

    @Override
    public List<Receipt> getAll() {
        return receiptRepository.findAll();
    }

    @Override
    public Receipt getReceiptByName(String name) throws NotFoundException {
        Optional<Receipt> dbReceipt = receiptRepository.getByName(name);

        if (!dbReceipt.isPresent()) {
            throw new NotFoundException("De opgevraagde afbeelding kan niet gevonden worden");
        }

        return dbReceipt.get();
    }

    @Override
    public String getEncodedReceiptFileByName(String name) throws NotFoundException, IOException {
        Optional<Receipt> dbReceipt = receiptRepository.getByName(name);

        if (!dbReceipt.isPresent()) {
            throw new NotFoundException("De opgevraagde afbeelding kan niet gevonden worden");
        }

        String filePath = getReceiptFilePath(dbReceipt.get().getFilename());

        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);

        return Base64.getEncoder().encodeToString(data);
    }

    @Override
    public byte[] getRawReceiptFileByName(String name) throws NotFoundException, IOException {
        Optional<Receipt> dbReceipt = receiptRepository.getByName(name);

        if (!dbReceipt.isPresent()) {
            throw new NotFoundException("De opgevraagde afbeelding kan niet gevonden worden");
        }

        String filePath = getReceiptFilePath(dbReceipt.get().getFilename());

        Path path = Paths.get(filePath);

        return Files.readAllBytes(path);
    }

    private String getReceiptFilePath(String fileName) {
        var fileSeparator = System.getProperty("file.separator");
        var folderName = "receipts";

        return String.format("%s%s%s%s", fileSaveLocation, folderName, fileSeparator, fileName);
    }

}
