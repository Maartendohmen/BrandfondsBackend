package nl.brandfonds.Brandfonds.utils;

import nl.brandfonds.Brandfonds.abstraction.IPasswordChangeRequestService;
import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class SchedulesUtil {

    @Autowired
    IPasswordChangeRequestService passwordChangeRequestService;

    @Autowired
    IRegisterRequestService registerRequestService;

    //Check every 30 seconds if link is invalid

    @Scheduled(cron = "0 0/5 * * * ?")
    public void deletePasswordRequests() {

        passwordChangeRequestService.getAll().forEach(passwordChangeRequest -> {
            if (LocalDateTime.now().isAfter(passwordChangeRequest.getInitialDate().plusMinutes(20))
            ) {
                passwordChangeRequestService.delete(passwordChangeRequest);
            }
        });
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void deleteRegisterRequests() {
        registerRequestService.getAll().forEach(registerRequest -> {
            if (LocalDateTime.now().isAfter(registerRequest.getInitialDate().plusMinutes(20))
            ) {
                registerRequestService.delete(registerRequest);
            }
        });
    }
}
