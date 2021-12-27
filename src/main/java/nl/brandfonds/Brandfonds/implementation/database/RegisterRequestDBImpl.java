package nl.brandfonds.Brandfonds.implementation.database;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegisterRequestDBImpl implements IRegisterRequestService {

    @Autowired
    RegisterRequestRepository registerRequestRepository;

    @Override
    public List<RegisterRequest> getAll() {
        return registerRequestRepository.findAll();
    }

    @Override
    public RegisterRequest getOne(Integer id) {
        return registerRequestRepository.getOne(id);
    }

    @Override
    public void save(RegisterRequest registerRequest) {
        registerRequestRepository.save(registerRequest);
        log.info("A register request for user {} {} with mailadres {} was created",
                registerRequest.getForname(), registerRequest.getSurname(), registerRequest.getEmailadres());
    }

    @Override
    public void delete(RegisterRequest registerRequest) {
        registerRequestRepository.delete(registerRequest);
        log.info("A register request for user {} {} with mailadres {} was deleted",
                registerRequest.getForname(), registerRequest.getSurname(), registerRequest.getEmailadres());
    }

    @Override
    public Optional<RegisterRequest> getByrandomString(String randomString) {
        return registerRequestRepository.getByrandomString(randomString);
    }
}
