package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IReceiptService;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.files.FileType;
import nl.brandfonds.Brandfonds.model.Receipt;
import nl.brandfonds.Brandfonds.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ReceiptDBImpl implements IReceiptService {

    @Value("${file.savelocation}")
    private String fileSaveLocation;

    @Autowired
    ReceiptRepository receiptRepository;

    @Override
    public void saveFile(MultipartFile file, String description, Date paidDate, Long paidAmount) throws IOException, AlreadyExistException {

        String filename = file.getOriginalFilename();
        String fileType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
        String filePath = fileSaveLocation + "receipts" + System.getProperty("file.separator") + filename;

        if (receiptRepository.getByName(filename).isPresent()) {
            throw new AlreadyExistException("Er bestaat al een bestand met dezelfde naam op de server");
        }

        BufferedImage bufferedImageResource = resizeAndCompressImage(file);
        java.io.File outputFile = new java.io.File(filePath);
        ImageIO.write(bufferedImageResource,fileType, outputFile);

        // cleanup temp file
        File tempToDeleteFile = new File("compressed_image." + fileType);
        tempToDeleteFile.delete();

        Receipt dbFile = new Receipt(filename, FileType.valueOf(fileType.toUpperCase()), description, paidDate,paidAmount);
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

        if (!dbReceipt.isPresent()){
            throw new NotFoundException("De opgevraagde afbeelding kan niet gevonden worden");
        }

        return dbReceipt.get();
    }

    @Override
    public String getEncodedReceiptFileByName(String name) throws NotFoundException, IOException {
        Optional<Receipt> dbReceipt = receiptRepository.getByName(name);

        if (!dbReceipt.isPresent()){
            throw new NotFoundException("De opgevraagde afbeelding kan niet gevonden worden");
        }

        String filePath = fileSaveLocation + "receipts" + System.getProperty("file.separator") + dbReceipt.get().getFilename();

        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);

        return Base64.getEncoder().encodeToString(data);
    }

    @Override
    public byte[] getRawReceiptFileByName(String name) throws NotFoundException, IOException {
        Optional<Receipt> dbReceipt = receiptRepository.getByName(name);

        if (!dbReceipt.isPresent()){
            throw new NotFoundException("De opgevraagde afbeelding kan niet gevonden worden");
        }

        String filePath = fileSaveLocation + "receipts" + System.getProperty("file.separator") + dbReceipt.get().getFilename();

        Path path = Paths.get(filePath);

        return Files.readAllBytes(path);
    }

    private BufferedImage resizeAndCompressImage(MultipartFile file) throws IOException {
        String shortType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
        BufferedImage input = ImageIO.read(file.getInputStream());

        java.io.File compressedImageFile = new java.io.File("compressed_image." + shortType);
        OutputStream os = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(shortType);
        ImageWriter writer = writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.5f);  // Change the quality value you prefer
        writer.write(null, new IIOImage(input, null, null), param);

        os.close();
        ios.close();
        writer.dispose();

        return ImageIO.read(compressedImageFile);
    }

}
