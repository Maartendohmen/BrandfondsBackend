package nl.brandfonds.Brandfonds.implementation.database;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.UserRole;
import nl.brandfonds.Brandfonds.repository.UserRepository;
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
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static nl.brandfonds.Brandfonds.utils.ImageManipulation.resizeAndCompressImage;

@Service
@Slf4j
public class UserDBImpl implements IUserService {

    @Value("${file.savelocation}")
    private String fileSaveLocation;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();

        users.removeIf(value -> value.getUserRole() == UserRole.BRANDMASTER);

        return users;
    }

    @Override
    public Optional<User> getByID(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
        log.info("A new user with forname {} and surname {} was created", user.getForname(), user.getSurname());
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
        log.info("A new user with forname {} and surname {} was deleted", user.getForname(), user.getSurname());
    }

    @Override
    public Optional<User> getByMail(String mail) {
        return userRepository.getByMail(mail);
    }

    @Override
    public Optional<User> getByName(String name) {
        return userRepository.getByName(name);
    }

    @Override
    public long getUserSaldo(Integer id) {
        return userRepository.getUserSaldo(id);
    }

    @Override
    public void setUserSaldo(Long amount, Integer id) {
        userRepository.setUserSaldo(amount, id);
    }

    @Override
    public void saveProfilePicture(User user, MultipartFile file) throws IOException {
        String fileType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
        String filename = String.format("%s.%s", user.getId(),fileType);
        String filePath = getProfilePictureFilePath(filename);

        BufferedImage bufferedImageResource = resizeAndCompressImage(file,500,500);
        java.io.File outputFile = new java.io.File(filePath);
        ImageIO.write(bufferedImageResource, fileType, outputFile);

        // cleanup temp file
        File tempToDeleteFile = new File("compressed_image." + fileType);
        tempToDeleteFile.delete();

        user.setProfilePictureFileName(filename);
        userRepository.saveAndFlush(user);
    }

    @Override
    public String getEncodedProfilePicture(User user) throws NotFoundException, IOException {

        if (user.getProfilePictureFileName() == null || user.getProfilePictureFileName().isEmpty()) {
            throw new NotFoundException("De opgevraagde afbeelding kan niet gevonden worden");
        }

        Path path = Paths.get(getProfilePictureFilePath(user.getProfilePictureFileName()));
        byte[] data = Files.readAllBytes(path);

        return Base64.getEncoder().encodeToString(data);
    }

    @Override
    public void updatePassword(String newpassword, String emailadres) {
        userRepository.updatePassword(newpassword, emailadres);
    }

    private String getProfilePictureFilePath(String filename) {
        var fileSeparator = System.getProperty("file.separator");
        var folderName = "profile_pictures";

        return String.format("%s%s%s%s", fileSaveLocation, folderName, fileSeparator, filename);
    }


}
