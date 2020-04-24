package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.abstraction.IPasswordChangeRequestService;
import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordChangeRequestDBImpl implements IPasswordChangeRequestService {

    @Autowired
    PasswordChangeRequestRepository passwordChangeRequestRepository;

    @Override
    public List<PasswordChangeRequest> GetAll() {
        return passwordChangeRequestRepository.findAll();
    }

    @Override
    public PasswordChangeRequest GetOne(Integer id) {
        return passwordChangeRequestRepository.getOne(id);
    }

    @Override
    public void Save(PasswordChangeRequest passwordChangeRequest) {
        passwordChangeRequestRepository.save(passwordChangeRequest);
    }

    @Override
    public void Delete(PasswordChangeRequest passwordChangeRequest) {
        passwordChangeRequestRepository.delete(passwordChangeRequest);
    }

    @Override
    public PasswordChangeRequest GetByrandomString(String randomString) {
        return passwordChangeRequestRepository.GetByrandomString(randomString);
    }
}
