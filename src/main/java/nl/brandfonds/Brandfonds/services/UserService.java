package nl.brandfonds.Brandfonds.services;

import lombok.RequiredArgsConstructor;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static nl.brandfonds.Brandfonds.utils.ImageManipulation.resizeAndCompressImage;

@Service
public class UserService {

    private static final String FOLDER_NAME = "profile_pictures";

    @Autowired
    private UserRepository userRepository;

    @Value("${fileSaveLocation}")
    private String fileSaveLocation;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException.UserNotFoundException(id));
    }

    public User getByMail(String email) {
        return userRepository.getByMail(email).orElseThrow(() -> new NotFoundException.UserNotFoundException(email));
    }

    public boolean exists(String mail) {
        return userRepository.getByMail(mail).isPresent();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void saveProfilePicture(Integer userId, MultipartFile file) throws IOException {
        var user = getById(userId);
        String fileType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
        String filename = String.format("%s.%s", user.getId(), fileType);
        String filePath = getProfilePictureFilePath(filename);

        BufferedImage bufferedImageResource = resizeAndCompressImage(file, 500, 500);
        java.io.File outputFile = new java.io.File(filePath);
        ImageIO.write(bufferedImageResource, fileType, outputFile);

        // cleanup temp file
        File tempToDeleteFile = new File("compressed_image." + fileType);
        tempToDeleteFile.delete();

        user.setProfilePictureFileName(filename);
        userRepository.saveAndFlush(user);
    }

    public byte[] getEncodedProfilePicture(Integer userId) throws IOException {
        var user = getById(userId);
        if (user.getProfilePictureFileName() == null || user.getProfilePictureFileName().isEmpty()) {
            throw new NotFoundException.FileNotFoundException(user.getProfilePictureFileName(), fileSaveLocation + FOLDER_NAME);
        }

        var path = Paths.get(getProfilePictureFilePath(user.getProfilePictureFileName()));
        return Files.readAllBytes(path);
    }

    private String getProfilePictureFilePath(String filename) {
        var fileSeparator = System.getProperty("file.separator");

        return String.format("%s%s%s%s", fileSaveLocation, FOLDER_NAME, fileSeparator, filename);
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.getByMail(username).orElseThrow(() -> new NotFoundException.UserNotFoundException(username));
            }
        };
    }
}
