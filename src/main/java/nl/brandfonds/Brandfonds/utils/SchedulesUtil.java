package nl.brandfonds.Brandfonds.utils;

import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SchedulesUtil {

    @Autowired
    PasswordChangeRequestRepository passwordChangeRequestRepository;

    @Autowired
    RegisterRequestRepository registerRequestRepository;

    //Check every 30 seconds if link is invalid

    @Scheduled(fixedDelay = 30000)
    public void DeletePasswordRequests() {

        passwordChangeRequestRepository.findAll().forEach(passwordChangeRequest -> {
            if (new Date().getTime() - passwordChangeRequest.getInitialdate().getTime() >= 20 * 60 * 1000
                    ) {
                passwordChangeRequestRepository.delete(passwordChangeRequest);
            }
        });
    }

    @Scheduled(fixedDelay = 30000)
    public void DeleteRegisterRequests() {
        registerRequestRepository.findAll().forEach(registerRequest -> {
            if (new Date().getTime() - registerRequest.getInitialdate().getTime() >= 20 * 60 * 1000
                    ) {
                registerRequestRepository.delete(registerRequest);
            }
        });
    }
}
