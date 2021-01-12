package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterRequestDBImpl implements IRegisterRequestService {

    @Autowired
    RegisterRequestRepository registerRequestRepository;

    @Override
    public List<RegisterRequest> GetAll() {
        return registerRequestRepository.findAll();
    }

    @Override
    public RegisterRequest GetOne(Integer id) {
        return registerRequestRepository.getOne(id);
    }

    @Override
    public void Save(RegisterRequest registerRequest) {
        registerRequestRepository.save(registerRequest);
    }

    @Override
    public void Delete(RegisterRequest registerRequest) {
        registerRequestRepository.delete(registerRequest);
    }

    @Override
    public RegisterRequest GetByrandomString(String randomString) {
        return registerRequestRepository.GetByrandomString(randomString);
    }
}
