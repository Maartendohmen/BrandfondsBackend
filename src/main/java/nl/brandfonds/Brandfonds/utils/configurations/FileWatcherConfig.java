package nl.brandfonds.Brandfonds.utils.configurations;

import nl.brandfonds.Brandfonds.abstraction.IReceiptService;
import nl.brandfonds.Brandfonds.utils.FileWatcherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.File;
import java.time.Duration;

@Configuration
public class FileWatcherConfig implements CommandLineRunner {

    @Autowired
    IReceiptService receiptService;

    @Value("${file.savelocation}")
    private String fileSaveLocation;

    @Bean
    public FileSystemWatcher fileSystemWatcher() {
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(true, Duration.ofMillis(5000L), Duration.ofMillis(3000L));
        fileSystemWatcher.addSourceDirectory(new File(fileSaveLocation));
        fileSystemWatcher.addListener(new FileWatcherUtil());
        fileSystemWatcher.start();
        System.out.println("started fileSystemWatcher");
        return fileSystemWatcher;
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        fileSystemWatcher().stop();
    }

    // Need to add receiptservice to watcher this way, as watchers aren't managed in the spring container,
    // see https://programmersought.com/article/50561446768/
    @Override
    public void run(String... args) throws Exception {
        FileWatcherUtil.setReceiptService(receiptService);
    }
}
