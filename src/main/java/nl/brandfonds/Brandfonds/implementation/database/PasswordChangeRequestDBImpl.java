package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IPasswordChangeRequestService;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordChangeRequestDBImpl implements IPasswordChangeRequestService {

    @Autowired
    PasswordChangeRequestRepository passwordChangeRequestRepository;

    @Override
    public List<PasswordChangeRequest> getAll() {
        return passwordChangeRequestRepository.findAll();
    }

    @Override
    public PasswordChangeRequest getOne(Integer id) {
        return passwordChangeRequestRepository.getOne(id);
    }

    @Override
    public void save(PasswordChangeRequest passwordChangeRequest) {
        passwordChangeRequestRepository.save(passwordChangeRequest);
    }

    @Override
    public void delete(PasswordChangeRequest passwordChangeRequest) {
        passwordChangeRequestRepository.delete(passwordChangeRequest);
    }

    @Override
    public Optional<PasswordChangeRequest> getByrandomString(String randomString) {
        return passwordChangeRequestRepository.getByrandomString(randomString);
    }
}
