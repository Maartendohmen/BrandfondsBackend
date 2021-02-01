package nl.brandfonds.Brandfonds.utils;

import nl.brandfonds.Brandfonds.abstraction.IPasswordChangeRequestService;
import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
            if (new Date().getTime() - passwordChangeRequest.getInitialdate().getTime() >= 20 * 60 * 1000
            ) {
                passwordChangeRequestService.delete(passwordChangeRequest);
            }
        });
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void deleteRegisterRequests() {
        registerRequestService.getAll().forEach(registerRequest -> {
            if (new Date().getTime() - registerRequest.getInitialdate().getTime() >= 20 * 60 * 1000
            ) {
                registerRequestService.delete(registerRequest);
            }
        });
    }
}
