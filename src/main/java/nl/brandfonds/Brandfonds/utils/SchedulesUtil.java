package nl.brandfonds.Brandfonds.utils;

import lombok.RequiredArgsConstructor;
import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulesUtil {

    @Autowired
    PasswordChangeRequestRepository passwordChangeRequestRepository;

    @Autowired
    RegisterRequestRepository registerRequestRepository;

    //Check every 30 seconds if link is invalid

    @Scheduled(cron = "0 0/5 * * * ?")
    public void deletePasswordRequests() {
        passwordChangeRequestRepository.findAll().forEach(passwordChangeRequest -> {
            if (LocalDateTime.now().isAfter(passwordChangeRequest.getInitialDate().plusMinutes(20))
            ) {
                passwordChangeRequestRepository.delete(passwordChangeRequest);
            }
        });
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void deleteRegisterRequests() {
        registerRequestRepository.findAll().forEach(registerRequest -> {
            if (LocalDateTime.now().isAfter(registerRequest.getInitialDate().plusMinutes(20))
            ) {
                registerRequestRepository.delete(registerRequest);
            }
        });
    }
}
