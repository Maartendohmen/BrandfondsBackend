package nl.brandfonds.Brandfonds.utils;

import nl.brandfonds.Brandfonds.abstraction.IReceiptService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.implementation.database.ReceiptDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

@Component
public class FileWatcherUtil implements FileChangeListener {


    private static IReceiptService receiptService;

    public static IReceiptService getReceiptService() {
        return receiptService;
    }

    public static void setReceiptService(IReceiptService receiptService) {
        FileWatcherUtil.receiptService = receiptService;
    }

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for (ChangedFiles cfiles : changeSet) {
            for (ChangedFile cfile : cfiles.getFiles()) {
                if (cfile.getType().equals(ChangedFile.Type.DELETE)) {
                    switch (cfile.getFile().getParentFile().getName()) {
                        case "receipts":
                            try {
                                String filename = cfile.getFile().getName();
                                receiptService.delete(receiptService.getReceiptByName(filename));
                            } catch (NotFoundException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
        }
    }

    private boolean isLocked(Path path) {
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock()) {
            return lock == null;
        } catch (IOException e) {
            return true;
        }
    }
}
