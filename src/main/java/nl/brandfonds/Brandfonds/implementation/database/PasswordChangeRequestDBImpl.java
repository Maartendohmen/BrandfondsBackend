package nl.brandfonds.Brandfonds.implementation.database;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.abstraction.IPasswordChangeRequestService;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PasswordChangeRequestDBImpl implements IPasswordChangeRequestService {

    @Autowired
    PasswordChangeRequestRepository passwordChangeRequestRepository;

    @Override
    public List<PasswordChangeRequest> getAll() {
        return passwordChangeRequestRepository.findAll();
    }

    @Override
    public Optional<PasswordChangeRequest> getOne(Integer id) {
        return passwordChangeRequestRepository.findById(id);
    }

    @Override
    public void save(PasswordChangeRequest passwordChangeRequest) {
        passwordChangeRequestRepository.save(passwordChangeRequest);
        log.info("A password change request for mailadres {} was created",
                passwordChangeRequest.getMailadres());
    }

    @Override
    public void delete(PasswordChangeRequest passwordChangeRequest) {
        passwordChangeRequestRepository.delete(passwordChangeRequest);
        log.info("A password change request for {} was deleted",
                passwordChangeRequest.getMailadres());
    }

    @Override
    public Optional<PasswordChangeRequest> getByRandomString(String randomString) {
        return passwordChangeRequestRepository.getByRandomString(randomString);
    }
}
